package com.example.interviewmgmt.web;

import com.example.interviewmgmt.model.Candidate;
import com.example.interviewmgmt.model.Interview;
import com.example.interviewmgmt.model.Test;
import com.example.interviewmgmt.repo.InterviewRepository;
import com.example.interviewmgmt.service.CandidateService;
import com.example.interviewmgmt.service.InterviewService;
import com.example.interviewmgmt.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;
    private final CandidateService candidateService;
    private final TestService testService;

    public InterviewController(InterviewService interviewService,  CandidateService candidateService, TestService testService) {
        this.interviewService = interviewService;
        this.candidateService = candidateService;
        this.testService = testService;
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
    public Interview createInterview(@RequestBody com.example.interviewmgmt.web.InterviewRequest req) {

        Candidate candidate = candidateService.getCandidateById(req.getCandidateId());
        if (candidate == null) {
            throw new RuntimeException("Candidate not found: " + req.getCandidateId());
        }

        Test test = null;
        if (req.getTestId() != null) {
            test = testService.getTestById(req.getTestId());
            if (test == null) {
                throw new RuntimeException("Test not found: " + req.getTestId());
            }
        }

        Interview interview = new Interview(req.getDateTime(), req.getPosition());
        interview.setCandidate(candidate);
        interview.setTest(test);
        interview.setResult(req.getResult());

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
