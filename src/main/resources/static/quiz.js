const params = new URLSearchParams(window.location.search);

const quizId = params.get("id");

let questions = [];

async function loadQuiz() {

    try {

        const quizResponse =
            await fetch(`/api/quizzes/${quizId}`);

        const quiz = await quizResponse.json();

        document.getElementById("quizTitle").textContent =
            quiz.title;

        document.getElementById("quizDescription").textContent =
            quiz.description || "";

        const questionResponse =
            await fetch(`/api/quizzes/${quizId}/questions`);

        questions = await questionResponse.json();

        displayQuestions();

    } catch (error) {

        document.getElementById("questions").innerHTML =
            "<p>Unable to load quiz.</p>";
    }
}

function displayQuestions() {

    const container =
        document.getElementById("questions");

    container.innerHTML = "";

    questions.forEach((question, index) => {

        const div = document.createElement("div");

        div.className = "quiz";

        div.innerHTML = `
            <h3>${index + 1}. ${question.questionText}</h3>

            <label>
                <input type="radio"
                       name="question-${question.id}"
                       value="A">
                ${question.optionA}
            </label>
            <br>

            <label>
                <input type="radio"
                       name="question-${question.id}"
                       value="B">
                ${question.optionB}
            </label>
            <br>

            <label>
                <input type="radio"
                       name="question-${question.id}"
                       value="C">
                ${question.optionC}
            </label>
            <br>

            <label>
                <input type="radio"
                       name="question-${question.id}"
                       value="D">
                ${question.optionD}
            </label>
        `;

        container.appendChild(div);
    });
}

function submitQuiz() {

    let score = 0;

    questions.forEach(question => {

        const selected =
            document.querySelector(
                `input[name="question-${question.id}"]:checked`
            );

        if (selected &&
            selected.value.toUpperCase() ===
            question.correctOption.toUpperCase()) {

            score++;
        }
    });

    document.getElementById("result").textContent =
        `Score: ${score} / ${questions.length}`;
}

loadQuiz();