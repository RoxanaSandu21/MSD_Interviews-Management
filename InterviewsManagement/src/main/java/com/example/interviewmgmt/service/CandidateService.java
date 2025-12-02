package com.example.interviewmgmt.service;

import com.example.interviewmgmt.model.Candidate;
import com.example.interviewmgmt.repo.CandidateRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id).orElse(null);
    }

    public Candidate saveCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }

    public Candidate registerCandidate(Candidate candidate) {
        // business rule: email must be unique
        List<Candidate> candidates = candidateRepository.findAll();
        boolean emailExists = candidates.stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(candidate.getEmail()));

        if (emailExists) {
            throw new IllegalStateException("A candidate with this email already exists.");
        }

        return candidateRepository.save(candidate);
    }
}
