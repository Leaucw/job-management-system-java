package Boundary;

import Entity.Job;
import Control.JobControl;
import adt.LinkedList;
import adt.ListInterface;
import java.util.Scanner;

public class JobUI {
    private Scanner scanner = new Scanner(System.in);
    private JobControl jobControl;

    public JobUI(JobControl jobControl) {
        this.jobControl = jobControl;
    }

   public void menu() {
    while (true) {
        System.out.println("\n=== EMPLOYER JOB MANAGEMENT SYSTEM ===");
        System.out.println("1. Create Job Posting");
        System.out.println("2. Update Job Posting");
        System.out.println("3. Remove Job Posting");
        System.out.println("4. Filter Jobs");
        System.out.println("5. Search Jobs");
        System.out.println("6. View Report of Jobs");
        System.out.println("7. Exit");
        System.out.print("Select option: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: createJobPosting(); break;
                case 2: updateJobPosting(); break;
                case 3: removeJobPosting(); break;
                case 4: filterJobsMenu(); break;
                case 5: searchJobsMenu(); break;
                case 6: viewAllJobs(); break;
                case 7: 
                    System.out.println("Exiting system...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine();
        }
    }
}
private void createJobPosting() {
    System.out.println("\n=== CREATE NEW JOB POSTING ===");

    System.out.print("Enter Job Title: ");
    String title = scanner.nextLine();

    System.out.print("Enter Company Name: ");
    String company = scanner.nextLine();

    double salary = 0;
    while (true) {
        System.out.print("Enter Salary (RM): ");
        try {
            salary = Double.parseDouble(scanner.nextLine());
            if (salary < 0) throw new NumberFormatException();
            break;
        } catch (NumberFormatException e) {
            System.out.println("Invalid salary. Please enter a positive number.");
        }
    }

    double cgpa = 0;
    while (true) {
        System.out.print("Enter Minimum CGPA (0.0-4.0): ");
        try {
            cgpa = Double.parseDouble(scanner.nextLine());
            if (cgpa < 0.0 || cgpa > 4.0) throw new NumberFormatException();
            break;
        } catch (NumberFormatException e) {
            System.out.println("Invalid CGPA. Please enter a value between 0.0 and 4.0.");
        }
    }

    int startTime = 0, endTime = 0;
    while (true) {
        try {
            System.out.print("Enter Interview Start Time (e.g., 1330): ");
            startTime = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Interview End Time (e.g., 1500): ");
            endTime = Integer.parseInt(scanner.nextLine());
            if (startTime >= endTime) throw new Exception();
            break;
        } catch (Exception e) {
            System.out.println("Invalid time. End time must be after start time and in correct format.");
        }
    }

    String location = "";
    while (true) {
        System.out.println("Select Location:");
        for (int i = 0; i < Job.getLocations().length; i++) {
            System.out.printf("%d. %s\n", i + 1, Job.getLocations()[i]);
        }
        System.out.print("Enter choice: ");
        try {
            int locChoice = Integer.parseInt(scanner.nextLine());
            location = Job.getLocations()[locChoice - 1];
            break;
        } catch (Exception e) {
            System.out.println("Invalid choice. Please select a valid number.");
        }
    }

    String jobType = "";
    while (true) {
        System.out.println("Select Job Type:");
        for (int i = 0; i < Job.getJobTypes().length; i++) {
            System.out.printf("%d. %s\n", i + 1, Job.getJobTypes()[i]);
        }
        System.out.print("Enter choice: ");
        try {
            int typeChoice = Integer.parseInt(scanner.nextLine());
            jobType = Job.getJobTypes()[typeChoice - 1];
            break;
        } catch (Exception e) {
            System.out.println("Invalid choice. Please select a valid number.");
        }
    }

    System.out.println("\n=== SKILL SELECTION ===");
    String[] selectedSkills;
    int[] proficiencies;

    while (true) {
        try {
            System.out.println("Select Skills (enter numbers separated by spaces):");
            for (int i = 0; i < Job.getGeneralSkills().length; i++) {
                System.out.printf("%d. %s\n", i + 1, Job.getGeneralSkills()[i]);
            }

            System.out.print("Enter choices: ");
            String[] skillChoices = scanner.nextLine().trim().split("\\s+");

            selectedSkills = new String[skillChoices.length];
            proficiencies = new int[skillChoices.length];

            for (int i = 0; i < skillChoices.length; i++) {
                int skillIndex = Integer.parseInt(skillChoices[i]) - 1;
                selectedSkills[i] = Job.getGeneralSkills()[skillIndex];

                while (true) {
                    try {
                        System.out.print("Enter proficiency (1-5) for " + selectedSkills[i] + ": ");
                        int prof = Integer.parseInt(scanner.nextLine());
                        if (prof < 1 || prof > 5) throw new NumberFormatException();
                        proficiencies[i] = prof;
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid proficiency. Enter a number between 1 and 5.");
                    }
                }
            }
            break;
        } catch (Exception e) {
            System.out.println("Invalid skill input. Please re-enter all selections carefully.");
        }
    }

    try {
        Job newJob = new Job(title, company, salary, cgpa, startTime, endTime,
                location, jobType, selectedSkills, proficiencies);
        jobControl.addJob(newJob);
        System.out.println("Job added successfully: " + newJob.getJobID());
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}

    private void updateJobPosting() {
        System.out.println("\n=== UPDATE JOB POSTING ===");
        System.out.print("Enter Job ID to update: ");
        String jobID = scanner.nextLine();
        
        try {
            Job existingJob = jobControl.getJobById(jobID);
            if (existingJob == null) {
                System.out.println("Job not found.");
                return;
            }
            
            System.out.println("\nCurrent Job Details:");
            System.out.println(existingJob);
            
            String newTitle = existingJob.getTitle();
            String newCompany = existingJob.getCompany();
            double newSalary = existingJob.getSalary();
            double newCGPA = existingJob.getCurrentCGPA();
            int newStartTime = existingJob.getInterviewStartTime();
            int newEndTime = existingJob.getInterviewEndTime();
            String newLocation = existingJob.getLocation();
            String newJobType = existingJob.getJobType();
            String[] newSkills = existingJob.getSkills();
            int[] newProficiencies = existingJob.getProficiencies();
            
            while (true) {
                System.out.println("\nSelect field to update:");
                System.out.println("1. Title");
                System.out.println("2. Company");
                System.out.println("3. Salary");
                System.out.println("4. Required CGPA");
                System.out.println("5. Interview Times");
                System.out.println("6. Location");
                System.out.println("7. Job Type");
                System.out.println("8. Skills");
                System.out.println("9. Finish Updating");
                System.out.print("Enter choice (1-9): ");
                
                int choice = Integer.parseInt(scanner.nextLine());
                
                if (choice == 9) break;
                
                switch (choice) {
                    case 1:
                        System.out.print("Enter new Title [" + newTitle + "]: ");
                        newTitle = scanner.nextLine();
                        break;
                    case 2:
                        System.out.print("Enter new Company [" + newCompany + "]: ");
                        newCompany = scanner.nextLine();
                        break;
                    case 3:
                        System.out.print("Enter new Salary [" + newSalary + "]: ");
                        newSalary = Double.parseDouble(scanner.nextLine());
                        break;
                    case 4:
                        System.out.print("Enter new CGPA [" + newCGPA + "]: ");
                        newCGPA = Double.parseDouble(scanner.nextLine());
                        break;
                    case 5:
                        System.out.print("Enter new Start Time [" + newStartTime + "]: ");
                        newStartTime = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter new End Time [" + newEndTime + "]: ");
                        newEndTime = Integer.parseInt(scanner.nextLine());
                        break;
                    case 6:
                        System.out.println("Select new Location:");
                        for (int i = 0; i < Job.getLocations().length; i++) {
                            System.out.printf("%d. %s\n", i+1, Job.getLocations()[i]);
                        }
                        System.out.print("Enter choice: ");
                        newLocation = Job.getLocations()[Integer.parseInt(scanner.nextLine())-1];
                        break;
                    case 7:
                        System.out.println("Select new Job Type:");
                        for (int i = 0; i < Job.getJobTypes().length; i++) {
                            System.out.printf("%d. %s\n", i+1, Job.getJobTypes()[i]);
                        }
                        System.out.print("Enter choice: ");
                        newJobType = Job.getJobTypes()[Integer.parseInt(scanner.nextLine())-1];
                        break;
                    case 8:
                        System.out.println("Select Skills (enter numbers separated by spaces):");
                        for (int i = 0; i < Job.getGeneralSkills().length; i++) {
                            System.out.printf("%d. %s\n", i+1, Job.getGeneralSkills()[i]);
                        }
                        System.out.print("Enter choices: ");
                        String[] skillChoices = scanner.nextLine().split(" ");
                        newSkills = new String[skillChoices.length];
                        newProficiencies = new int[skillChoices.length];
                        
                        for (int i = 0; i < skillChoices.length; i++) {
                            int index = Integer.parseInt(skillChoices[i])-1;
                            newSkills[i] = Job.getGeneralSkills()[index];
                            System.out.print("Enter proficiency (1-5) for " + newSkills[i] + ": ");
                            newProficiencies[i] = Integer.parseInt(scanner.nextLine());
                        }
                        break;
                }
            }
            
            boolean updated = jobControl.updateJob(
                jobID, newTitle, newCompany, newSalary, newCGPA, 
                newStartTime, newEndTime, newLocation, newJobType, 
                newSkills, newProficiencies
            );
            
            if (updated) {
                System.out.println("\nJob updated successfully!");
            } else {
                System.out.println("\nFailed to update job.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeJobPosting() {
        System.out.println("\n=== REMOVE JOB POSTING ===");
        System.out.print("Enter Job ID to remove: ");
        String jobID = scanner.nextLine();
        
        System.out.print("Are you sure? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            try {
                if (jobControl.removeJob(jobID)) {
                    System.out.println("Job removed successfully.");
                } else {
                    System.out.println("Job not found.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Job removal cancelled.");
        }
    }
    
    public void searchJobsMenu() {
    System.out.print("\nEnter a keyword to search for jobs: ");
    String keyword = scanner.nextLine().trim();
    
    if (keyword.isEmpty()) {
        System.out.println("Please enter a valid keyword.");
    } else {
        jobControl.searchJobs(keyword);
    }
}

    private void filterJobsMenu() {
        System.out.println("\n=== FILTER JOBS ===");
        System.out.println("1. By Salary Range");
        System.out.println("2. By Location");
        System.out.println("3. By Company");
        System.out.println("4. By Job Type");
        System.out.println("5. Back to Main Menu");
        System.out.print("Select option: ");
        
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 5) return;
        
        try {
            LinkedList<Job> results;
            switch (choice) {
                case 1:
                    System.out.print("Enter minimum salary: ");
                    double min = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter maximum salary: ");
                    double max = Double.parseDouble(scanner.nextLine());
                    results = jobControl.filterBySalaryRange(min, max);
                    break;
                case 2:
                    System.out.println("Select location:");
                    for (int i = 0; i < Job.getLocations().length; i++) {
                        System.out.printf("%d. %s\n", i+1, Job.getLocations()[i]);
                    }
                    System.out.print("Enter choice: ");
                    String location = Job.getLocations()[Integer.parseInt(scanner.nextLine())-1];
                    results = jobControl.filterByLocation(location);
                    break;
                case 3:
                    System.out.print("Enter company name: ");
                    String company = scanner.nextLine();
                    results = jobControl.filterByCompany(company);
                    break;
                case 4:
                    System.out.println("Select job type:");
                    for (int i = 0; i < Job.getJobTypes().length; i++) {
                        System.out.printf("%d. %s\n", i+1, Job.getJobTypes()[i]);
                    }
                    System.out.print("Enter choice: ");
                    String jobType = Job.getJobTypes()[Integer.parseInt(scanner.nextLine())-1];
                    results = jobControl.filterByJobType(jobType);
                    break;
                default:
                    return;
            }
            
            displayJobs(results);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllJobs() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Do you want to display jobs sorted by salary (highest to lowest)? (Y/N): ");
    String choice = scanner.nextLine().trim().toUpperCase();

    ListInterface<Job> jobsToDisplay;

    if (choice.equals("Y")) {
        jobsToDisplay = jobControl.getAllJobsSortedBySalaryDesc();
        System.out.println("\n=== Job Postings (Sorted by Salary: Highest to Lowest) ===");
    } else {
        jobsToDisplay = jobControl.getAllJobs();
        System.out.println("\n=== All Job Postings ===");
    }

    if (jobsToDisplay.isEmpty()) {
        System.out.println("No jobs available.");
        return;
    }

    ListInterface.LinkedListIterator iterator = jobsToDisplay.getIterator();
    int count = 1;
    while (iterator.hasNext()) {
        System.out.println("Job #" + count++);
        System.out.println(iterator.next());
        System.out.println("----------------------------------------------------------------------------------------------------");
    }
    }

    private void displayJobs(LinkedList<Job> jobs) {
        if (jobs.isEmpty()) {
            System.out.println("No jobs found.");
            return;
        }
        
        System.out.println("\n=== JOB LISTINGS ===");
        ListInterface.LinkedListIterator iterator = jobs.getIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
            System.out.println("---------------------");
        }
        System.out.println("Total jobs: " + jobs.getNumberOfEntries());
    }
}