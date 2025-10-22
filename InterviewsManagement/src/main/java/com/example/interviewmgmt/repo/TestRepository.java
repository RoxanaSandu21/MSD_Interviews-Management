package com.example.interviewmgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.interviewmgmt.model.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
}
