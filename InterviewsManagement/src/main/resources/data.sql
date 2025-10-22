-- Candidates
INSERT INTO candidate (id, first_name, last_name, email, phone, position_applied)
VALUES (1, 'Alice', 'Johnson', 'alice.johnson@email.com', '1234567890', 'Java Developer');

INSERT INTO candidate (id, first_name, last_name, email, phone, position_applied)
VALUES (2, 'Bob', 'Miller', 'bob.miller@email.com', '0987654321', 'QA Engineer');

-- Tests
INSERT INTO test (id, test_name, duration_minutes)
VALUES (1, 'Java Technical Test', 60);

INSERT INTO test (id, test_name, duration_minutes)
VALUES (2, 'QA Automation Test', 45);

-- Questions
INSERT INTO question (id, text, difficulty, category)
VALUES (1, 'Explain the difference between List and Set in Java.', 'MEDIUM', 'Java');

INSERT INTO question (id, text, difficulty, category)
VALUES (2, 'What is a test case?', 'EASY', 'QA');

-- Interviews
INSERT INTO interview (id, scheduled_at, interviewer_name, status, candidate_id, test_id)
VALUES (1, CURRENT_TIMESTAMP, 'John Doe', 'Scheduled', 1, 1);

INSERT INTO interview (id, scheduled_at, interviewer_name, status, candidate_id, test_id)
VALUES (2, CURRENT_TIMESTAMP, 'Jane Smith', 'Completed', 2, 2);
