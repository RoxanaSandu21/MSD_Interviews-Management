package com.example.interviewmgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.interviewmgmt.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
