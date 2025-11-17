-- ===========================
-- Candidates
-- ===========================
INSERT INTO candidate (id, name, email, phone)
VALUES (1, 'Alice Johnson', 'alice.johnson@email.com', '1234567890');

INSERT INTO candidate (id, name, email, phone)
VALUES (2, 'Bob Miller', 'bob.miller@email.com', '0987654321');

-- ===========================
-- Questions (Global pool)
-- ===========================
INSERT INTO question (id, text, area, difficulty, correct_answer)
VALUES
    (1, 'Explain the difference between List and Set in Java.', 'Java', 'Medium', 'List allows duplicates, Set does not.'),
    (2, 'What is a test case?', 'QA', 'Easy', 'A set of conditions to verify functionality.'),
    (3, 'What is polymorphism in Java?', 'Java', 'Hard', 'The ability of an object to take many forms.'),
    (4, 'What is regression testing?', 'QA', 'Medium', 'Testing to ensure new code doesn’t break existing features.'),
    (5, 'What is the difference between abstract class and interface?', 'Java', 'Medium', 'Interfaces define contracts; abstract classes can include implementations.'),
    (6, 'How do you handle exceptions in Java?', 'Java', 'Easy', 'Using try-catch blocks.');

-- ===========================
-- Tests
-- ===========================
-- Predefined tests (each could represent a dynamically generated one)
INSERT INTO test (id, area, difficulty)
VALUES
    (1, 'Java', 'Medium'),
    (2, 'QA', 'Easy');

-- Associate questions with tests (for simplicity)
INSERT INTO question (id, text, area, difficulty, correct_answer, test_id)
VALUES
    (7, 'Explain the JVM architecture.', 'Java', 'Medium', 'Class loader, memory area, execution engine.', 1),
    (8, 'What is a smoke test?', 'QA', 'Easy', 'A preliminary test to check basic functionality.', 2);

-- ===========================
-- Interviews
-- ===========================
INSERT INTO interview (id, date_time, position, candidate_id, test_id, result)
VALUES
    (1, CURRENT_TIMESTAMP, 'Java Developer', 1, 1, 'Scheduled'),
    (2, CURRENT_TIMESTAMP, 'QA Engineer', 2, 2, 'Completed');
