package com.example.interviewmgmt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime dateTime;

    @NotBlank
    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    @JsonBackReference
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    private String result; // optional outcome or feedback

    public Interview() {}

    public Interview(LocalDateTime dateTime, String position) {
        this.dateTime = dateTime;
        this.position = position;
    }

    public Long getId() { return id; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }
    public Test getTest() { return test; }
    public void setTest(Test test) { this.test = test; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}
