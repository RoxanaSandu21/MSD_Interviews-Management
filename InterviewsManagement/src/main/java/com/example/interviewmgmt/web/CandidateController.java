package com.example.interviewmgmt.web;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.interviewmgmt.model.Candidate;
import com.example.interviewmgmt.service.CandidateService;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin("*")  // Allows frontend apps to connect
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public List<Candidate> getAll() {
        return candidateService.getAllCandidates();
    }

    @PostMapping
    public Candidate create(@RequestBody Candidate candidate) {
        return candidateService.addCandidate(candidate);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
    }
}
