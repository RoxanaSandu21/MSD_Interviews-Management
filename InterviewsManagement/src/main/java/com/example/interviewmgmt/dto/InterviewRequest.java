package com.example.interviewmgmt.web;

import java.time.LocalDateTime;

public class InterviewRequest {
    private LocalDateTime dateTime;
    private String position;
    private Long candidateId;
    private Long testId;
    private String result;

    // getters and setters
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }

    public Long getTestId() { return testId; }
    public void setTestId(Long testId) { this.testId = testId; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}
