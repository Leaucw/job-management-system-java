package Entity;

/**
 *
 * @author Hao Lun
 */
public class Match {
    private Applicant applicant;
    private Job job;
    private double matchScore; // Represents the strength of the match (e.g., 0.0 to 1.0 or 0 to 100)

    public Match(Applicant applicant, Job job, double matchScore) {
        if (applicant == null || job == null) {
            throw new IllegalArgumentException("Applicant and Job cannot be null for a Match.");
        }
        this.applicant = applicant;
        this.job = job;
        // Ensure score is within a reasonable range, e.g., 0.0 to 1.0
        this.matchScore = Math.max(0.0, Math.min(matchScore, 1.0));
    }

    // Getters
    public Applicant getApplicant() {
        return applicant;
    }

    public Job getJob() {
        return job;
    }

    public double getMatchScore() {
        return matchScore;
    }

    // Optional: Setter for score if recalculation is needed, though matches are often immutable once created.
    // public void setMatchScore(double matchScore) {
    //     this.matchScore = Math.max(0.0, Math.min(matchScore, 1.0));
    // }

    @Override
    public String toString() {
        return String.format("Match Score: %.2f%%\n  Applicant: %s (%s)\n  Job: %s (%s at %s)\n--------------------",
                matchScore * 100, // Display score as percentage
                applicant.getName(),
                applicant.getApplicantId(),
                job.getTitle(),
                job.getJobID(),
                job.getCompany()
        );
    }

    // Allows sorting matches by score (descending)
    public static int compareByScoreDesc(Match m1, Match m2) {
        return Double.compare(m2.getMatchScore(), m1.getMatchScore());
    }
}