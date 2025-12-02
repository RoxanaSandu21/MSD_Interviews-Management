package com.example.interviewmgmt;

import com.example.interviewmgmt.model.Candidate;
import com.example.interviewmgmt.model.Interview;
import com.example.interviewmgmt.model.Question;
import com.example.interviewmgmt.repo.CandidateRepository;
import com.example.interviewmgmt.repo.InterviewRepository;
import com.example.interviewmgmt.repo.QuestionRepository;
import com.example.interviewmgmt.repo.TestRepository;
import com.example.interviewmgmt.service.InterviewService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class InterviewServiceTest {

    private final InterviewRepository interviewRepository = Mockito.mock(InterviewRepository.class);
    private final CandidateRepository candidateRepository = Mockito.mock(CandidateRepository.class);
    private final QuestionRepository questionRepository = Mockito.mock(QuestionRepository.class);
    private final TestRepository testRepository = Mockito.mock(TestRepository.class);

    private final InterviewService interviewService =
            new InterviewService(interviewRepository, candidateRepository, questionRepository, testRepository);

    @Test
    void testScheduleInterviewSuccessfully() {
        Long candidateId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Candidate candidate = new Candidate("Alice", "alice@test.com", "12345");

        Mockito.when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        Mockito.when(interviewRepository.findAll()).thenReturn(List.of());

        Question q1 = new Question("What is OOP?", "Java", "Medium", "Object-oriented programming");
        Question q2 = new Question("Explain inheritance", "Java", "Medium", "Concept of extending classes");

        Mockito.when(questionRepository.findAll()).thenReturn(List.of(q1, q2));
        Mockito.when(testRepository.save(Mockito.any(com.example.interviewmgmt.model.Test.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(interviewRepository.save(Mockito.any(Interview.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Interview interview = interviewService.scheduleInterview(
                candidateId,
                now,
                "Java Developer",
                "Java",
                "Medium",
                2
        );

        assertThat(interview).isNotNull();
        assertThat(interview.getCandidate()).isEqualTo(candidate);
        assertThat(interview.getTest().getQuestions()).hasSize(2);
        assertThat(interview.getPosition()).isEqualTo("Java Developer");
    }

    @Test
    void testScheduleInterviewFailsWhenCandidateAlreadyHasInterviewAtSameTime() {
        Long candidateId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Candidate candidate = Mockito.mock(Candidate.class);
        Mockito.when(candidate.getId()).thenReturn(candidateId);

        Mockito.when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));

        Interview existing = new Interview(now, "Java Dev");
        existing.setCandidate(candidate);

        Mockito.when(interviewRepository.findAll()).thenReturn(List.of(existing));

        assertThatThrownBy(() ->
                interviewService.scheduleInterview(candidateId, now, "Java Developer", "Java", "Medium", 1)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already has an interview");
    }

    @Test
    void testSaveInterviewBasic() {
        LocalDateTime now = LocalDateTime.now();
        Interview interview = new Interview(now, "Java Developer");

        Mockito.when(interviewRepository.save(interview)).thenReturn(interview);

        Interview saved = interviewService.saveInterview(interview);

        assertThat(saved).isNotNull();
        Mockito.verify(interviewRepository).save(interview);
    }
}
