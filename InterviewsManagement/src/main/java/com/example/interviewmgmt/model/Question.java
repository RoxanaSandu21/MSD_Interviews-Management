package com.example.interviewmgmt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String text;

    @NotBlank
    private String area;

    @NotBlank
    private String difficulty;

    @NotBlank
    private String correctAnswer;

    public Question() {}

    public Question(String text, String area, String difficulty, String correctAnswer) {
        this.text = text;
        this.area = area;
        this.difficulty = difficulty;
        this.correctAnswer = correctAnswer;
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
}
