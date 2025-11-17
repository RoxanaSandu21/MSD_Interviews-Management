package com.example.interviewmgmt.service;

import com.example.interviewmgmt.model.Question;
import com.example.interviewmgmt.model.Test;
import com.example.interviewmgmt.repo.QuestionRepository;
import com.example.interviewmgmt.repo.TestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;

    public TestService(TestRepository testRepository, QuestionRepository questionRepository) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
    }

    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    public Test getTestById(Long id) {
        return testRepository.findById(id).orElse(null);
    }

    public Test saveTest(Test test) {
        return testRepository.save(test);
    }

    public void deleteTest(Long id) {
        testRepository.deleteById(id);
    }

    /** Dynamically generate a test from question pool **/
    public Test generateTest(String area, String difficulty) {
        List<Question> filtered = questionRepository.findAll().stream()
                .filter(q -> q.getArea().equalsIgnoreCase(area)
                        && q.getDifficulty().equalsIgnoreCase(difficulty))
                .collect(Collectors.toList());

        Test test = new Test(area, difficulty);
        test.setQuestions(filtered);
        return testRepository.save(test);
    }
}
