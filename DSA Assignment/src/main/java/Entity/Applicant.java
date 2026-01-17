package Entity;

import adt.LinkedList;
import adt.ListInterface;

public class Applicant {
    private static int applicantCounter = 1;
    private String applicantId;
    private String name;
    private double cgpa;
    private String location;
    private String jobType; // Added job type field
    private LinkedList<String> skills;
    private LinkedList<Integer> proficiencies;
    private LinkedList<String> appliedJobIds;
    
    private static final String[] LOCATIONS = {
        "Johor", "Kedah", "Kelantan", "Melaka", "Negeri Sembilan", 
        "Pahang", "Perak", "Perlis", "Pulau Pinang", "Sabah", 
        "Sarawak", "Selangor", "Terengganu", "Kuala Lumpur", "Labuan", "Putrajaya"
    };
    
    private static final String[] JOB_TYPES = {
        "Computer Science", "Engineering", "Business", "Finance", "Medicine", 
        "Education", "Law", "Psychology", "Arts", "Communication"
    };

    public Applicant(String name, double cgpa, String location, String jobType, LinkedList<String> skills, LinkedList<Integer> proficiencies) {
        if (skills == null || proficiencies == null || skills.contains(null) || proficiencies.contains(null) || 
            skills.getNumberOfEntries() != proficiencies.getNumberOfEntries()) {
            throw new IllegalArgumentException("Skills and proficiencies must be non-null, contain no null values, and have the same length");
        }
        this.applicantId = String.format("A%03d", applicantCounter++);
        this.name = (name != null && !name.trim().isEmpty()) ? name.trim() : "Unnamed Applicant";
        this.cgpa = Math.min(Math.max(cgpa, 0.0), 4.0);
        this.location = (isValidLocation(location)) ? location : "Unknown";
        this.jobType = (isValidJobType(jobType)) ? jobType : "Unknown";
        this.skills = new LinkedList<>();
        ListInterface.LinkedListIterator skillIter = skills.getIterator();
        while (skillIter.hasNext()) {
            String skill = (String) skillIter.next();
            if (skill != null) {
                this.skills.add(skill);
            }
        }
        this.proficiencies = new LinkedList<>();
        ListInterface.LinkedListIterator profIter = proficiencies.getIterator();
        while (profIter.hasNext()) {
            Integer prof = (Integer) profIter.next();
            if (prof != null) {
                this.proficiencies.add(prof);
            }
        }
        this.appliedJobIds = new LinkedList<>();
    }

    // Getters
    public String getApplicantId() { return applicantId; }
    public String getName() { return name; }
    public double getCgpa() { return cgpa; }
    public String getLocation() { return location; }
    public String getJobType() { return jobType; } // Added getter for job type
    
    public LinkedList<String> getSkills() {
        LinkedList<String> copy = new LinkedList<>();
        if (skills != null) {
            ListInterface.LinkedListIterator iter = skills.getIterator();
            while (iter.hasNext()) {
                String skill = (String) iter.next();
                if (skill != null) {
                    copy.add(skill);
                }
            }
        }
        return copy;
    }

    public LinkedList<Integer> getProficiencies() {
        LinkedList<Integer> copy = new LinkedList<>();
        if (proficiencies != null) {
            ListInterface.LinkedListIterator iter = proficiencies.getIterator();
            while (iter.hasNext()) {
                Integer prof = (Integer) iter.next();
                if (prof != null) {
                    copy.add(prof);
                }
            }
        }
        return copy;
    }

    public LinkedList<String> getAppliedJobIds() {
        LinkedList<String> copy = new LinkedList<>();
        if (appliedJobIds != null) {
            ListInterface.LinkedListIterator iter = appliedJobIds.getIterator();
            while (iter.hasNext()) {
                String jobId = (String) iter.next();
                if (jobId != null) {
                    copy.add(jobId);
                }
            }
        }
        return copy;
    }

    // Setters
    public void setName(String name) { 
        this.name = (name != null && !name.trim().isEmpty()) ? name.trim() : this.name; 
    }
    
    public void setCgpa(double cgpa) { 
        this.cgpa = Math.min(Math.max(cgpa, 0.0), 4.0); 
    }
    
    public void setLocation(String location) {
        this.location = (isValidLocation(location)) ? location : this.location;
    }
    
    public void setJobType(String jobType) {
        this.jobType = (isValidJobType(jobType)) ? jobType : this.jobType;
    }
    
    public void setSkills(LinkedList<String> skills, LinkedList<Integer> proficiencies) {
        if (skills == null || proficiencies == null || skills.contains(null) || proficiencies.contains(null) || 
            skills.getNumberOfEntries() != proficiencies.getNumberOfEntries()) {
            throw new IllegalArgumentException("Skills and proficiencies must be non-null, contain no null values, and have the same length");
        }
        this.skills.clear();
        this.proficiencies.clear();
        ListInterface.LinkedListIterator skillIter = skills.getIterator();
        while (skillIter.hasNext()) {
            String skill = (String) skillIter.next();
            if (skill != null) {
                this.skills.add(skill);
            }
        }
        ListInterface.LinkedListIterator profIter = proficiencies.getIterator();
        while (profIter.hasNext()) {
            Integer prof = (Integer) profIter.next();
            if (prof != null) {
                this.proficiencies.add(prof);
            }
        }
    }

    public void addJobApplication(String jobId) {
        if (jobId != null && !jobId.trim().isEmpty() && !appliedJobIds.contains(jobId)) {
            appliedJobIds.add(jobId);
        }
    }

    public boolean removeJobApplication(String jobId) {
        if (jobId == null || jobId.trim().isEmpty()) return false;
        ListInterface.LinkedListIterator iterator = appliedJobIds.getIterator();
        while (iterator.hasNext()) {
            String id = (String) iterator.next();
            if (id != null && id.equals(jobId)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public boolean hasAppliedForJob(String jobId) {
        return jobId != null && !jobId.trim().isEmpty() && appliedJobIds.contains(jobId);
    }
    
    public static boolean isValidLocation(String location) {
        if (location == null) return false;
        for (String loc : LOCATIONS) {
            if (loc.equalsIgnoreCase(location)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isValidJobType(String jobType) {
        if (jobType == null) return false;
        for (String type : JOB_TYPES) {
            if (type.equalsIgnoreCase(jobType)) {
                return true;
            }
        }
        return false;
    }

    public static String[] getLocations() {
        return LOCATIONS.clone();
    }
    
    public static String[] getJobTypes() {
        return JOB_TYPES.clone();
    }

    @Override
    public String toString() {
        StringBuilder skillsStr = new StringBuilder();
        if (skills != null && proficiencies != null) {
            ListInterface.LinkedListIterator skillIter = skills.getIterator();
            ListInterface.LinkedListIterator profIter = proficiencies.getIterator();
            while (skillIter.hasNext() && profIter.hasNext()) {
                String skill = (String) skillIter.next();
                Integer prof = (Integer) profIter.next();
                if (skill != null && prof != null) {
                    if (skillsStr.length() > 0) skillsStr.append(", ");
                    skillsStr.append(skill).append(":").append(prof);
                }
            }
        }

        StringBuilder applicationsStr = new StringBuilder();
        if (appliedJobIds != null) {
            ListInterface.LinkedListIterator jobIter = appliedJobIds.getIterator();
            while (jobIter.hasNext()) {
                String jobId = (String) jobIter.next();
                if (jobId != null) {
                    if (applicationsStr.length() > 0) applicationsStr.append(", ");
                    applicationsStr.append(jobId);
                }
            }
        }

        return String.format(
            "Applicant ID: %s\nName: %s\nCGPA: %.2f\nLocation: %s\nJob Type: %s\nSkills: %s\nApplied Jobs: %s",
            applicantId, name, cgpa, location, jobType,
            skillsStr.length() > 0 ? skillsStr.toString() : "None",
            applicationsStr.length() > 0 ? applicationsStr.toString() : "None"
        );
    }

}
