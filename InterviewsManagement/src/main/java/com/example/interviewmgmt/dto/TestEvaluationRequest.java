package com.example.interviewmgmt.dto;

import java.util.List;

public class TestEvaluationRequest {

    private List<Boolean> answersCorrect;

    public List<Boolean> getAnswersCorrect() {
        return answersCorrect;
    }

    public void setAnswersCorrect(List<Boolean> answersCorrect) {
        this.answersCorrect = answersCorrect;
    }
}
