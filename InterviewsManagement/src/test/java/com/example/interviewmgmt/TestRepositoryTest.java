package com.example.interviewmgmt;

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
    void testSaveAndFindTest() {
        com.example.interviewmgmt.model.Test test = new com.example.interviewmgmt.model.Test("Java", "Medium");

        com.example.interviewmgmt.model.Test saved = testRepository.save(test);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getArea()).isEqualTo("Java");
        assertThat(saved.getDifficulty()).isEqualTo("Medium");

        com.example.interviewmgmt.model.Test found = testRepository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getArea()).isEqualTo("Java");
    }
}
