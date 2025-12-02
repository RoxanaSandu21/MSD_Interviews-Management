package com.example.interviewmgmt;

import com.example.interviewmgmt.model.Candidate;
import com.example.interviewmgmt.repo.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.sql.init.mode=never"
})
public class CandidateRepositoryTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Test
    void testSaveAndFindCandidate() {
        Candidate candidate = new Candidate("Test User", "test.user@email.com", "1112223333");
        candidateRepository.save(candidate);

        Candidate found = candidateRepository.findById(candidate.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test User");
    }
}
