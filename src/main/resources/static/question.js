// async function addQuestion() {
//
//     const quizId =
//         document.getElementById("quizId").value;
//
//     const questionText =
//         document.getElementById("questionText").value;
//
//     const optionA =
//         document.getElementById("optionA").value;
//
//     const optionB =
//         document.getElementById("optionB").value;
//
//     const optionC =
//         document.getElementById("optionC").value;
//
//     const optionD =
//         document.getElementById("optionD").value;
//
//     const correctOption =
//         document.getElementById("correctOption").value;
//
//     const message =
//         document.getElementById("message");
//
//     if (!quizId ||
//         !questionText ||
//         !optionA ||
//         !optionB ||
//         !optionC ||
//         !optionD ||
//         !correctOption) {
//
//         message.textContent =
//             "Please fill all fields.";
//
//         return;
//     }
//
//     try {
//
//         const response =
//             await fetch(`/api/quizzes/${quizId}/questions`, {
//
//                 method: "POST",
//
//                 headers: {
//                     "Content-Type": "application/json"
//                 },
//
//                 body: JSON.stringify({
//                     questionText: questionText,
//                     optionA: optionA,
//                     optionB: optionB,
//                     optionC: optionC,
//                     optionD: optionD,
//                     correctOption: correctOption
//                 })
//             });
//
//         const question = await response.json();
//
//         if (!response.ok) {
//
//             message.textContent =
//                 question.message || "Unable to add question.";
//
//             return;
//         }
//
//         message.textContent =
//             "Question added successfully!";
//
//         document.getElementById("questionText").value = "";
//         document.getElementById("optionA").value = "";
//         document.getElementById("optionB").value = "";
//         document.getElementById("optionC").value = "";
//         document.getElementById("optionD").value = "";
//         document.getElementById("correctOption").value = "";
//
//     } catch (error) {
//
//         message.textContent =
//             "Unable to connect to server.";
//     }
// }