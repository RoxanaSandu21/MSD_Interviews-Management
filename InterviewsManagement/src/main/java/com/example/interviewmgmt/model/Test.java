package com.example.interviewmgmt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String area; // e.g. "Java", "Frontend", "Data Science"

    @NotBlank
    private String difficulty; // e.g. "Easy", "Medium", "Hard"

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "test_id")
    private List<Question> questions = new ArrayList<>();

    public Test() {}

    public Test(String area, String difficulty) {
        this.area = area;
        this.difficulty = difficulty;
    }

    public Long getId() { return id; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public void addQuestion(Question question) { this.questions.add(question); }
}
