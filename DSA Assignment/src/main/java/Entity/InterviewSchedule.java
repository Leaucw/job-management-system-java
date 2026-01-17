package Entity;

import java.time.LocalDate;
import java.time.LocalTime;
import adt.LinkedList;
import adt.ListInterface;

public class InterviewSchedule {
    
    private Applicant applicant;
    private Job job;
    private double score; //??
    private LocalDate interviewDate;
    private LinkedList<Applicant> applicantList;
    
    public InterviewSchedule(Job job, LocalDate interviewDate) {
        this.job = job;
        this.interviewDate = interviewDate;
        this.applicantList = new LinkedList<>();
    }
    
    public InterviewSchedule(Applicant applicant, Job job,  LocalDate interviewDate) {
        this.applicant = applicant;
        this.job = job;
        
        this.interviewDate = interviewDate;
    }
    /*
    private LinkedList<Applicant> getApplicantList(){
        return applicantList;
    }
    */
    public Applicant getApplicant() {
        return applicant;
    }

    public Job getJob() {
        return job;
    }
    
    public double getScore() {
        return score;
    }

    public LocalDate getInterviewDate() {
        return interviewDate;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setInterviewDate(LocalDate interviewDate) {
        this.interviewDate = interviewDate;
    }
    
    
    // what is toString() want to display ?
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Interview Schedule for Job: ").append(job.getTitle()).append(" at ").append(job.getCompany()).append("\n");
        sb.append("Interview Time: ").append(job.getInterviewStartTime()).append(" - ").append(job.getInterviewEndTime()).append("\n");
        sb.append("Interview Date: ").append(interviewDate).append("\n");
        sb.append("Applicants:\n");

        for (int i = 1; i <= applicantList.getNumberOfEntries(); i++) {
            sb.append(applicantList.getEntry(i)).append("\n");
        }

        return sb.toString();
    }
    
    
    
}

