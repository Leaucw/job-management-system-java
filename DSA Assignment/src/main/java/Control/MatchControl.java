package Control;

import Entity.Applicant;
import Entity.Job;
import Entity.Match;
import adt.LinkedList;
import adt.ListInterface;
/**
 *
 * @author Hao Lun
 */
public class MatchControl {

    private ApplicantControl applicantControl;
    private JobControl jobControl;

    // Define weights for different matching criteria (can be adjusted)
    private static final double WEIGHT_CGPA = 0.30; // 30% weight for meeting CGPA requirement
    private static final double WEIGHT_SKILLS = 0.50; // 70% weight for skills match
    private static final double WEIGHT_LOCATION = 0.20; // 20% weight for location match

    public MatchControl(ApplicantControl applicantControl, JobControl jobControl) {
        if (applicantControl == null || jobControl == null) {
            throw new IllegalArgumentException("ApplicantControl and JobControl cannot be null.");
        }
        this.applicantControl = applicantControl;
        this.jobControl = jobControl;
    }

    

    /**
     * Calculates a match score between an applicant and a job.
     * Score ranges from 0.0 (no match) to 1.0 (perfect match based on criteria).
     *
     * @param applicant The applicant.
     * @param job The job.
     * @return The match score (0.0 to 1.0).
     */
    public double calculateMatchScore(Applicant applicant, Job job) {
        if (applicant == null || job == null) {
            return 0.0; // Cannot match null objects
        }
        
        // --- Job Type Check ---
        if (applicant.getJobType() == null || job.getJobType() == null ||
            !applicant.getJobType().equalsIgnoreCase(job.getJobType())) {
            return 0.0; // Hard filter: Job type mismatch
        }
        

        double score = 0.0;

        // --- 1. CGPA Check  ---
        if (applicant.getCgpa() >= job.getCurrentCGPA()) {
            score += WEIGHT_CGPA;
        }
        // --- 2. Skills Check ---
        String[] requiredSkills = job.getSkills();
        int[] requiredProficiencies = job.getProficiencies();
        LinkedList<String> applicantSkills = applicant.getSkills();
        LinkedList<Integer> applicantProficiencies = applicant.getProficiencies();

        if (requiredSkills == null || requiredSkills.length == 0) {
            // If job requires no specific skills, give full skill score component
            score += WEIGHT_SKILLS;
        } else if (applicantSkills != null && applicantProficiencies != null && requiredProficiencies != null &&
                   applicantSkills.getNumberOfEntries() > 0 && applicantProficiencies.getNumberOfEntries() == applicantSkills.getNumberOfEntries() &&
                   requiredSkills.length == requiredProficiencies.length) {

            int matchedSkillsCount = 0;
            for (int i = 0; i < requiredSkills.length; i++) {
                String reqSkill = requiredSkills[i];
                int reqProficiency = requiredProficiencies[i];

                if (reqSkill == null || reqSkill.trim().isEmpty()) {
                    continue; // Skip empty skill requirements if any
                }

                boolean skillFoundAndSufficient = false;
                ListInterface.LinkedListIterator appSkillIter = applicantSkills.getIterator();
                ListInterface.LinkedListIterator appProfIter = applicantProficiencies.getIterator();

                while (appSkillIter.hasNext() && appProfIter.hasNext()) {
                    String appSkill = (String) appSkillIter.next();
                    Integer appProficiency = (Integer) appProfIter.next();

                    if (appSkill != null && appProficiency != null &&
                        reqSkill.equalsIgnoreCase(appSkill.trim()) && // Case-insensitive skill comparison
                        appProficiency >= reqProficiency) {
                        skillFoundAndSufficient = true;
                        break; // Found this required skill with sufficient proficiency
                    }
                }

                if (skillFoundAndSufficient) {
                    matchedSkillsCount++;
                }
            }

            // Calculate skill score component based on the proportion of matched skills
            double skillMatchRatio = (requiredSkills.length > 0) ? (double) matchedSkillsCount / requiredSkills.length : 1.0;
            score += WEIGHT_SKILLS * skillMatchRatio;

        } else {
             // Applicant lacks skills or data is inconsistent, partial or zero skill score might apply
             // In this case, we give 0 for the skill component if job requires skills but applicant has none relevant/listed
             // Or if data arrays/lists are mismatched (which shouldn't happen with constructor validation)
        }

        // --- 3. Location Check ---
        if (applicant.getLocation() != null && job.getLocation() != null) {
            if (applicant.getLocation().equalsIgnoreCase(job.getLocation())) {
                score += WEIGHT_LOCATION;
            }
        }

        // --- 4. Other Factors (Future Enhancements) ---
        // - Experience level matching
        // - Weighting specific skills differently

        // Ensure score stays within bounds (0.0 to 1.0)
        return Math.max(0.0, Math.min(score, 1.0));
    }

    /**
     * Finds suitable jobs for a given applicant, ranked by match score.
     *
     * @param applicantId The ID of the applicant.
     * @param scoreThreshold Minimum match score to be included (e.g., 0.5 for 50%).
     * @return A sorted list of Matches (Job + Score).
     */
    public LinkedList<Match> findMatchesForApplicant(String applicantId, double scoreThreshold) {
        LinkedList<Match> matches = new LinkedList<>();
        Applicant applicant = applicantControl.getApplicantById(applicantId);
        if (applicant == null) {
            System.out.println("Applicant not found: " + applicantId);
            return matches; // Return empty list
        }

        LinkedList<Job> allJobs = (LinkedList<Job>) jobControl.getAllJobs();
        ListInterface.LinkedListIterator jobIter = allJobs.getIterator();
        while (jobIter.hasNext()) {
            Job job = (Job) jobIter.next();
            if (job != null) {
                // Avoid matching applicant to jobs they already applied for? (Optional business rule)
                // if (applicant.hasAppliedForJob(job.getJobID())) {
                //     continue;
                // }

                double score = calculateMatchScore(applicant, job);
                if (score >= scoreThreshold) {
                    matches.add(new Match(applicant, job, score));
                }
            }
        }

        // Sort matches by score in descending order
        matches.sort(Match::compareByScoreDesc);
        return matches;
    }

     /**
     * Finds suitable applicants for a given job, ranked by match score.
     *
     * @param jobId The ID of the job.
     * @param scoreThreshold Minimum match score to be included (e.g., 0.5 for 50%).
     * @return A sorted list of Matches (Applicant + Score).
     */
    public LinkedList<Match> findMatchesForJob(String jobId, double scoreThreshold) {
        LinkedList<Match> matches = new LinkedList<>();
        Job job = jobControl.getJobById(jobId);
        if (job == null) {
            System.out.println("Job not found: " + jobId);
            return matches; // Return empty list
        }

        LinkedList<Applicant> allApplicants = applicantControl.getAllApplicants();
        ListInterface.LinkedListIterator appIter = allApplicants.getIterator();
        while (appIter.hasNext()) {
            Applicant applicant = (Applicant) appIter.next();
             if (applicant != null) {
                // Avoid matching applicants who already applied? (Optional business rule)
                // if (applicant.hasAppliedForJob(jobId)) {
                //     continue;
                // }

                double score = calculateMatchScore(applicant, job);
                if (score >= scoreThreshold) {
                    matches.add(new Match(applicant, job, score));
                }
            }
        }

        // Sort matches by score in descending order
        matches.sort(Match::compareByScoreDesc);
        return matches;
    }

     /**
     * Finds all potential matches above a certain threshold in the system.
     * Note: This can be computationally expensive for large numbers of applicants and jobs.
     *
     * @param scoreThreshold Minimum match score.
     * @return A sorted list of all Matches found.
     */
     public LinkedList<Match> findAllMatches(double scoreThreshold) {
         LinkedList<Match> allMatches = new LinkedList<>();
         LinkedList<Applicant> allApplicants = applicantControl.getAllApplicants();
         LinkedList<Job> allJobs = (LinkedList<Job>) jobControl.getAllJobs();

         ListInterface.LinkedListIterator appIter = allApplicants.getIterator();
         while (appIter.hasNext()) {
             Applicant applicant = (Applicant) appIter.next();
             if (applicant != null) {
                 ListInterface.LinkedListIterator jobIter = allJobs.getIterator();
                 while (jobIter.hasNext()) {
                     Job job = (Job) jobIter.next();
                     if (job != null) {
                         double score = calculateMatchScore(applicant, job);
                         if (score >= scoreThreshold) {
                             allMatches.add(new Match(applicant, job, score));
                         }
                     }
                 }
             }
         }

         // Sort all matches by score
         allMatches.sort(Match::compareByScoreDesc);
         return allMatches;
     }
}
