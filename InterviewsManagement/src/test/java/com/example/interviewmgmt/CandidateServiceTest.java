package com.example.interviewmgmt;

import com.example.interviewmgmt.model.Candidate;
import com.example.interviewmgmt.repo.CandidateRepository;
import com.example.interviewmgmt.service.CandidateService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CandidateServiceTest {

    private final CandidateRepository candidateRepository = Mockito.mock(CandidateRepository.class);
    private final CandidateService candidateService = new CandidateService(candidateRepository);

    @Test
    void testRegisterCandidateSuccessfully() {
        Candidate newCandidate = new Candidate("Alice", "alice@test.com", "12345");

        Mockito.when(candidateRepository.findAll()).thenReturn(List.of());
        Mockito.when(candidateRepository.save(newCandidate)).thenReturn(newCandidate);

        Candidate saved = candidateService.registerCandidate(newCandidate);

        assertThat(saved).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("alice@test.com");
        Mockito.verify(candidateRepository).save(newCandidate);
    }

    @Test
    void testRegisterCandidateDuplicateEmailThrowsException() {
        Candidate existing = new Candidate("Bob", "bob@test.com", "99999");
        Candidate newCandidate = new Candidate("John", "bob@test.com", "88888");

        Mockito.when(candidateRepository.findAll()).thenReturn(List.of(existing));

        assertThatThrownBy(() -> candidateService.registerCandidate(newCandidate))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void testGetCandidateById() {

        Candidate candidate = Mockito.mock(Candidate.class);

        Mockito.when(candidate.getId()).thenReturn(1L);
        Mockito.when(candidate.getName()).thenReturn("John Doe");
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));

        Candidate found = candidateService.getCandidateById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("John Doe");
    }
}
