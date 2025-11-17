package com.example.interviewmgmt.web;

import com.example.interviewmgmt.model.Question;
import com.example.interviewmgmt.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public List<Question> getAll() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getById(@PathVariable Long id) {
        Question question = questionService.getQuestionById(id);
        return (question != null) ? ResponseEntity.ok(question) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Question create(@RequestBody Question question) {
        return questionService.saveQuestion(question);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
