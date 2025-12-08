package com.example.interviewmgmt.web;

import com.example.interviewmgmt.model.Question;
import com.example.interviewmgmt.model.Test;
import com.example.interviewmgmt.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public List<Test> getAll() {
        return testService.getAllTests();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Test> getById(@PathVariable Long id) {
        Test test = testService.getTestById(id);
        return (test != null) ? ResponseEntity.ok(test) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Test create(@RequestBody Test test) {
        return testService.saveTest(test);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        testService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }

    /** Dynamic test generation endpoint **/
    @GetMapping("/generate")
    public Test generateTest(@RequestParam String area, @RequestParam String difficulty) {
        return testService.generateTest(area, difficulty);
    }

    @PostMapping("/{testId}/questions")
    public Question addQuestionToTest(@PathVariable Long testId,
                                      @RequestBody Question question) {

        Test test = testService.getTestById(testId);
        if (test == null) {
            throw new RuntimeException("Test not found");
        }

        test.addQuestion(question);      // updates the list in the entity
        testService.saveTest(test);      // persists both test + new question

        return question;
    }

}
