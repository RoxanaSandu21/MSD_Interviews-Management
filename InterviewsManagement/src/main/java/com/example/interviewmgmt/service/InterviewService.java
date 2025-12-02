package com.example.interviewmgmt.service;

import com.example.interviewmgmt.model.Candidate;
import com.example.interviewmgmt.model.Test;
import com.example.interviewmgmt.model.Question;
import com.example.interviewmgmt.model.Interview;
import com.example.interviewmgmt.repo.CandidateRepository;
import com.example.interviewmgmt.repo.InterviewRepository;
import com.example.interviewmgmt.repo.QuestionRepository;
import com.example.interviewmgmt.repo.TestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class InterviewService {


    private final InterviewRepository interviewRepository;
    private final CandidateRepository candidateRepository;
    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;

    public InterviewService(InterviewRepository interviewRepository,
                            CandidateRepository candidateRepository,
                            QuestionRepository questionRepository,
                            TestRepository testRepository) {
        this.interviewRepository = interviewRepository;
        this.candidateRepository = candidateRepository;
        this.questionRepository = questionRepository;
        this.testRepository = testRepository;
    }

    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    public Optional<Interview> getInterviewById(Long id) {
        return interviewRepository.findById(id);
    }

    public Interview saveInterview(Interview interview) {
        return interviewRepository.save(interview);
    }

    public void deleteInterview(Long id) {
        interviewRepository.deleteById(id);
    }

    @Transactional
    public Interview scheduleInterview(Long candidateId,
                                       LocalDateTime dateTime,
                                       String position,
                                       String area,
                                       String difficulty,
                                       int numberOfQuestions) {

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + candidateId));

        // business rule: no two interviews at the same time for this candidate
        boolean hasInterviewAtSameTime = interviewRepository
                .findAll().stream()
                .anyMatch(i -> i.getCandidate() != null
                        && i.getCandidate().getId().equals(candidateId)
                        && i.getDateTime().equals(dateTime));

        if (hasInterviewAtSameTime) {
            throw new IllegalStateException("Candidate already has an interview at this time.");
        }

        // create test with selected questions
        Test test = generateTest(area, difficulty, numberOfQuestions);

        Interview interview = new Interview(dateTime, position);
        interview.setCandidate(candidate);
        interview.setTest(test);

        return interviewRepository.save(interview);
    }

    private Test generateTest(String area, String difficulty, int numberOfQuestions) {
        // find matching questions
        List<Question> all = questionRepository.findAll();
        List<Question> filtered = all.stream()
                .filter(q -> q.getArea().equalsIgnoreCase(area)
                        && q.getDifficulty().equalsIgnoreCase(difficulty))
                .limit(numberOfQuestions)
                .toList();

        if (filtered.isEmpty()) {
            throw new IllegalStateException("No questions available for area=" + area +
                    " and difficulty=" + difficulty);
        }

        Test test = new Test(area, difficulty);
        filtered.forEach(test::addQuestion);

        return testRepository.save(test);
    }
}

