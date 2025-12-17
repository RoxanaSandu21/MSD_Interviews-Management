-- Interviewers
INSERT INTO interviewer (name, email, department)
VALUES ('John Recruiter', 'john@company.com', 'Development');

INSERT INTO interviewer (name, email, department)
VALUES ('Jane Recruiter', 'jane@company.com', 'Testing');

-- Candidates
INSERT INTO candidate (name, email, phone)
VALUES ('Alice Johnson', 'alice.johnson@email.com', '1234567890');

INSERT INTO candidate (name, email, phone)
VALUES ('Bob Miller', 'bob.miller@email.com', '0987654321');

-- Questions (Global pool)
INSERT INTO question (text, area, difficulty, correct_answer)
VALUES
    ('Explain the difference between List and Set in Java.', 'Java', 'Medium', 'List allows duplicates, Set does not.'),
    ('What is a test case?', 'QA', 'Easy', 'A set of conditions to verify functionality.'),
    ('What is polymorphism in Java?', 'Java', 'Hard', 'The ability of an object to take many forms.'),
    ('What is regression testing?', 'QA', 'Medium', 'Testing to ensure new code doesn’t break existing features.'),
    ('What is the difference between abstract class and interface?', 'Java', 'Medium', 'Interfaces define contracts; abstract classes can include implementations.'),
    ('How do you handle exceptions in Java?', 'Java', 'Easy', 'Using try-catch blocks.');

-- Tests
-- Predefined tests (each could represent a dynamically generated one)
INSERT INTO test (area, difficulty)
VALUES
    ('Java', 'Medium'),
    ('QA', 'Easy');

-- Associate questions with tests
INSERT INTO question (text, area, difficulty, correct_answer, test_id)
VALUES
    ('Explain the JVM architecture.', 'Java', 'Medium', 'Class loader, memory area, execution engine.', 1),
    ('What is a smoke test?', 'QA', 'Easy', 'A preliminary test to check basic functionality.', 2);

-- Interviews
INSERT INTO interview (date_time, position, candidate_id, test_id, interviewer_id, result)
VALUES
    (CURRENT_TIMESTAMP, 'Java Developer', 1, 1, 1, 'Scheduled'),
    (CURRENT_TIMESTAMP, 'QA Engineer', 2, 2, 2, 'Completed');

