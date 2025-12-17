package com.example.interviewmgmt.web;

import com.example.interviewmgmt.dto.InterviewRequest;
import com.example.interviewmgmt.model.Candidate;
import com.example.interviewmgmt.model.Interview;
import com.example.interviewmgmt.model.Interviewer;
import com.example.interviewmgmt.model.Test;
import com.example.interviewmgmt.dto.TestEvaluationRequest;
import com.example.interviewmgmt.service.CandidateService;
import com.example.interviewmgmt.service.InterviewService;
import com.example.interviewmgmt.service.InterviewerService;
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
    private final InterviewerService interviewerService;

    public InterviewController(InterviewService interviewService,  CandidateService candidateService, TestService testService, InterviewerService interviewerService) {
        this.interviewService = interviewService;
        this.candidateService = candidateService;
        this.testService = testService;
        this.interviewerService = interviewerService;
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
    public Interview createInterview(@RequestBody InterviewRequest req) {

        Candidate candidate = candidateService.getCandidateById(req.getCandidateId());
        if (candidate == null) {
            throw new RuntimeException("Candidate not found: " + req.getCandidateId());
        }

        Interviewer interviewer = interviewerService.getById(req.getInterviewerId());
        if (interviewer == null) {
            throw new RuntimeException("Interviewer not found: " + req.getInterviewerId());
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
        interview.setInterviewer(interviewer);
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

    @PostMapping("/{id}/complete")
    public ResponseEntity<Interview> completeInterview(@PathVariable Long id,
                                                       @RequestBody TestEvaluationRequest request) {
        Interview interview = interviewService.getInterviewById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        Test test = interview.getTest();
        if (test == null) {
            throw new RuntimeException("This interview has no test assigned");
        }

        if (test.getQuestions() == null || test.getQuestions().isEmpty()) {
            throw new RuntimeException("Test has no questions");
        }

        List<Boolean> flags = request.getAnswersCorrect();
        int questionCount = test.getQuestions().size();

        // Normalize answersCorrect size to number of questions
        List<Boolean> normalized = new java.util.ArrayList<>();
        for (int i = 0; i < questionCount; i++) {
            boolean val = (flags != null && i < flags.size() && Boolean.TRUE.equals(flags.get(i)));
            normalized.add(val);
        }
        test.setAnswersCorrect(normalized);

        long correctCount = test.getNumberOfCorrectAnswers();

        interview.setResult("Completed");

        Interview saved = interviewService.saveInterview(interview);
        return ResponseEntity.ok(saved);
    }
}
