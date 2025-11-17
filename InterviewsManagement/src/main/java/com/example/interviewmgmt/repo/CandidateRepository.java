package com.example.interviewmgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.interviewmgmt.model.Candidate;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
