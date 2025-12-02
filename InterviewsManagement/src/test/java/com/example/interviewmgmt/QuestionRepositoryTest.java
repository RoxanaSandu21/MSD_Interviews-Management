package com.example.interviewmgmt;

import com.example.interviewmgmt.model.Question;
import com.example.interviewmgmt.repo.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.sql.init.mode=never"
})
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void testSaveAndFindQuestion() {
        Question question = new Question(
                "What is polymorphism in Java?",
                "Java",
                "Hard",
                "The ability of an object to take many forms."
        );

        Question saved = questionRepository.save(question);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getText()).isEqualTo("What is polymorphism in Java?");
        assertThat(saved.getArea()).isEqualTo("Java");
        assertThat(saved.getDifficulty()).isEqualTo("Hard");

        Question found = questionRepository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getCorrectAnswer()).contains("many forms");
    }
}
