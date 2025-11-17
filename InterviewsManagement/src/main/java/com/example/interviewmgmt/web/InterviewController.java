package com.example.interviewmgmt.web;

import com.example.interviewmgmt.model.Interview;
import com.example.interviewmgmt.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping
    public List<Interview> getAll() {
        return interviewService.getAllInterviews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interview> getById(@PathVariable Long id) {
        return interviewService.getInterviewById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Interview create(@RequestBody Interview interview) {
        return interviewService.saveInterview(interview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interview> update(@PathVariable Long id, @RequestBody Interview updated) {
        return interviewService.getInterviewById(id)
                .map(existing -> {
                    existing.setDateTime(updated.getDateTime());
                    existing.setPosition(updated.getPosition());
                    existing.setResult(updated.getResult());
                    existing.setTest(updated.getTest());
                    return ResponseEntity.ok(interviewService.saveInterview(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        interviewService.deleteInterview(id);
        return ResponseEntity.noContent().build();
    }
}
