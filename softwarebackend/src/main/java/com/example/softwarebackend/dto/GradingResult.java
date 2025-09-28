package com.example.softwarebackend.dto;

public class GradingResult {
    private String submissionId;
    private double score;

    public GradingResult(String submissionId, double score) {
        this.submissionId = submissionId;
        this.score = score;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
