package com.example.interviewmgmt.web;

import com.example.interviewmgmt.model.Interviewer;
import com.example.interviewmgmt.service.InterviewerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviewers")
public class InterviewerController {

    private final InterviewerService interviewerService;

    public InterviewerController(InterviewerService interviewerService) {
        this.interviewerService = interviewerService;
    }

    @GetMapping
    public List<Interviewer> getAll() {
        return interviewerService.getAllInterviewers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interviewer> getById(@PathVariable Long id) {
        Interviewer interviewer = interviewerService.getById(id);
        return interviewer != null ? ResponseEntity.ok(interviewer)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Interviewer create(@RequestBody Interviewer interviewer) {
        return interviewerService.save(interviewer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        interviewerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
