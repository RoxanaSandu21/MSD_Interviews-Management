## Interview Management System

*Authors: Sandu Roxana-Alexandra & Ciot Tudor*

**Introduction**

This document describes the architecture, components, workflow, and functionality of the Interview Management System developed as part of an academic software engineering project. The application supports recruiters or employees in managing candidates, scheduling interviews, generating and evaluating tests, and keeping track of interviewers and results. The system is built with Java Spring Boot for the backend and a lightweight HTML/CSS/JavaScript interface served directly from Spring Boot.

**System Architecture Overview**

The application follows a layered architecture:
- Presentation Layer: A static web interface located in src/main/resources/static, using HTML, CSS, and vanilla JavaScript.
- REST API Layer: Controllers exposing endpoints under /api/... paths for CRUD operations and business workflows.
- Service Layer: Contains business logic such as interview scheduling, test generation, test evaluation, and validation.
- Persistence Layer: JPA Entities, Repositories, and H2 in-memory database used for development.
- Data Initialization: data.sql pre-loads sample candidates, tests, questions and interviewers.

**Entities**

The main domain entities of the system are:

1. Candidate – Represents a person applying for a job. Has name, email, phone, and a list of interviews.
2. Interview – Represents a scheduled interview. Contains position, datetime, assigned interviewer, candidate, optional test, status (Scheduled/Completed), and result.
3. Test – A set of questions used to evaluate a candidate. Includes area, difficulty, list of questions, and a list of boolean values representing correctness of answers.
4. Question – Represents a question asked during a test, including its correct answer.
5. Interviewer – Represents an employee conducting interviews.
The relationships include: Candidate 1..* Interviews, Interview *..1 Candidate, Interview *..1 Interviewer, Test 1..* Questions.

**REST API Overview**

Each entity has a corresponding controller offering CRUD operations and business endpoints:
- /api/candidates – Add candidates, list candidates, view specific candidate including their interviews.
- /api/interviews – Schedule interviews, complete interviews, retrieve all or per candidate/interviewer.
- /api/tests – Create tests, attach questions, dynamically generate tests, view test questions.
- /api/questions – Add questions globally or attach them to tests.
- /api/interviewers – CRUD operations and view interviews conducted by an interviewer.

**Business Logic**

Core business logic resides in the Service layer:
- CandidateService – Includes email uniqueness validation.
- InterviewService – Assigns candidate, interviewer, test; prevents double-booking; marks interviews completed.
- TestService – Generates tests based on filters, attaches questions to tests.
- Test Evaluation Workflow – When an interview starts, the assigned test is shown to the recruiter, who marks whether each question was answered correctly. The sum of correct answers is stored and displayed as the interview result.

**User Interface**

The UI is implemented as a multi-tab interface:
- Candidates Tab – Add candidates, view interviews for each candidate.
- Interviewers Tab – Add interviewers, view interviews they have conducted.
- Interviews Tab – View all interviews, start scheduled interviews, complete them.
- Tests Tab – Create tests, view questions per test.
- Questions Tab – Add questions globally or assign them to a specific test.

The UI interacts with the backend exclusively through fetch() REST calls and updates dynamically without page reload.

**Data Flow Example**

1. Recruiter adds a candidate.
2. Recruiter schedules an interview, selecting candidate, interviewer, test.
3. The interview appears in both Candidate and Interviewer views.
4. Recruiter clicks Start → The test questions appear with correct answers visible.
5. Recruiter evaluates responses and submits.
6. Interview result (e.g., 3/5 correct) is stored and immediately updated in UI.

**Testing**

The project includes JUnit tests:

- Repository tests using @DataJpaTest
- Service-layer unit tests using Mockito
  
These ensure correct validation, scheduling logic, and persistence behavior.

**Conclusion**

The Interview Management System demonstrates a complete full-stack architecture including RESTful backend, client-side UI, validation, business workflows, test evaluation, and persistent domain modeling. It is extendable for real company use, such as integrating authentication, email notifications, or advanced reporting.
