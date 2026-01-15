package Boundary;

import Entity.Applicant;
import Entity.Job;
import Control.ApplicantControl;
import Control.JobControl;
import adt.LinkedList;
import adt.ListInterface;
import java.util.Scanner;

/**
 * 
 * @author Wiston
 */
public class ApplicantUI {
    private Scanner scanner = new Scanner(System.in);
    private ApplicantControl applicantControl;
    private JobControl jobControl;

    public ApplicantUI(ApplicantControl applicantControl, JobControl jobControl) {
        this.applicantControl = applicantControl;
        this.jobControl = jobControl;
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== APPLICANT MANAGEMENT SYSTEM ===");
            System.out.println("1. Add New Applicant");
            System.out.println("2. Edit Applicant");
            System.out.println("3. Remove Applicant");
            System.out.println("4. View All Applicants");
            System.out.println("5. Exit");
            System.out.print("Select option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1: addApplicant(); break;
                    case 2: editApplicant(); break;
                    case 3: removeApplicant(); break;
                    case 4: viewAllApplicants(); break;
                    case 5:
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

    private void addApplicant() {
        System.out.println("\n=== ADD NEW APPLICANT ===");
        System.out.print("Enter Applicant Name: ");
        String name = scanner.nextLine();

        double cgpa = getValidDouble("Enter CGPA (0.0-4.0): ", 0, 4);
        
        System.out.println("Available Locations:");
        displayOptions(Applicant.getLocations());
        System.out.print("Enter Location: ");
        String location = scanner.nextLine();
        
        System.out.println("Available Job Types:");
        displayOptions(Applicant.getJobTypes());
        System.out.print("Enter Job Type: ");
        String jobType = scanner.nextLine();
        
        LinkedList<String> skills = selectMultipleFromList("Select Skills:", Job.getGeneralSkills());
        LinkedList<Integer> proficiencies = new LinkedList<>();
        ListInterface.LinkedListIterator skillIter = skills.getIterator();
        while (skillIter.hasNext()) {
            String skill = (String) skillIter.next();
            int proficiency = getValidInt("Enter proficiency (1-5) for " + skill + ": ", 1, 5);
            proficiencies.add(proficiency);
        }

        Applicant applicant = new Applicant(name, cgpa, location, jobType, skills, proficiencies);
        applicantControl.addApplicant(applicant);
    }
    
    private void editApplicant() {
        System.out.println("\n=== EDIT APPLICANT ===");
        System.out.print("Enter Applicant ID: ");
        String applicantId = scanner.nextLine();
        
        Applicant applicant = applicantControl.getApplicantById(applicantId);
        if (applicant == null) {
            System.out.println("Applicant not found.");
            return;
        }
        
        System.out.println("Current Applicant Details:");
        System.out.println(applicant);
        
        System.out.println("\nWhat would you like to edit?");
        System.out.println("1. Name");
        System.out.println("2. CGPA");
        System.out.println("3. Location");
        System.out.println("4. Job Type");
        System.out.println("5. Skills and Proficiencies");
        System.out.println("6. Exit");
        System.out.print("Select option: ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    System.out.print("Enter New Name: ");
                    String name = scanner.nextLine();
                    applicant.setName(name);
                    System.out.println("Name updated successfully.");
                    break;
                case 2:
                    double cgpa = getValidDouble("Enter New CGPA (0.0-4.0): ", 0, 4);
                    applicant.setCgpa(cgpa);
                    System.out.println("CGPA updated successfully.");
                    break;
                case 3:
                    System.out.println("Available Locations:");
                    displayOptions(Applicant.getLocations());
                    System.out.print("Enter New Location: ");
                    String location = scanner.nextLine();
                    applicant.setLocation(location);
                    System.out.println("Location updated successfully.");
                    break;
                case 4:
                    System.out.println("Available Job Types:");
                    displayOptions(Applicant.getJobTypes());
                    System.out.print("Enter New Job Type: ");
                    String jobType = scanner.nextLine();
                    applicant.setJobType(jobType);
                    System.out.println("Job Type updated successfully.");
                    break;
                case 5:
                    LinkedList<String> skills = selectMultipleFromList("Select New Skills:", Job.getGeneralSkills());
                    LinkedList<Integer> proficiencies = new LinkedList<>();
                    ListInterface.LinkedListIterator skillIter = skills.getIterator();
                    while (skillIter.hasNext()) {
                        String skill = (String) skillIter.next();
                        int proficiency = getValidInt("Enter proficiency (1-5) for " + skill + ": ", 1, 5);
                        proficiencies.add(proficiency);
                    }
                    applicant.setSkills(skills, proficiencies);
                    System.out.println("Skills and proficiencies updated successfully.");
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine();
        }
    }

    private void removeApplicant() {
        System.out.println("\n=== REMOVE APPLICANT ===");
        System.out.print("Enter Applicant ID: ");
        String applicantId = scanner.nextLine();
        if (applicantControl.removeApplicant(applicantId)) {
            System.out.println("Applicant removed successfully.");
        } else {
            System.out.println("Applicant not found.");
        }
    }

    private void viewAllApplicants() {
        LinkedList<Applicant> applicants = applicantControl.getAllApplicants();
        if (applicants.isEmpty()) {
            System.out.println("No applicants found.");
            return;
        }

        System.out.println("\n=== APPLICANT LIST ===");
        ListInterface.LinkedListIterator iterator = applicants.getIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
            System.out.println("---------------------");
        }
        System.out.println("Total applicants: " + applicants.getNumberOfEntries());
    }

    private void displayOptions(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s\n", i + 1, options[i]);
        }
    }

    private LinkedList<String> selectMultipleFromList(String prompt, String[] options) {
        LinkedList<String> availableOptions = new LinkedList<>();
        for (String option : options) {
            availableOptions.add(option);
        }

        System.out.println(prompt);
        ListInterface.LinkedListIterator optIter = availableOptions.getIterator();
        int index = 1;
        while (optIter.hasNext()) {
            System.out.printf("%2d. %s\n", index++, optIter.next());
        }

        System.out.print("Enter selections (space-separated numbers): ");
        String input = scanner.nextLine();
        String[] selections = input.trim().split("\\s+");
        LinkedList<String> selected = new LinkedList<>();

        optIter = availableOptions.getIterator();
        index = 1;
        while (optIter.hasNext()) {
            String option = (String) optIter.next();
            for (String sel : selections) {
                try {
                    int choice = Integer.parseInt(sel);
                    if (choice == index && !selected.contains(option)) {
                        selected.add(option);
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid inputs
                }
            }
            index++;
        }
        return selected;
    }

    private int getValidInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                scanner.nextLine();
                if (value >= min && value <= max) return value;
                System.out.printf("Please enter between %d and %d\n", min, max);
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    private double getValidDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = scanner.nextDouble();
                scanner.nextLine();
                if (value >= min && value <= max) return value;
                System.out.printf("Please enter between %.2f and %.2f\n", min, max);
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }
}