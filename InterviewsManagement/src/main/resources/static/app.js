const API_BASE = "/api";

const tabs = document.querySelectorAll(".tab-button");
const contents = document.querySelectorAll(".tab-content");
const toastEl = document.getElementById("toast");

function showToast(message) {
    toastEl.textContent = message;
    toastEl.classList.remove("hidden");
    toastEl.classList.add("show");
    setTimeout(() => {
        toastEl.classList.remove("show");
        setTimeout(() => toastEl.classList.add("hidden"), 200);
    }, 2000);
}

// ----- Tab switching -----
tabs.forEach(btn => {
    btn.addEventListener("click", () => {
        tabs.forEach(b => b.classList.remove("active"));
        contents.forEach(c => c.classList.remove("active"));

        btn.classList.add("active");
        document.getElementById("tab-" + btn.dataset.tab).classList.add("active");
    });
});

// ----- Candidates -----
async function loadCandidates() {
    const res = await fetch(`${API_BASE}/candidates`);
    const data = await res.json();
    const tbody = document.querySelector("#candidates-table tbody");
    tbody.innerHTML = "";
    data.forEach(c => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${c.id}</td>
          <td>${c.name}</td>
          <td>${c.email}</td>
          <td>${c.phone}</td>
          <td>
             <button class="small-button view-interviews-btn"
                     data-id="${c.id}"
                     data-name="${c.name}">
                 Interviews
             </button>
          </td>`;
        tbody.appendChild(tr);
    });

    // add click handlers for "Interviews" buttons
    tbody.querySelectorAll(".view-interviews-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const id = btn.dataset.id;
            const name = btn.dataset.name;
            loadInterviewsForCandidate(id, name);
        });
    });
}

async function loadInterviewsForCandidate(candidateId, candidateName) {
    const res = await fetch(`${API_BASE}/candidates/${candidateId}`);
    if (!res.ok) {
        showToast("Error loading interviews for candidate");
        return;
    }
    const candidate = await res.json();

    const panel = document.getElementById("candidate-interviews-panel");
    const nameSpan = document.getElementById("candidate-interviews-name");
    const tbody = document.querySelector("#candidate-interviews-table tbody");

    nameSpan.textContent = `${candidate.name} (ID ${candidate.id})`;
    tbody.innerHTML = "";

    if (candidate.interviews && candidate.interviews.length > 0) {
        candidate.interviews.forEach(i => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
              <td>${i.id}</td>
              <td>${i.dateTime}</td>
              <td>${i.position}</td>
              <td>${i.result || ""}</td>`;
            tbody.appendChild(tr);
        });
    } else {
        const tr = document.createElement("tr");
        tr.innerHTML = `<td colspan="4">No interviews for this candidate.</td>`;
        tbody.appendChild(tr);
    }

    panel.classList.remove("hidden");
}

document.getElementById("candidate-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const body = {
        name: document.getElementById("candidate-name").value,
        email: document.getElementById("candidate-email").value,
        phone: document.getElementById("candidate-phone").value
    };
    const res = await fetch(`${API_BASE}/candidates`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });

    if (res.ok) {
        showToast("Candidate added");
        e.target.reset();
        loadCandidates();
    } else {
        showToast("Error adding candidate");
    }
});

// ----- Interviews -----
async function loadInterviews() {
    const res = await fetch(`${API_BASE}/interviews`);
    const data = await res.json();
    const tbody = document.querySelector("#interviews-table tbody");
    tbody.innerHTML = "";
    data.forEach(i => {
        const tr = document.createElement("tr");
        const testId = i.test ? i.test.id : "";
        const candidateId = i.candidateId || "";

        const status = i.result || "Scheduled";

        let resultCellHtml = "";

        if (status === "Completed" && i.test && i.test.questions) {
            const total = i.test.questions.length;
            const correct = i.test.answersCorrect
                ? i.test.answersCorrect.filter(v => v === true).length
                : 0;
            resultCellHtml = `${correct}/${total}`;
        } else if (status === "Scheduled" && i.test && i.test.questions && i.test.questions.length > 0) {
            // Show Start button only if there is a test with questions
            resultCellHtml = `<button class="small-button start-test-btn" data-id="${i.id}">Start</button>`;
        } else {
            resultCellHtml = "";
        }

        tr.innerHTML = `
          <td>${i.id}</td>
          <td>${i.dateTime}</td>
          <td>${i.position}</td>
          <td>${testId}</td>
          <td>${candidateId}</td>
          <td>${status}</td>
          <td>${resultCellHtml}</td>
        `;
        tbody.appendChild(tr);
    });

    // attach click listeners for Start buttons
    tbody.querySelectorAll(".start-test-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const interviewId = btn.dataset.id;
            loadTestForInterview(interviewId);
        });
    });
}

async function loadTestForInterview(interviewId) {
    const res = await fetch(`${API_BASE}/interviews/${interviewId}`);
    if (!res.ok) {
        showToast("Error loading interview/test");
        return;
    }
    const interview = await res.json();

    if (!interview.test || !interview.test.questions || interview.test.questions.length === 0) {
        showToast("This interview has no test or no questions");
        return;
    }

    const panel = document.getElementById("interview-test-panel");
    const idSpan = document.getElementById("interview-test-id");
    const hiddenInterviewId = document.getElementById("evaluation-interview-id");
    const tbody = document.querySelector("#interview-test-questions-table tbody");

    idSpan.textContent = `${interview.test.id}`;
    hiddenInterviewId.value = interview.id;
    tbody.innerHTML = "";

    interview.test.questions.forEach((q, idx) => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${idx + 1}</td>
          <td>${q.text}</td>
          <td>${q.correctAnswer}</td>
          <td>
            <input type="checkbox" class="answer-correct-checkbox" data-index="${idx}">
          </td>
        `;
        tbody.appendChild(tr);
    });

    panel.classList.remove("hidden");
}


document.getElementById("interview-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const body = {
        candidateId: Number(document.getElementById("interview-candidateId").value),
        dateTime: document.getElementById("interview-datetime").value,
        position: document.getElementById("interview-position").value,
        result: document.getElementById("interview-result").value || "Scheduled",
        testId: document.getElementById("interview-testId").value ?
            Number(document.getElementById("interview-testId").value) : null
    };

    const res = await fetch(`${API_BASE}/interviews`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });

    if (res.ok) {
        showToast("Interview scheduled");
        e.target.reset();
        loadInterviews();
        // reload candidate-specific panel if it's open
        const panel = document.getElementById("candidate-interviews-panel");
        if (!panel.classList.contains("hidden")) {
            const nameSpan = document.getElementById("candidate-interviews-name");
            const text = nameSpan.textContent || "";
            const match = text.match(/ID (\d+)\)?$/);
            if (match) {
                const candidateId = match[1];
                loadInterviewsForCandidate(candidateId);
            }
        }
    } else {
        showToast("Error scheduling interview");
    }
});

document.getElementById("test-evaluation-form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const interviewId = document.getElementById("evaluation-interview-id").value;
    const checkboxes = document.querySelectorAll(".answer-correct-checkbox");
    const answersCorrect = Array.from(checkboxes).map(cb => cb.checked);

    const res = await fetch(`${API_BASE}/interviews/${interviewId}/complete`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ answersCorrect })
    });

    if (res.ok) {
        showToast("Interview completed");
        document.getElementById("interview-test-panel").classList.add("hidden");
        loadInterviews();

        // refresh candidate-specific panel if open
        const panel = document.getElementById("candidate-interviews-panel");
        if (!panel.classList.contains("hidden")) {
            const nameSpan = document.getElementById("candidate-interviews-name");
            const text = nameSpan.textContent || "";
            const match = text.match(/ID (\d+)\)?$/);
            if (match) {
                const candidateId = match[1];
                loadInterviewsForCandidate(candidateId);
            }
        }
    } else {
        showToast("Error completing interview");
    }
});


// ----- Tests -----
async function loadTests() {
    const res = await fetch(`${API_BASE}/tests`);
    const data = await res.json();
    const tbody = document.querySelector("#tests-table tbody");
    tbody.innerHTML = "";
    data.forEach(t => {
        const tr = document.createElement("tr");
        const numQuestions = t.questions ? t.questions.length : 0;
        const id = t.id ?? "";

        tr.innerHTML = `
          <td>${id}</td>
          <td>${t.area}</td>
          <td>${t.difficulty}</td>
          <td>${numQuestions}</td>
          <td>
            <button class="small-button view-test-btn" data-id="${id}">View</button>
          </td>
        `;

        tbody.appendChild(tr);
    });

    // Add click event to buttons
    tbody.querySelectorAll(".view-test-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            loadQuestionsForTest(btn.dataset.id);
        });
    });
}

async function loadQuestionsForTest(testId) {
    const res = await fetch(`${API_BASE}/tests/${testId}`);
    if (!res.ok) {
        showToast("Could not load questions for this test");
        return;
    }
    const test = await res.json();

    const panel = document.getElementById("test-questions-panel");
    const idSpan = document.getElementById("test-questions-id");
    const tbody = document.querySelector("#test-questions-table tbody");

    idSpan.textContent = test.id;
    tbody.innerHTML = "";

    if (test.questions && test.questions.length > 0) {
        test.questions.forEach(q => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
              <td>${q.id}</td>
              <td>${q.text}</td>
              <td>${q.area}</td>
              <td>${q.difficulty}</td>
            `;
            tbody.appendChild(tr);
        });
    } else {
        const tr = document.createElement("tr");
        tr.innerHTML = `<td colspan="4">No questions for this test.</td>`;
        tbody.appendChild(tr);
    }

    panel.classList.remove("hidden");
}

document.getElementById("test-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const body = {
        area: document.getElementById("test-area").value,
        difficulty: document.getElementById("test-difficulty").value
    };
    const res = await fetch(`${API_BASE}/tests`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });
    if (res.ok) {
        showToast("Test created");
        e.target.reset();
        loadTests();
    } else {
        showToast("Error creating test");
    }
});

// ----- Questions -----
async function loadQuestions() {
    const res = await fetch(`${API_BASE}/questions`);
    const data = await res.json();
    const tbody = document.querySelector("#questions-table tbody");
    tbody.innerHTML = "";
    data.forEach(q => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${q.id}</td>
          <td>${q.text}</td>
          <td>${q.area}</td>
          <td>${q.difficulty}</td>`;
        tbody.appendChild(tr);
    });
}

document.getElementById("question-form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const testIdValue = document.getElementById("question-testId").value.trim();

    const questionPayload = {
        text: document.getElementById("question-text").value,
        area: document.getElementById("question-area").value,
        difficulty: document.getElementById("question-difficulty").value,
        correctAnswer: document.getElementById("question-correct").value
    };

    let res;

    if (testIdValue) {
        // attach question to a specific test
        const testId = Number(testIdValue);
        res = await fetch(`${API_BASE}/tests/${testId}/questions`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(questionPayload)
        });
    } else {
        // just add question to global pool
        res = await fetch(`${API_BASE}/questions`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(questionPayload)
        });
    }

    if (res.ok) {
        showToast("Question added");
        e.target.reset();
        await loadQuestions();
        await loadTests(); // refresh #Questions column
    } else {
        showToast("Error adding question");
    }
});


// ----- Initial load -----
loadCandidates();
loadInterviews();
loadTests();
loadQuestions();
