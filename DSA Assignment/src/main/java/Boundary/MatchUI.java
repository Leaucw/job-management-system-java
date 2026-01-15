package Boundary;

import Control.ApplicantControl;
import Control.JobControl;
import Control.MatchControl;
import Entity.Match;
import adt.LinkedList;
import adt.ListInterface;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 *
 * @author Hao Lun
 */
public class MatchUI {

    private MatchControl matchControl;
    private ApplicantControl applicantControl; // Needed to list applicants for selection
    private JobControl jobControl;         // Needed to list jobs for selection
    private Scanner scanner;

    // Define the minimum score threshold for displaying matches
    private static final double DEFAULT_SCORE_THRESHOLD = 0.3; // 30% match score

    public MatchUI(ApplicantControl applicantControl, JobControl jobControl) {
         if (applicantControl == null || jobControl == null) {
            throw new IllegalArgumentException("ApplicantControl and JobControl cannot be null.");
        }
        this.applicantControl = applicantControl;
        this.jobControl = jobControl;
        // Initialize MatchControl here, passing the required dependencies
        this.matchControl = new MatchControl(applicantControl, jobControl);
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int choice;
        do {
            System.out.println("\n=== MATCHING ENGINE MENU ===");
            System.out.println("1. Find Suitable Jobs for an Applicant (Search By Applicant ID)");
            System.out.println("2. Find Suitable Applicants for a Job (Search By Job ID)");
            System.out.println("3. View All Potential Matches (High Score)");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select option: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        findJobsForApplicantUI();
                        break;
                    case 2:
                        findApplicantsForJobUI();
                        break;
                    case 3:
                        viewAllMatchesUI();
                        break;
                    case 0:
                        System.out.println("Returning to Main Menu...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
                choice = -1; // Set choice to loop again
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace(); // For debugging
                choice = -1; // Loop again or handle as needed
            }
        } while (choice != 0);
    }

    private void findJobsForApplicantUI() {
        System.out.println("\n--- Find Suitable Jobs for Applicant ---");
        System.out.print("Enter Applicant ID to find matches for: ");
        String applicantId = scanner.nextLine().trim();

        if (applicantId.isEmpty()) {
            System.out.println("Applicant ID cannot be empty.");
            return;
        }

        // Optional: Add threshold input
        // System.out.print("Enter minimum match score threshold (0.0 to 1.0, default " + DEFAULT_SCORE_THRESHOLD + "): ");
        // double threshold = readDoubleInput(DEFAULT_SCORE_THRESHOLD);

        System.out.println("Finding matches with score >= " + (DEFAULT_SCORE_THRESHOLD * 100) + "%...");
        LinkedList<Match> matches = matchControl.findMatchesForApplicant(applicantId, DEFAULT_SCORE_THRESHOLD);

        displayMatches(matches, "Jobs found for Applicant " + applicantId);
    }

    private void findApplicantsForJobUI() {
        System.out.println("\n--- Find Suitable Applicants for Job ---");
        System.out.print("Enter Job ID to find candidates for: ");
        String jobId = scanner.nextLine().trim();

         if (jobId.isEmpty()) {
            System.out.println("Job ID cannot be empty.");
            return;
        }

        // Optional: Add threshold input
        // System.out.print("Enter minimum match score threshold (0.0 to 1.0, default " + DEFAULT_SCORE_THRESHOLD + "): ");
        // double threshold = readDoubleInput(DEFAULT_SCORE_THRESHOLD);

        System.out.println("Finding candidates with score >= " + (DEFAULT_SCORE_THRESHOLD * 100) + "%...");
        LinkedList<Match> matches = matchControl.findMatchesForJob(jobId, DEFAULT_SCORE_THRESHOLD);

        displayMatches(matches, "Candidates found for Job " + jobId);
    }

    private void viewAllMatchesUI() {
        System.out.println("\n--- View All Potential Matches ---");
        // Optional: Add threshold input
        // System.out.print("Enter minimum match score threshold (0.0 to 1.0, default " + DEFAULT_SCORE_THRESHOLD + "): ");
        // double threshold = readDoubleInput(DEFAULT_SCORE_THRESHOLD);

        System.out.println("Finding all matches with score >= " + (DEFAULT_SCORE_THRESHOLD * 100) + "%...");
        System.out.println("(This may take a moment depending on the number of applicants and jobs)");

        LinkedList<Match> allMatches = matchControl.findAllMatches(DEFAULT_SCORE_THRESHOLD);
        displayMatches(allMatches, "All Potential Matches Found");
    }


    /**
     * Displays a list of matches to the console.
     * @param matches The list of Match objects.
     * @param title A title for the report.
     */
    private void displayMatches(LinkedList<Match> matches, String title) {
        System.out.println("\n--- " + title + " ---");
        if (matches == null || matches.isEmpty()) {
            System.out.println("No suitable matches found meeting the criteria.");
        } else {
            System.out.println("Found " + matches.getNumberOfEntries() + " match(es):");
            ListInterface.LinkedListIterator iter = matches.getIterator();
            while (iter.hasNext()) {
                Match match = (Match) iter.next();
                System.out.println(match.toString()); // Use the Match's toString method
            }
        }
        System.out.println("--- End of Report ---");
    }

     // Helper function to read double input with default value
    private double readDoubleInput(double defaultValue) {
        try {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                return defaultValue;
            }
            double value = Double.parseDouble(line);
            return Math.max(0.0, Math.min(value, 1.0)); // Clamp between 0.0 and 1.0
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Using default value: " + defaultValue);
            return defaultValue;
        }
    }

}