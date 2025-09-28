package com.example.softwarebackend.dto;

import java.util.List;





public class CodeSubmission {
    private String studentId;
    private SubmissionType submissionType;
    private String language;
    private String code;
    private List<TestCase> testCases;


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public SubmissionType getSubmissionType() {
        return submissionType;
    }

    public void setSubmissionType(SubmissionType submissionType) {
        this.submissionType = submissionType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    @Override
    public String toString() {
        return "CodeSubmission{" +
                "studentId='" + studentId + '\'' +
                ", submissionType=" + submissionType +
                ", language='" + language + '\'' +
                ", code='" + (code != null ? code.substring(0, Math.min(code.length(), 50)) + "..." : null) + '\'' +
                ", testCases=" + testCases +
                '}';
    }

}
