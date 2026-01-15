package Control;

import Entity.Job;
import Entity.Applicant;
import adt.LinkedList;
import adt.ListInterface;

/**
 * 
 * @author Wiston
 */
public class ApplicantControl {
    private LinkedList<Applicant> applicantList;
    private JobControl jobControl;

    public ApplicantControl(JobControl jobControl) {
        if (jobControl == null) {
            throw new IllegalArgumentException("JobControl cannot be null");
        }
        this.jobControl = jobControl;
        this.applicantList = new LinkedList<>();
    }

    public void addApplicant(Applicant applicant) {
        if (applicant == null) {
            System.out.println("Cannot add null applicant.");
            return;
        }
        applicantList.add(applicant);
        System.out.println("Applicant added successfully: " + applicant.getApplicantId());
    }

    public boolean removeApplicant(String applicantId) {
        if (applicantId == null || applicantId.trim().isEmpty()) {
            System.out.println("Applicant ID cannot be null or empty.");
            return false;
        }
        ListInterface.LinkedListIterator iterator = applicantList.getIterator();
        while (iterator.hasNext()) {
            Applicant applicant = (Applicant) iterator.next();
            if (applicant != null && applicant.getApplicantId().equals(applicantId)) {
                iterator.remove();
                System.out.println("Applicant removed successfully.");
                return true;
            }
        }
        System.out.println("Applicant ID not found.");
        return false;
    }

    public Applicant getApplicantById(String applicantId) {
        if (applicantId == null || applicantId.trim().isEmpty()) {
            return null;
        }
        return applicantList.findFirst(applicant -> 
            applicant != null && applicant.getApplicantId().equals(applicantId));
    }

    public LinkedList<Applicant> getAllApplicants() {
        return applicantList;
    }

    public void sortApplicantsByCGPA() {
        applicantList.sort((a1, a2) -> {
            if (a1 == null || a2 == null) return 0;
            return Double.compare(a2.getCgpa(), a1.getCgpa());
        });
    }

    public LinkedList<Applicant> filterByCGPA(double minCGPA) {
        return applicantList.filter(applicant -> 
            applicant != null && applicant.getCgpa() >= minCGPA);
    }

    public LinkedList<Applicant> filterBySkill(String skill, int minProficiency) {
        if (skill == null || skill.trim().isEmpty()) {
            return new LinkedList<>();
        }
        return applicantList.filter(applicant -> {
            if (applicant == null) return false;
            LinkedList<String> skills = applicant.getSkills();
            LinkedList<Integer> proficiencies = applicant.getProficiencies();
            if (skills == null || proficiencies == null) return false;
            ListInterface.LinkedListIterator skillIter = skills.getIterator();
            ListInterface.LinkedListIterator profIter = proficiencies.getIterator();
            while (skillIter.hasNext() && profIter.hasNext()) {
                String s = (String) skillIter.next();
                Integer p = (Integer) profIter.next();
                if (s != null && p != null && s.equals(skill) && p >= minProficiency) {
                    return true;
                }
            }
            return false;
        });
    }
    
    public LinkedList<Applicant> filterByJobType(String jobType) {
        if (jobType == null || jobType.trim().isEmpty()) {
            return new LinkedList<>();
        }
        return applicantList.filter(applicant -> 
            applicant != null && jobType.equals(applicant.getJobType()));
    }
    
    public LinkedList<Applicant> filterByLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return new LinkedList<>();
        }
        return applicantList.filter(applicant -> 
            applicant != null && location.equals(applicant.getLocation()));
    }
}