package com.example.interviewmgmt;

import com.example.interviewmgmt.model.Question;
import com.example.interviewmgmt.repo.TestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.sql.init.mode=never"
})
class TestRepositoryTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    void testSaveAndFindTestEntity() {

        com.example.interviewmgmt.model.Test testEntity =
                new com.example.interviewmgmt.model.Test("Java", "Medium");

        // simulate adding 2 questions
        Question q1 = new Question("Q1", "Java", "Medium", "A1");
        Question q2 = new Question("Q2", "Java", "Medium", "A2");
        testEntity.addQuestion(q1);
        testEntity.addQuestion(q2);

        // mark first as correct, second as incorrect
        testEntity.setAnswerCorrectness(0, true);
        testEntity.setAnswerCorrectness(1, false);

        com.example.interviewmgmt.model.Test saved = testRepository.save(testEntity);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getQuestions()).hasSize(2);
        assertThat(saved.getAnswersCorrect()).containsExactly(true, false);
    }

}
