// async function createQuiz() {
//
//     const title =
//         document.getElementById("title").value;
//
//     const description =
//         document.getElementById("description").value;
//
//     const duration =
//         document.getElementById("duration").value;
//
//     const message =
//         document.getElementById("message");
//
//     if (!title || !duration) {
//
//         message.textContent =
//             "Please enter quiz title and duration.";
//
//         return;
//     }
//
//     try {
//
//         const response =
//             await fetch("/api/quizzes", {
//
//                 method: "POST",
//
//                 headers: {
//                     "Content-Type": "application/json"
//                 },
//
//                 body: JSON.stringify({
//                     title: title,
//                     description: description,
//                     duration: Number(duration)
//                 })
//             });
//
//         const quiz = await response.json();
//
//         if (!response.ok) {
//
//             message.textContent =
//                 quiz.message || "Unable to create quiz.";
//
//             return;
//         }
//
//         message.textContent =
//             `Quiz created successfully! Quiz ID: ${quiz.id}`;
//
//         document.getElementById("title").value = "";
//         document.getElementById("description").value = "";
//         document.getElementById("duration").value = "";
//
//     } catch (error) {
//
//         message.textContent =
//             "Unable to connect to server.";
//     }
// }