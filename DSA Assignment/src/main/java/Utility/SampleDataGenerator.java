/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utility;

import Control.ApplicantControl;
import Control.JobControl;
import Entity.Applicant;
import Entity.Job;
import adt.LinkedList;

/**
 *
 * @author User
 */
public class SampleDataGenerator {
    
    
    public static void addSampleData(JobControl jobControl, ApplicantControl applicantControl) {
        System.out.println("Adding sample data...");

        // === Sample Jobs ===
        jobControl.addJob(new Job("Software Developer", "Tech Solutions", 5000, 3.0, 900, 1700, "Kuala Lumpur", "Computer Science",
                new String[]{"Technical Skills", "Problem-Solving", "Teamwork"}, new int[]{4, 4, 3}));

        jobControl.addJob(new Job("QA Engineer", "Innovate Inc.", 4500, 2.8, 1000, 1600, "Selangor", "Engineering",
                new String[]{"Adaptability", "Critical Thinking", "Communication Skills"}, new int[]{3, 4, 5}));

        jobControl.addJob(new Job("Project Manager", "BuildIt Corp", 7000, 3.5, 800, 1800, "Pulau Pinang", "Business",
                new String[]{"Leadership", "Time Management", "Communication Skills"}, new int[]{5, 4, 5}));

        jobControl.addJob(new Job("Graphic Designer", "DesignHub", 4000, 2.7, 930, 1600, "Sarawak", "Arts",
                new String[]{"Creativity", "Work Ethic", "Communication Skills"}, new int[]{4, 4, 3}));

        jobControl.addJob(new Job("Marketing Executive", "BrandCo", 4800, 3.2, 1000, 1700, "Terengganu", "Communication",
                new String[]{"Creativity", "Teamwork", "Adaptability"}, new int[]{3, 4, 4}));

        jobControl.addJob(new Job("Financial Analyst", "FinWise", 6000, 3.6, 900, 1630, "Perak", "Finance",
                new String[]{"Critical Thinking", "Time Management", "Problem-Solving"}, new int[]{4, 5, 4}));

        jobControl.addJob(new Job("Teacher", "Smart School", 3500, 2.9, 800, 1500, "Pahang", "Education",
                new String[]{"Communication Skills", "Leadership", "Adaptability"}, new int[]{4, 4, 3}));

        jobControl.addJob(new Job("Legal Advisor", "LawBridge", 7500, 3.9, 1100, 1800, "Putrajaya", "Law",
                new String[]{"Critical Thinking", "Work Ethic", "Communication Skills"}, new int[]{5, 4, 5}));

        jobControl.addJob(new Job("Psychologist", "MindCare", 6800, 3.7, 900, 1600, "Sabah", "Psychology",
                new String[]{"Adaptability", "Leadership", "Critical Thinking"}, new int[]{4, 3, 5}));

        jobControl.addJob(new Job("System Analyst", "ITPro", 5500, 3.3, 830, 1500, "Selangor", "Computer Science",
                new String[]{"Technical Skills", "Problem-Solving", "Work Ethic"}, new int[]{4, 4, 3}));

        // === Sample Applicants ===
        // Applicant 1
        LinkedList<String> skillsApp1 = new LinkedList<>();
        LinkedList<Integer> profApp1 = new LinkedList<>();
        skillsApp1.add("Technical Skills");
        profApp1.add(5);
        skillsApp1.add("Problem-Solving");
        profApp1.add(4);
        skillsApp1.add("Teamwork");
        profApp1.add(3);
        applicantControl.addApplicant(new Entity.Applicant("Alice Tan", 3.8, "Johor", "Computer Science", skillsApp1, profApp1));

// Applicant 2
        LinkedList<String> skillsApp2 = new LinkedList<>();
        LinkedList<Integer> profApp2 = new LinkedList<>();
        skillsApp2.add("Communication Skills");
        profApp2.add(4);
        skillsApp2.add("Critical Thinking");
        profApp2.add(3);
        skillsApp2.add("Adaptability");
        profApp2.add(4);
        applicantControl.addApplicant(new Entity.Applicant("Bob Lee", 3.1, "Selangor", "Engineering", skillsApp2, profApp2));

// Applicant 3
        LinkedList<String> skillsApp3 = new LinkedList<>();
        LinkedList<Integer> profApp3 = new LinkedList<>();
        skillsApp3.add("Leadership");
        profApp3.add(4);
        skillsApp3.add("Time Management");
        profApp3.add(5);
        skillsApp3.add("Communication Skills");
        profApp3.add(5);
        applicantControl.addApplicant(new Entity.Applicant("Charlie Wong", 3.6, "Kuala Lumpur", "Business", skillsApp3, profApp3));

// Applicant 4
        LinkedList<String> skillsApp4 = new LinkedList<>();
        LinkedList<Integer> profApp4 = new LinkedList<>();
        skillsApp4.add("Java");
        profApp4.add(2);
        skillsApp4.add("Problem-Solving");
        profApp4.add(3);
        applicantControl.addApplicant(new Entity.Applicant("Diana Lim", 2.7, "Kuala Lumpur", "Computer Science", skillsApp4, profApp4));

// Applicant 5
        LinkedList<String> skillsApp5 = new LinkedList<>();
        LinkedList<Integer> profApp5 = new LinkedList<>();
        skillsApp5.add("Time Management");
        profApp5.add(5);
        skillsApp5.add("Creativity");
        profApp5.add(4);
        skillsApp5.add("Work Ethic");
        profApp5.add(3);
        applicantControl.addApplicant(new Entity.Applicant("Edward Tan", 3.4, "Penang", "Finance", skillsApp5, profApp5));

// Applicant 6
        LinkedList<String> skillsApp6 = new LinkedList<>();
        LinkedList<Integer> profApp6 = new LinkedList<>();
        skillsApp6.add("Adaptability");
        profApp6.add(3);
        skillsApp6.add("Leadership");
        profApp6.add(4);
        skillsApp6.add("Work Ethic");
        profApp6.add(4);
        applicantControl.addApplicant(new Entity.Applicant("Fiona Chua", 3.2, "Pahang", "Education", skillsApp6, profApp6));

// Applicant 7
        LinkedList<String> skillsApp7 = new LinkedList<>();
        LinkedList<Integer> profApp7 = new LinkedList<>();
        skillsApp7.add("Critical Thinking");
        profApp7.add(5);
        skillsApp7.add("Work Ethic");
        profApp7.add(4);
        skillsApp7.add("Problem-Solving");
        profApp7.add(4);
        applicantControl.addApplicant(new Entity.Applicant("George Ng", 3.9, "Putrajaya", "Law", skillsApp7, profApp7));

// Applicant 8
        LinkedList<String> skillsApp8 = new LinkedList<>();
        LinkedList<Integer> profApp8 = new LinkedList<>();
        skillsApp8.add("Adaptability");
        profApp8.add(3);
        skillsApp8.add("Creativity");
        profApp8.add(4);
        skillsApp8.add("Communication Skills");
        profApp8.add(5);
        applicantControl.addApplicant(new Entity.Applicant("Hannah Yap", 3.7, "Sabah", "Psychology", skillsApp8, profApp8));

// Applicant 9
        LinkedList<String> skillsApp9 = new LinkedList<>();
        LinkedList<Integer> profApp9 = new LinkedList<>();
        skillsApp9.add("Technical Skills");
        profApp9.add(4);
        skillsApp9.add("Teamwork");
        profApp9.add(3);
        skillsApp9.add("Leadership");
        profApp9.add(3);
        applicantControl.addApplicant(new Entity.Applicant("Ivan Lee", 3.0, "Melaka", "Computer Science", skillsApp9, profApp9));

// Applicant 10
        LinkedList<String> skillsApp10 = new LinkedList<>();
        LinkedList<Integer> profApp10 = new LinkedList<>();
        skillsApp10.add("Communication Skills");
        profApp10.add(5);
        skillsApp10.add("Creativity");
        profApp10.add(4);
        skillsApp10.add("Time Management");
        profApp10.add(4);
        applicantControl.addApplicant(new Entity.Applicant("Jasmine Ho", 3.5, "Negeri Sembilan", "Communication", skillsApp10, profApp10));

        
        System.out.println("Sample data added.");
    }

}
