package Control;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import Entity.Applicant;
import Entity.Job;
import Entity.Match;
import Entity.InterviewSchedule;
import Control.MatchControl;
import Boundary.MatchUI;
import adt.LinkedList;
import adt.ListInterface;

public class InterviewControl {

    private LinkedList<InterviewSchedule> interviewList;
    private ApplicantControl applicantControl;
    private JobControl jobControl;
    private MatchControl matchControl;

    public InterviewControl(ApplicantControl applicantControl, JobControl jobControl, MatchControl matchControl) {
        this.interviewList = new LinkedList<>();
        this.applicantControl = applicantControl;
        this.jobControl = jobControl;
        this.matchControl = matchControl;
    }

    public void addInterviewSchedule(Applicant applicant, Job job, LocalDate interviewDate) {
        interviewDate = LocalDate.now().plusDays(7);
        InterviewSchedule schedule = new InterviewSchedule(applicant, job, interviewDate);
        interviewList.add(schedule);
    }

    //the application must consist of applicant name,ID...
    //if the application is valid, add the schedule,set validation on days
    public LocalDate adjustInterviewSchedule(int days) {
        LocalDate interviewDate = LocalDate.now().plusDays(days);
        return interviewDate;
    }

    //list out all jobs
    public void listJobsByCompany(String companyName, LinkedList<Job> jobList) {
        System.out.println("Jobs under company: " + companyName);

        ListInterface.LinkedListIterator iterator = jobList.getIterator();
        while (iterator.hasNext()) {
            Job job = (Job) iterator.next();
            if (job.getCompany().equals(companyName)) {
                System.out.println("- " + job.getTitle() + " (ID: " + job.getJobID() + ")");
            }
        }
    }

    //........................
    public LinkedList<Job> getMatchedJobs(String input) {
        //LinkedList<Applicant> allApplicants = applicantControl.getAllApplicants();
        LinkedList<Job> allJobs = (LinkedList<Job>) jobControl.getAllJobs();
        LinkedList<Job> matchedJobs = new LinkedList<>();

        // Match job by ID or Type
        for (int i = 1; i <= allJobs.getNumberOfEntries(); i++) {
            Job job = allJobs.getEntry(i);
            if (job.getJobID().equalsIgnoreCase(input) || job.getJobType().equalsIgnoreCase(input)) {
                matchedJobs.add(job);
            }
        }

        return matchedJobs;
    }

    public LinkedList<Match> checkApplicantsForJob(Job job) {
        LinkedList<Match> matches = new LinkedList<>();
        LinkedList<Applicant> applicantList = applicantControl.getAllApplicants();
        //LinkedList<Job> allJobs = (LinkedList<Job>) jobControl.getAllJobs();
        /*
    LinkedList<Match> allMatches = matchControl.findAllMatches(DEFAULT_SCORE_THRESHOLD);
        displayMatches(allMatches, "All Potential Matches Found");
         */
        for (int i = 1; i <= applicantList.getNumberOfEntries(); i++) {
            Applicant applicant = applicantList.getEntry(i);
            matchControl.calculateMatchScore(applicant, job);
            double score = matchControl.calculateMatchScore(applicant, job);
            //System.out.print("\napplicant: "+applicant.getName()+" score: "+score); //tester
            // Check if this applicant has qualify to apply the job
            // !!! THE SCORE MUST CHANGE to COMPANY SET SCORE
            if (applicant.getJobType().equalsIgnoreCase(job.getJobType()) && score >= 0.5) {
                //System.out.print("\nscore: "+score);
                //double score = matchControl.calculateMatchScore(applicant, job);
                matches.add(new Match(applicant, job, score)); // Create a Match object
            }
        }
        return matches;
    }

    public void sortMatchesByScore(LinkedList<Match> matches) {
        matches.sort((m1, m2) -> Double.compare(m2.getMatchScore(), m1.getMatchScore())); // Sort matches by score descending
    }

    public void displayMatches(LinkedList<Match> matches) {
        if (matches.isEmpty()) {
            System.out.println("  No applicants found.");
        } else {
            for (int i = 1; i <= matches.getNumberOfEntries(); i++) {
                Match match = matches.getEntry(i);
                System.out.printf("  %-15s Match Score: %.2f%n", match.getApplicant().getName(), match.getMatchScore());
            }
        }
    }

//applyApplicant() start--------------------------------------------------------------------
    public LinkedList<Job> getMatchingJobsForApplicant(Applicant applicant, LinkedList<Job> uniquedJobs) {
        LinkedList<Job> matchingJobs = new LinkedList<>();
        String applicantJobType = applicant.getJobType();
        // Find jobs that match the applicant's job type
        for (int i = 1; i <= uniquedJobs.getNumberOfEntries(); i++) {
            Job job = uniquedJobs.getEntry(i);
            if (applicantJobType.equalsIgnoreCase(job.getJobType())) {
                matchingJobs.add(job);
            }
        }
        return matchingJobs;
    }

    public int getApplicantsCountForJob(Job job) {
        int count = 0;
        ListInterface.LinkedListIterator<InterviewSchedule> iterator = interviewList.getIterator();

        while (iterator.hasNext()) {
            InterviewSchedule schedule = iterator.next();
            if (schedule.getJob().equals(job)) {
                count++;
            }
        }
        return count;
    }

    // Check the score is above the company type jobs threshold
    public boolean isApplicantQualifiedForJob(Applicant applicant, Job job) {
        // Calculate match score
        double score = matchControl.calculateMatchScore(applicant, job);

        // Check if job type matches and if score is above the threshold
        return applicant.getJobType().equalsIgnoreCase(job.getJobType()) && score >= 0.5;
    }

    public LinkedList<Applicant> getTopApplicantsForJob(Job job, int limit) {
        LinkedList<Match> matches = new LinkedList<>();
        LinkedList<Applicant> applicantList = applicantControl.getAllApplicants();

        for (int i = 1; i <= applicantList.getNumberOfEntries(); i++) {
            Applicant applicant = applicantList.getEntry(i);

            // Ensure job type matches first
            if (applicant.getJobType().equalsIgnoreCase(job.getJobType())) {
                double score = matchControl.calculateMatchScore(applicant, job);

                // Optional: You can also check if score passes a threshold (like job min score)
                matches.add(new Match(applicant, job, score));
            }
        }

        // Sort matches by score descending
        sortMatchesByScore(matches); // Assuming you have this already

        // Pick top N applicants
        LinkedList<Applicant> topApplicants = new LinkedList<>();
        for (int i = 1; i <= Math.min(matches.getNumberOfEntries(), limit); i++) {
            topApplicants.add(matches.getEntry(i).getApplicant());
        }

        return topApplicants;
    }

    public boolean hasApplicantAlreadyApplied(Applicant applicant, Job job) {
        boolean alreadyApplied = false;

        // Check if applicant has already applied to this job
        for (int i = 1; i <= applicant.getAppliedJobIds().getNumberOfEntries(); i++) {
            if (job.getJobID().equalsIgnoreCase(applicant.getAppliedJobIds().getEntry(i))) {
                alreadyApplied = true;
                break;
            }
        }

        return alreadyApplied;
    }

    public void applyForJob(Applicant applicant, Job job) {
        // Add the job ID to the applicant's list of applied job IDs
        applicant.getAppliedJobIds().add(job.getJobID());
    }

//applyApplicant() end---------------------------------------------------------------
    //
    public LinkedList<Applicant> getUniqueApplicants() {
        LinkedList<Applicant> uniqueApplicants = new LinkedList<>();
        ListInterface.LinkedListIterator<InterviewSchedule> iterator = interviewList.getIterator();

        while (iterator.hasNext()) {
            Applicant applicant = iterator.next().getApplicant();
            boolean found = false;

            for (int i = 0; i < uniqueApplicants.getNumberOfEntries(); i++) {
                if (uniqueApplicants.getEntry(i + 1).getApplicantId().equals(applicant.getApplicantId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                uniqueApplicants.add(applicant);
            }
        }

        return uniqueApplicants;
    }

    public LinkedList<InterviewSchedule> getSchedulesForApplicant(Applicant applicant) {
        LinkedList<InterviewSchedule> schedules = new LinkedList<>();
        ListInterface.LinkedListIterator<InterviewSchedule> iterator = interviewList.getIterator();

        while (iterator.hasNext()) {
            InterviewSchedule schedule = iterator.next();
            if (schedule.getApplicant().getApplicantId().equals(applicant.getApplicantId())) {
                schedules.add(schedule);
            }
        }

        return schedules;
    }

    public LinkedList<String> getUniqueJobTypesForApplicantSchedules(LinkedList<InterviewSchedule> schedules) {
        LinkedList<Job> uniquedJobs = (LinkedList<Job>) jobControl.getAllJobs();
        LinkedList<String> jobTypes = new LinkedList<>();

        for (int i = 0; i < schedules.getNumberOfEntries(); i++) {
            String jobType = schedules.getEntry(i + 1).getJob().getJobType();
            if (!jobTypes.contains(jobType)) {
                jobTypes.add(jobType);
            }
        }

        return jobTypes;
    }

    public void getCompaniesAndJobIDsForJobType(String jobType, LinkedList<InterviewSchedule> schedules,
            LinkedList<String> companyList, LinkedList<String> jobIdList) {
        LinkedList<Applicant> allApplicants = applicantControl.getAllApplicants();
        LinkedList<Job> allJobs = (LinkedList<Job>) jobControl.getAllJobs();

        for (int i = 0; i < schedules.getNumberOfEntries(); i++) {
            InterviewSchedule schedule = schedules.getEntry(i + 1);
            Job job = schedule.getJob();

            if (job.getJobType().equals(jobType)) {
                String company = job.getCompany();
                String jobId = job.getJobID();

                if (!companyList.contains(company)) {
                    companyList.add(company);
                }
                if (!jobIdList.contains(jobId)) {
                    jobIdList.add(jobId);
                }
            }
        }
    }

    public LinkedList<InterviewSchedule> getInterviewList() {
        return interviewList;
    }

    public void addInterviewSchedule(InterviewSchedule schedule) {
        interviewList.add(schedule);
    }
    //() end

    //-------------------------------------------------------------------------------------------------------
    //filterApplicantSchedule() start
    public LinkedList<Applicant> getApplicantsSchedule() {
        LinkedList<Applicant> uniqueApplicants = new LinkedList<>();
        ListInterface.LinkedListIterator<InterviewSchedule> iterator = interviewList.getIterator();

        while (iterator.hasNext()) {
            InterviewSchedule schedule = iterator.next();
            Applicant applicant = schedule.getApplicant();
            boolean found = false;

            for (int i = 0; i < uniqueApplicants.getNumberOfEntries(); i++) {
                if (uniqueApplicants.getEntry(i + 1).getApplicantId().equals(applicant.getApplicantId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                uniqueApplicants.add(applicant);
            }
        }
        return uniqueApplicants;
    }

    public LinkedList<InterviewSchedule> uniqueApplicantSchedules(Applicant applicant) {
        LinkedList<InterviewSchedule> result = new LinkedList<>();
        ListInterface.LinkedListIterator<InterviewSchedule> iterator = interviewList.getIterator();

        while (iterator.hasNext()) {
            InterviewSchedule schedule = iterator.next();
            if (schedule.getApplicant().getApplicantId().equals(applicant.getApplicantId())) {
                result.add(schedule);
            }
        }
        return result;
    }

    public LinkedList<String> getJobForApplicantSchedules(LinkedList<InterviewSchedule> schedules) {
        LinkedList<Job> allJobs = (LinkedList<Job>) jobControl.getAllJobs();
        LinkedList<String> jobTypes = new LinkedList<>();

        for (int i = 0; i < schedules.getNumberOfEntries(); i++) {
            String jobType = schedules.getEntry(i + 1).getJob().getJobType();
            if (!jobTypes.contains(jobType)) {
                jobTypes.add(jobType);
            }
        }
        return jobTypes;
    }

    public void getCompaniesInformation(String jobType, LinkedList<InterviewSchedule> schedules,
            LinkedList<String> companyList, LinkedList<String> jobIdList) {
        for (int i = 0; i < schedules.getNumberOfEntries(); i++) {
            InterviewSchedule schedule = schedules.getEntry(i + 1);
            Job job = schedule.getJob();

            if (job.getJobType().equals(jobType)) {
                String company = job.getCompany();
                String jobId = job.getJobID();

                if (!companyList.contains(company)) {
                    companyList.add(company);
                }
                if (!jobIdList.contains(jobId)) {
                    jobIdList.add(jobId);
                }
            }
        }
    }

    public void displayApplicantJobMatches(Applicant applicant, String jobType, LinkedList<InterviewSchedule> schedules) {
        System.out.println("\nApplicant: " + applicant.getName() + " (" + applicant.getApplicantId() + ")");
        System.out.println("Job Type: " + jobType);

        ListInterface.LinkedListIterator<InterviewSchedule> iterator = schedules.getIterator();
        while (iterator.hasNext()) {
            InterviewSchedule schedule = iterator.next();
            if (schedule.getJob().getJobType().equals(jobType)) {
                Job job = schedule.getJob();
                System.out.println(" - Job ID: " + job.getJobID() + ", Company: " + job.getCompany());
            }
        }
    }

    public Applicant findApplicantById(String id) {
        LinkedList<Applicant> allApplicants = applicantControl.getAllApplicants();
        LinkedList<Job> allJobs = (LinkedList<Job>) jobControl.getAllJobs();
        for (int i = 0; i < allApplicants.getNumberOfEntries(); i++) {
            Applicant a = allApplicants.getEntry(i + 1);
            if (id.equalsIgnoreCase(a.getApplicantId())) {
                return a;
            }
        }
        return null;
    }
    //filterApplicantSchedule() end

    //-------------------------------------------------------------------------------------------------------------
    //filterCompanySchedule() start
    public LinkedList<Job> getJobsByCompany(String companyName) {
        LinkedList<Job> companyJobs = new LinkedList<>();
        ListInterface.LinkedListIterator<InterviewSchedule> iterator = interviewList.getIterator();

        while (iterator.hasNext()) {
            Job job = iterator.next().getJob();
            if (job.getCompany().equalsIgnoreCase(companyName) && !companyJobs.contains(job)) {
                companyJobs.add(job);
            }
        }
        return companyJobs;
    }

    public LinkedList<InterviewSchedule> getSchedulesByJob(Job job) {
        LinkedList<InterviewSchedule> jobSchedules = new LinkedList<>();
        ListInterface.LinkedListIterator<InterviewSchedule> iterator = interviewList.getIterator();

        while (iterator.hasNext()) {
            InterviewSchedule schedule = iterator.next();
            if (schedule.getJob().getJobID().equals(job.getJobID())) {
                jobSchedules.add(schedule);
            }
        }
        return jobSchedules;
    }

    public void displayCompanyJobSchedules(String companyName) {
        LinkedList<Job> companyJobs = getJobsByCompany(companyName);
        if (companyJobs.isEmpty()) {
            System.out.println("No jobs found for " + companyName);
            return;
        }
        for (int i = 0; i < companyJobs.getNumberOfEntries(); i++) {
            Job job = companyJobs.getEntry(i + 1);
            displayJobWithSchedules(job);
        }
    }

    public void displayJobWithSchedules(Job job) {
        LinkedList<InterviewSchedule> jobSchedules = getSchedulesByJob(job);

        if (!jobSchedules.isEmpty()) {
            System.out.println("\n➤ Job Title: " + job.getTitle());
            System.out.println("   Job ID   : " + job.getJobID());

            for (int i = 0; i < jobSchedules.getNumberOfEntries(); i++) {
                InterviewSchedule schedule = jobSchedules.getEntry(i + 1);
                displayScheduleInfo(schedule);
            }
        }
    }

    public void displayScheduleInfo(InterviewSchedule schedule) {
        Applicant applicant = schedule.getApplicant();
        Job job = schedule.getJob();

        // Dynamically find match score if not stored in schedule
        double score = schedule.getScore(); // default

        // Try to get score from Match if available
        LinkedList<Match> allMatches = getAllApplicantScoresForJobs();
        for (int i = 1; i <= allMatches.getNumberOfEntries(); i++) {
            Match match = allMatches.getEntry(i);
            if (match.getApplicant().getApplicantId().equals(applicant.getApplicantId())
                    && match.getJob().getJobID().equals(job.getJobID())) {
                score = match.getMatchScore(); // override with actual score
                break;
            }
        }

        System.out.print("   - Interview Start: " + job.getInterviewStartTime());
        System.out.print(" | Applicant: " + applicant.getName() + " (" + applicant.getApplicantId() + ")");
        System.out.println(" | Score: " + score);
    }

    //filterCompanySchedule() end
    //------------------------------------------------------------------------------------------------------
    //display all company schedule
    public LinkedList<String> getCompanyNames() {
        LinkedList<String> companyList = new LinkedList<>();
        ListInterface.LinkedListIterator<InterviewSchedule> iterator = interviewList.getIterator();

        while (iterator.hasNext()) {
            InterviewSchedule schedule = iterator.next();
            String company = schedule.getJob().getCompany();

            boolean found = false;
            for (int i = 1; i <= companyList.getNumberOfEntries(); i++) {
                if (companyList.getEntry(i).equalsIgnoreCase(company)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                companyList.add(company);
            }
        }

        return companyList;
    }

    public int getInterviewStartTime(String company) {
        LinkedList<Job> allJobs = (LinkedList<Job>) jobControl.getAllJobs();

        for (int i = 1; i <= allJobs.getNumberOfEntries(); i++) {
            Job job = allJobs.getEntry(i);
            if (job.getCompany().equalsIgnoreCase(company)) {
                return job.getInterviewStartTime(); // direct from Job object
            }
        }

        return -1; // fallback
    }

    public int getInterviewEndTime(String company) {
        LinkedList<Job> allJobs = (LinkedList<Job>) jobControl.getAllJobs();

        for (int i = 1; i <= allJobs.getNumberOfEntries(); i++) {
            Job job = allJobs.getEntry(i);
            if (job.getCompany().equalsIgnoreCase(company)) {
                return job.getInterviewEndTime(); // direct from Job object
            }
        }

        return -1; // fallback
    }

    public void printApplicantsForCompany(String company) {
        ListInterface.LinkedListIterator<InterviewSchedule> iterator = interviewList.getIterator();
        while (iterator.hasNext()) {
            InterviewSchedule schedule = iterator.next();
            if (schedule.getJob().getCompany().equals(company)) {
                Applicant applicant = schedule.getApplicant();
                System.out.println(" - " + applicant.getName() + " (ID: " + applicant.getApplicantId() + ")");
            }
        }
    }

    public LocalDate getInterviewDate(String company) {
        for (int i = 1; i <= interviewList.getNumberOfEntries(); i++) {
            InterviewSchedule schedule = interviewList.getEntry(i);
            if (schedule.getJob().getCompany().equalsIgnoreCase(company)) {
                return schedule.getInterviewDate().plusDays(7); // Assuming this returns a LocalDate
            }
        }
        return LocalDate.now().plusDays(7); // or LocalDate.now() if you want a fallback
    }

    public LinkedList<Match> getAllApplicantScoresForJobs() {
        LinkedList<Match> allMatches = new LinkedList<>();

        LinkedList<Job> allJobs = (LinkedList<Job>) jobControl.getAllJobs(); // get all jobs
        LinkedList<Applicant> allApplicants = applicantControl.getAllApplicants(); // get all applicants

        for (int i = 1; i <= allJobs.getNumberOfEntries(); i++) {
            Job job = allJobs.getEntry(i);

            // Get match scores for all applicants for this job
            LinkedList<Match> jobMatches = checkApplicantsForJob(job);

            // Add all matches to the master list
            for (int j = 1; j <= jobMatches.getNumberOfEntries(); j++) {
                allMatches.add(jobMatches.getEntry(j));
            }
        }
        return allMatches;
    }

}

