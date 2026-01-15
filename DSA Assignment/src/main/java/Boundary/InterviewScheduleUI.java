package Boundary;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import Control.ApplicantControl;
import Control.JobControl;
import Control.MatchControl;
import Control.InterviewControl;
import Entity.Applicant;
import Entity.Match;
import Entity.InterviewSchedule;
import Entity.Job;

import adt.LinkedList;
import adt.ListInterface;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Yi Liang
 */
//the schedule must include timedate, applicants information, 
public class InterviewScheduleUI {

    private Scanner scanner = new Scanner(System.in);
    private ApplicantControl applicantControl;
    private JobControl jobControl;
    private InterviewControl interviewControl;
    //private MatchControl matchControl;

    public InterviewScheduleUI(ApplicantControl applicantControl, JobControl jobControl,
            InterviewControl interviewControl/*, MatchControl matchControl*/) {
        this.applicantControl = applicantControl;
        this.jobControl = jobControl;
        this.interviewControl = interviewControl;
        // this.matchControl = matchControl;
    }

    public void menu() {
        //check qualify applicant
        //check schedule(certain ,all company)
        //add schedule(applicant and company)
        //

        while (true) {
            System.out.println("\n=== EMPLOYER JOB MANAGEMENT SYSTEM ===");
            System.out.println("1. Check Applicant Qualifications");
            System.out.println("2. Auto Apply Applicant");
            System.out.println("3. Filter Applicant Schedule");
            System.out.println("4. Filter Company Schedule");
            System.out.println("5. View All Company Schedule");
            System.out.println("6. Exit");
            System.out.print("Select option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        checkApplicantQualify();
                        break;  
                    case 2:
                        applyApplicant();
                        break; //manually added??? OR automatically added???
                    case 3:
                        filterApplicantSchedule();
                        break; //check by applicant ID(or name)
                    case 4:
                        filterCompanySchedule();
                        break;
                    case 5:
                        viewAllCompanySchedule();
                        break; 
                    case 6:
                        System.out.println("Exiting system...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                //e.printStackTrace();
                scanner.nextLine();
            }
        }

    }

    // Method to check applicant qualifications
    public void checkApplicantQualify() {
        System.out.println("\n=== CHECK APPLICANT QUALIFICATIONS ===");
        System.out.print("Enter Job ID or Job Type: ");
        String input = scanner.nextLine();

        // Get matched jobs based on user input
        LinkedList<Job> matchedJobs = interviewControl.getMatchedJobs(input);
        //LinkedList<Applicant> allApplicants = applicantControl.getAllApplicants();
        //LinkedList<Job> allJobs = (LinkedList<Job>) jobControl.getAllJobs();
        if (matchedJobs.isEmpty()) {
            System.out.println("No job matched your input.");
            return;
        }

        // Loop through matched jobs using index-based loop
        for (int i = 1; i <= matchedJobs.getNumberOfEntries(); i++) {
            Job job = matchedJobs.getEntry(i);
            System.out.println("\nJob: " + job.getJobID() + " - " + job.getTitle() + " (" + job.getCompany() + ")");

            // Check applicants for the job and calculate match scores
            LinkedList<Match> matches = interviewControl.checkApplicantsForJob(job);

            // Sort the matches by score in descending order
            interviewControl.sortMatchesByScore(matches);

            // Display the matches and their scores
            interviewControl.displayMatches(matches);
        }
    }

    // Check if the number of applicants for this job is less than or equal to 7 for applyApplicant()
    public void applyApplicant() {
        System.out.println("\n=== AUTO APPLY APPLICANT TO MATCHING JOBS ===");

        LinkedList<Applicant> applicantList = applicantControl.getAllApplicants();
        LinkedList<Job> allJobs = (LinkedList<Job>) jobControl.getAllJobs();

        if (applicantList.getNumberOfEntries() == 0) {
            System.out.println("\nThere are no applicants.");
            return;
        }

        // Loop through each applicant
        for (int i = 1; i <= applicantList.getNumberOfEntries(); i++) {
            Applicant applicant = applicantList.getEntry(i);
            System.out.println("\nApplicant: " + applicant.getName() + " (" + applicant.getApplicantId() + ")");

            // Get the list of matching jobs for this applicant
            LinkedList<Job> matchingJobs = interviewControl.getMatchingJobsForApplicant(applicant, allJobs);

            boolean matched = false;

            for (int j = 1; j <= matchingJobs.getNumberOfEntries(); j++) {
                Job job = matchingJobs.getEntry(j);

                // Get the top 7 applicants for this job based on score --LIMIT TO 7 person
                LinkedList<Applicant> topApplicants = interviewControl.getTopApplicantsForJob(job, 7);

                // Check if current applicant is in the top 7
                boolean isInTop = false;
                for (int k = 1; k <= topApplicants.getNumberOfEntries(); k++) {
                    if (topApplicants.getEntry(k).getApplicantId().equals(applicant.getApplicantId())) {
                        isInTop = true;
                        break;
                    }
                }

                if (isInTop) {
                    // Check if the applicant has already applied
                    if (!interviewControl.hasApplicantAlreadyApplied(applicant, job)) {
                        interviewControl.applyForJob(applicant, job);
                        System.out.println("  ➤ Applied to: " + job.getTitle() + " at " + job.getCompany() + " [Job ID: " + job.getJobID() + "]");

                        // Create a new interview schedule
                        InterviewSchedule schedule = new InterviewSchedule(applicant, job, LocalDate.now());
                        interviewControl.addInterviewSchedule(schedule);

                        matched = true;
                    }
                } else {
                    System.out.println("  ➤ Not in top 7 for: " + job.getTitle() + " at " + job.getCompany());
                }
            }

            if (!matched) {
                System.out.println("  No matching jobs found or applicant not qualified.");
            }
        }
    }

    public void filterApplicantSchedule() {
        System.out.println("\n=== FILTER SCHEDULE OF THE APPLICANT ===");

        //InterviewControl interviewControl = new InterviewControl(); // Shared or injected
        LinkedList<Applicant> uniqueApplicants = interviewControl.getApplicantsSchedule();
        //LinkedList<Applicant> allApplicants = applicantControl.getAllApplicants();
        //LinkedList<Job> allJobs = (LinkedList<Job>) jobControl.getAllJobs();
        // Step 1: Display all available applicant IDs
        System.out.println("Available Applicant IDs:");
        for (int i = 0; i < uniqueApplicants.getNumberOfEntries(); i++) {
            Applicant a = uniqueApplicants.getEntry(i + 1);
            System.out.println(" - " + a.getApplicantId() + ": " + a.getName());
        }

        // Step 2: Prompt for ID
        System.out.print("\nEnter Applicant ID to filter: ");
        String input = scanner.nextLine();
        String inputId = input.trim();

        // Step 3: Find matching applicant
        Applicant selectedApplicant = interviewControl.findApplicantById(inputId);
        if (selectedApplicant == null) {
            System.out.println("Applicant ID not found.");
            return;
        }

        // Step 4: Retrieve interview schedules and job types
        LinkedList<InterviewSchedule> schedules = interviewControl.uniqueApplicantSchedules(selectedApplicant);
        LinkedList<String> jobTypes = interviewControl.getJobForApplicantSchedules(schedules);

// Check if schedules list is empty
        if (schedules == null || schedules.isEmpty()) {
            System.out.println("No interview schedules found for this applicant.");
            return;
        }

// Check if jobTypes list is empty
        if (jobTypes == null || jobTypes.isEmpty()) {
            System.out.println("No job types found for the applicant's schedules.");
            return;
        }

        // Step 5: Filter based on job type with multiple companies and job IDs
        for (int j = 0; j < jobTypes.getNumberOfEntries(); j++) {
            String jobType = jobTypes.getEntry(j + 1);
            LinkedList<String> companyList = new LinkedList<>();
            LinkedList<String> jobIdList = new LinkedList<>();

            interviewControl.getCompaniesInformation(jobType, schedules, companyList, jobIdList);

            if (!companyList.isEmpty() && !jobIdList.isEmpty()) {
                interviewControl.displayApplicantJobMatches(selectedApplicant, jobType, schedules);
            }
        }
    }

    public void filterCompanySchedule() {

        System.out.println("\n=== FILTER SCHEDULE BY COMPANY ===");

        // Step 1: Display all companies
        LinkedList<String> companyNames = interviewControl.getCompanyNames();
        System.out.println("Available Companies:");
        for (int i = 0; i < companyNames.getNumberOfEntries(); i++) {
            System.out.println(" - " + companyNames.getEntry(i + 1));
        }

        // Step 2: Prompt for company
        System.out.print("\nEnter Company Name to filter: ");
        String companyName = scanner.nextLine().trim();
        
        // Step 3: Display jobs and applicants
        interviewControl.displayCompanyJobSchedules(companyName);
    }

    public void viewAllCompanySchedule() {
        System.out.println("\n=== VIEW ALL COMPANY SCHEDULES ===");

        LinkedList<String> companyNames = interviewControl.getCompanyNames();

        if (companyNames == null || companyNames.getNumberOfEntries() == 0) {
            System.out.println("No company schedules found.");
            return;
        }
        ListInterface.LinkedListIterator<String> iterator = companyNames.getIterator();
        while (iterator.hasNext()) {
            String company = iterator.next();
            int interviewStartTime = interviewControl.getInterviewStartTime(company);
            int interviewEndTime = interviewControl.getInterviewEndTime(company);
            LocalDate interviewDate = interviewControl.getInterviewDate(company); // You need to implement this if not yet

            System.out.println("\n\nCompany: " + company);
            System.out.println("Interview Date: " + interviewDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Interview Time: " + String.format("%04d", interviewStartTime) + " - "
                    + String.format("%04d", interviewEndTime) + "\n");
            System.out.println("Applicants:");
            interviewControl.printApplicantsForCompany(company);
        }

    }

}
