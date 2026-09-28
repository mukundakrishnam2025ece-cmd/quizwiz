async function loadQuizzes() {

    const quizList = document.getElementById("quizList");

    try {

        const response = await fetch("/api/quizzes");

        const quizzes = await response.json();

        quizList.innerHTML = "";

        if (quizzes.length === 0) {
            quizList.innerHTML = "<p>No quizzes available.</p>";
            return;
        }

        quizzes.forEach(quiz => {

            const div = document.createElement("div");

            div.className = "quiz";

            div.innerHTML = `
                <h3>${quiz.title}</h3>
                <p>${quiz.description || ""}</p>
                <p><strong>Duration:</strong> ${quiz.duration} minutes</p>
                <button onclick="startQuiz(${quiz.id})">
                    Start Quiz
                </button>
            `;

            quizList.appendChild(div);
        });

    } catch (error) {

        quizList.innerHTML =
            "<p>Unable to load quizzes.</p>";
    }
}

function startQuiz(id) {
    window.location.href = `quiz.html?id=${id}`;
}

loadQuizzes();