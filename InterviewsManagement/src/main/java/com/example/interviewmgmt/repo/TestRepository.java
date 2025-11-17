package com.example.interviewmgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.interviewmgmt.model.Test;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
}
