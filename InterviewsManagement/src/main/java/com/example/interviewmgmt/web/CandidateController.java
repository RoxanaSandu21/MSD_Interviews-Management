package com.example.interviewmgmt.web;

import com.example.interviewmgmt.model.Candidate;
import com.example.interviewmgmt.service.CandidateService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public List<Candidate> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @GetMapping("/{id}")
    public Candidate getCandidateById(@PathVariable Long id) {
        return candidateService.getCandidateById(id);
    }

    @PostMapping
    public Candidate addCandidate(@RequestBody Candidate candidate) {
        return candidateService.registerCandidate(candidate);
    }

    @DeleteMapping("/{id}")
    public void deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
    }
}
