package com.example.interviewmgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.interviewmgmt.model.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
