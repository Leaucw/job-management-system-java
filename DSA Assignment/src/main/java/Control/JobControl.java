package Control;

import Entity.Job;
import adt.LinkedList;
import adt.ListInterface;

public class JobControl {
    private ListInterface<Job> jobList = new LinkedList<>();

    public void addJob(Job job) {
        if (job == null) {
            throw new IllegalArgumentException("Job cannot be null");
        }
        validateJob(job);
        jobList.add(job);
    }

    public boolean updateJob(String jobID, String newTitle, String newCompany, double newSalary, 
                           double newCGPA, int newStartTime, int newEndTime, 
                           String newLocation, String newJobType, 
                           String[] newSkills, int[] newProficiencies) {
        Job foundJob = jobList.findFirst(job -> job.getJobID().equals(jobID));
        
        if (foundJob == null) {
            return false;
        }

        // Validate all fields before updating
        if (newTitle != null && newTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (newCompany != null && newCompany.trim().isEmpty()) {
            throw new IllegalArgumentException("Company cannot be empty");
        }
        if (newSalary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        if (newCGPA < 0 || newCGPA > 4) {
            throw new IllegalArgumentException("CGPA must be between 0 and 4");
        }
        if (newStartTime >= 0) {
            validateTime(newStartTime);
        }
        if (newEndTime >= 0) {
            validateTime(newEndTime);
        }
        if (newStartTime >= 0 && newEndTime >= 0 && newStartTime >= newEndTime) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        if (newLocation != null && !isValidLocation(newLocation)) {
            throw new IllegalArgumentException("Invalid location");
        }
        if (newJobType != null && !isValidJobType(newJobType)) {
            throw new IllegalArgumentException("Invalid job type");
        }
        if (newSkills != null) {
            validateSkills(newSkills, newProficiencies);
        }

        // Update the job
        if (newTitle != null) foundJob.setTitle(newTitle);
        if (newCompany != null) foundJob.setCompany(newCompany);
        if (newSalary >= 0) foundJob.setSalary(newSalary);
        if (newCGPA >= 0) foundJob.setCurrentCGPA(newCGPA);
        if (newStartTime >= 0 || newEndTime >= 0) {
            int start = newStartTime >= 0 ? newStartTime : foundJob.getInterviewStartTime();
            int end = newEndTime >= 0 ? newEndTime : foundJob.getInterviewEndTime();
            foundJob.setInterviewTimes(start, end);
        }
        if (newLocation != null) foundJob.setLocation(newLocation);
        if (newJobType != null) foundJob.setJobType(newJobType);
        if (newSkills != null) foundJob.setSkills(newSkills, newProficiencies);
        
        return true;
    }

    public boolean removeJob(String jobID) {
        if (jobID == null || jobID.trim().isEmpty()) {
            throw new IllegalArgumentException("Job ID cannot be empty");
        }
        
        ListInterface.LinkedListIterator iterator = jobList.getIterator();
        while (iterator.hasNext()) {
            Job job = (Job) iterator.next();
            if (job.getJobID().equals(jobID)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    public void searchJobs(String keyword) {
        ListInterface<Job> matchingJobs = new LinkedList<>(); // Using ListInterface for matching jobs
        
        ListInterface.LinkedListIterator iterator = jobList.getIterator();
        while (iterator.hasNext()) {
            Job job = (Job) iterator.next();
            // Case-insensitive matching for the keyword in job title, company, and location
            if (job.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                job.getCompany().toLowerCase().contains(keyword.toLowerCase()) ||
                job.getLocation().toLowerCase().contains(keyword.toLowerCase())) {
                matchingJobs.add(job);
            }
        }
        
        if (matchingJobs.isEmpty()) {
            System.out.println("No jobs found matching the keyword: " + keyword);
        } else {
            System.out.println("\nSearch Results:");
            ListInterface.LinkedListIterator resultIterator = matchingJobs.getIterator();
            while (resultIterator.hasNext()) {
                System.out.println(resultIterator.next()+ "/n");
            }
        }
    }
    
    public LinkedList<Job> filterBySalaryRange(double min, double max) {
        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("Salary values cannot be negative");
        }
        if (min > max) {
            throw new IllegalArgumentException("Minimum salary cannot be greater than maximum");
        }
        return jobList.filter(job -> job.getSalary() >= min && job.getSalary() <= max);
    }

    public LinkedList<Job> filterByLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        if (!isValidLocation(location)) {
            throw new IllegalArgumentException("Invalid location");
        }
        return jobList.filter(job -> job.getLocation().equalsIgnoreCase(location));
    }

    public LinkedList<Job> filterByCompany(String company) {
        if (company == null || company.trim().isEmpty()) {
            throw new IllegalArgumentException("Company cannot be empty");
        }
        jobList.sort((j1, j2) -> j1.getCompany().compareToIgnoreCase(j2.getCompany()));
        return jobList.filter(job -> job.getCompany().equalsIgnoreCase(company));
    }

    public LinkedList<Job> filterByJobType(String jobType) {
        if (jobType == null || jobType.trim().isEmpty()) {
            throw new IllegalArgumentException("Job type cannot be empty");
        }
        if (!isValidJobType(jobType)) {
            throw new IllegalArgumentException("Invalid job type");
        }
        return jobList.filter(job -> job.getJobType().equalsIgnoreCase(jobType));
    }

    public Job getJobById(String jobId) {
        if (jobId == null || jobId.trim().isEmpty()) {
            throw new IllegalArgumentException("Job ID cannot be empty");
        }
        return jobList.findFirst(job -> job != null && job.getJobID().equals(jobId));
    }

    public ListInterface<Job> getAllJobs() {
        return jobList;
    }
    
    public ListInterface<Job> getAllJobsSortedBySalaryDesc() {
    jobList.sort((job1, job2) -> Double.compare(job2.getSalary(), job1.getSalary()));
    return jobList;
}

    // Validation helper methods
    private void validateJob(Job job) {
        if (job.getTitle() == null || job.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (job.getCompany() == null || job.getCompany().trim().isEmpty()) {
            throw new IllegalArgumentException("Company cannot be empty");
        }
        if (job.getSalary() < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        if (job.getCurrentCGPA() < 0 || job.getCurrentCGPA() > 4) {
            throw new IllegalArgumentException("CGPA must be between 0 and 4");
        }
        validateTime(job.getInterviewStartTime());
        validateTime(job.getInterviewEndTime());
        if (job.getInterviewStartTime() >= job.getInterviewEndTime()) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        if (!isValidLocation(job.getLocation())) {
            throw new IllegalArgumentException("Invalid location");
        }
        if (!isValidJobType(job.getJobType())) {
            throw new IllegalArgumentException("Invalid job type");
        }
        validateSkills(job.getSkills(), job.getProficiencies());
    }

    private void validateTime(int time) {
        int hours = time / 100;
        int minutes = time % 100;
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Invalid time. Use 24-hour format (e.g., 1330)");
        }
    }

    private boolean isValidLocation(String location) {
        for (String loc : Job.getLocations()) {
            if (loc.equalsIgnoreCase(location)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidJobType(String jobType) {
        for (String type : Job.getJobTypes()) {
            if (type.equalsIgnoreCase(jobType)) {
                return true;
            }
        }
        return false;
    }

    private void validateSkills(String[] skills, int[] proficiencies) {
        if (skills == null || skills.length == 0) {
            throw new IllegalArgumentException("At least one skill is required");
        }
        if (proficiencies == null || proficiencies.length != skills.length) {
            throw new IllegalArgumentException("Skills and proficiencies arrays must match");
        }
        for (String skill : skills) {
            boolean valid = false;
            for (String validSkill : Job.getGeneralSkills()) {
                if (validSkill.equalsIgnoreCase(skill)) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                throw new IllegalArgumentException("Invalid skill: " + skill);
            }
        }
        for (int proficiency : proficiencies) {
            if (proficiency < 1 || proficiency > 5) {
                throw new IllegalArgumentException("Proficiency must be between 1 and 5");
            }
        }
    }

}
