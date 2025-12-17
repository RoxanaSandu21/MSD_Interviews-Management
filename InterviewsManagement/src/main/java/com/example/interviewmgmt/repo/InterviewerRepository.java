package com.example.interviewmgmt.repo;

import com.example.interviewmgmt.model.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {
}
