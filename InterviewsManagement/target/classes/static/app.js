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
          <td>${c.phone}</td>`;
        tbody.appendChild(tr);
    });
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
        const candidateId = i.candidate ? i.candidate.id : "";
        const testId = i.test ? i.test.id : "";
        tr.innerHTML = `
          <td>${i.id}</td>
          <td>${i.dateTime}</td>
          <td>${i.position}</td>
          <td>${candidateId}</td>
          <td>${testId}</td>
          <td>${i.result || ""}</td>`;
        tbody.appendChild(tr);
    });
}

document.getElementById("interview-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const body = {
        dateTime: document.getElementById("interview-datetime").value,
        position: document.getElementById("interview-position").value,
        candidateId: Number(document.getElementById("interview-candidateId").value),
        testId: document.getElementById("interview-testId").value ?
            Number(document.getElementById("interview-testId").value) : null,
        result: document.getElementById("interview-result").value || "Scheduled"
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
    } else {
        showToast("Error scheduling interview");
    }
});

// ----- Tests -----
async function loadTests() {
    const res = await fetch(`${API_BASE}/tests`);
    const data = await res.json();
    const tbody = document.querySelector("#tests-table tbody");
    tbody.innerHTML = "";
    data.forEach(t => {
        const numQuestions = t.questions ? t.questions.length : 0;
        const id = t.id ?? "";
        tbody.innerHTML += `
          <tr>
            <td>${id}</td>
            <td>${t.area}</td>
            <td>${t.difficulty}</td>
            <td>${numQuestions}</td>
          </tr>`;
    });
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
    const testIdValue = document.getElementById("question-testId").value;
    const body = {
        text: document.getElementById("question-text").value,
        area: document.getElementById("question-area").value,
        difficulty: document.getElementById("question-difficulty").value,
        correctAnswer: document.getElementById("question-correct").value
    };
    if (testIdValue) {
        body.testId = Number(testIdValue);
    }

    const res = await fetch(`${API_BASE}/questions`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });
    if (res.ok) {
        showToast("Question added");
        e.target.reset();
        loadQuestions();
        loadTests(); // to refresh #Questions counts
    } else {
        showToast("Error adding question");
    }
});

// ----- Initial load -----
loadCandidates();
loadInterviews();
loadTests();
loadQuestions();
