package com.example.interviewmgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.interviewmgmt.model.Question;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
