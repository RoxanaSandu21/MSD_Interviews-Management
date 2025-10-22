package com.example.interviewmgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.interviewmgmt.model.Interview;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
}
