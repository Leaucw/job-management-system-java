package Entity;
/**
 *
 * @author Leau
 */
public class Job {
    private static int jobCounter = 1;
    private String jobID;
    private String title;
    private String company;
    private double salary;
    private double currentCGPA;
    private int interviewStartTime;
    private int interviewEndTime;
    private String[] skills;
    private int[] proficiencies;
    private String location;
    private String jobType;
    
    private static final String[] LOCATIONS = {
        "Johor", "Kedah", "Kelantan", "Melaka", "Negeri Sembilan", 
        "Pahang", "Perak", "Perlis", "Pulau Pinang", "Sabah", 
        "Sarawak", "Selangor", "Terengganu", "Kuala Lumpur", "Labuan", "Putrajaya"};
    
    private static final String[] JOB_TYPES = {
        "Computer Science", "Engineering", "Business", "Finance", "Medicine", 
        "Education", "Law", "Psychology", "Arts", "Communication"};
    
    private static final String[] GENERAL_SKILLS = {
        "Communication Skills", "Problem-Solving", "Leadership", "Teamwork", 
        "Time Management", "Adaptability", "Critical Thinking", "Technical Skills", 
        "Creativity", "Work Ethic"};

    public Job(String title, String company, double salary, double currentCGPA, 
              int interviewStartTime, int interviewEndTime, String location, 
              String jobType, String[] skills, int[] proficiencies) {
        this.jobID = String.format("J%03d", jobCounter++);
        this.title = title;
        this.company = company;
        this.salary = salary;
        this.currentCGPA = currentCGPA;
        this.interviewStartTime = interviewStartTime;
        this.interviewEndTime = interviewEndTime;
        this.location = location;
        this.jobType = jobType;
        this.skills = skills;
        this.proficiencies = proficiencies;
    }

    // Getters
    public String getJobID() { return jobID; }
    public String getTitle() { return title; }
    public String getCompany() { return company; }
    public double getSalary() { return salary; }
    public String getLocation() { return location; }
    public String getJobType() { return jobType; }
    public double getCurrentCGPA() { return currentCGPA; }
    public int getInterviewStartTime() { return interviewStartTime; }
    public int getInterviewEndTime() { return interviewEndTime; }
    public String[] getSkills() { return skills; }
    public int[] getProficiencies() { return proficiencies; }
    
    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setCompany(String company) { this.company = company; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setCurrentCGPA(double currentCGPA) { this.currentCGPA = currentCGPA; }
    public void setInterviewTimes(int start, int end) { 
        this.interviewStartTime = start;
        this.interviewEndTime = end;
    }
    public void setLocation(String location) { this.location = location; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public void setSkills(String[] skills, int[] proficiencies) {
        this.skills = skills;
        this.proficiencies = proficiencies;
    }

    public static String[] getLocations() { return LOCATIONS.clone(); }
    public static String[] getJobTypes() { return JOB_TYPES.clone(); }
    public static String[] getGeneralSkills() { return GENERAL_SKILLS.clone(); }

    @Override
    public String toString() {
        StringBuilder skillsStr = new StringBuilder();
        if (skills != null) {
            for (int i = 0; i < skills.length; i++) {
                if (i > 0) skillsStr.append(", ");
                skillsStr.append(skills[i]).append(":").append(proficiencies[i]);
            }
        }
        
        return String.format(
            "Job ID: %s\nTitle: %s\nCompany: %s\nSalary: RM%.2f\nLocation: %s\n" +
            "Job Type: %s\nRequired CGPA: %.2f\nInterview Time: %04d-%04d\n" +
            "Skills Required: %s",
            jobID, title, company, salary, location, jobType, currentCGPA,
            interviewStartTime, interviewEndTime, skillsStr.toString());
    }
}
