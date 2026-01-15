package Utility;

import Boundary.JobUI;
import Boundary.ApplicantUI;
import Boundary.InterviewScheduleUI;
import Boundary.MatchUI; // Import MatchUI
import Control.JobControl;
import Control.ApplicantControl; // Import ApplicantControl
import Control.InterviewControl;
import Control.MatchControl;
import adt.LinkedList;
import java.util.Scanner;
/**
 * Main Menu
 */
public class JobManagementApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JobControl jobControl = new JobControl();
        
        ApplicantControl applicantControl = new ApplicantControl(jobControl);
        MatchUI matchUI = new MatchUI(applicantControl, jobControl);
        
        JobUI jobUI = new JobUI(jobControl);
        // Updated to pass applicantControl
        ApplicantUI applicantUI = new ApplicantUI(applicantControl, jobControl);
        MatchControl matchControl = new MatchControl(applicantControl, jobControl);
        InterviewControl interviewControl = new InterviewControl(applicantControl, jobControl, matchControl);
        InterviewScheduleUI interviewScheduleUI = new InterviewScheduleUI(applicantControl, jobControl, interviewControl);
        
        // Pre-populate with sample data
        SampleDataGenerator.addSampleData(jobControl, applicantControl);
        
        while (true) {
            try {
                System.out.println("\n=== JOB MANAGEMENT APPLICATION ===");
                System.out.println("1. Job Management");
                System.out.println("2. Applicant Management");
                System.out.println("3. Matching Engine");
                System.out.println("4. Arrange Interview Schedule");
                System.out.println("0. Exit");
                System.out.print("Select option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        jobUI.menu();
                        break;
                    case 2:
                        applicantUI.menu();
                        break;
                    case 3:
                        matchUI.menu(); // Call MatchUI menu
                        break;
                    case 4:
                        interviewScheduleUI.menu();
                        break;
                    case 0: // Changed Exit case
                        System.out.println("Thank you for using Job Management Application. Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Please select 1, 2, or 3.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }
    
   

}