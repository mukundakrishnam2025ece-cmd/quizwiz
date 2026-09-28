// async function loadResults() {
//
//     const quizId =
//         document.getElementById("quizId").value;
//
//     const results =
//         document.getElementById("results");
//
//     if (!quizId) {
//
//         results.innerHTML =
//             "<p>Please enter Quiz ID.</p>";
//
//         return;
//     }
//
//     try {
//
//         const response =
//             await fetch(`/api/attempts/quiz/${quizId}/results`);
//
//         const attempts =
//             await response.json();
//
//         if (!response.ok) {
//
//             results.innerHTML =
//                 "<p>Unable to load results.</p>";
//
//             return;
//         }
//
//         if (attempts.length === 0) {
//
//             results.innerHTML =
//                 "<p>No students have attempted this quiz yet.</p>";
//
//             return;
//         }
//
//         let html = `
//             <h3>Student Results</h3>
//
//             <table style="width:100%; border-collapse:collapse;">
//                 <tr>
//                     <th style="padding:10px; border:1px solid #ddd;">
//                         Student
//                     </th>
//
//                     <th style="padding:10px; border:1px solid #ddd;">
//                         Register Number
//                     </th>
//
//                     <th style="padding:10px; border:1px solid #ddd;">
//                         Score
//                     </th>
//
//                     <th style="padding:10px; border:1px solid #ddd;">
//                         Status
//                     </th>
//                 </tr>
//         `;
//
//         attempts.forEach(attempt => {
//
//             html += `
//                 <tr>
//
//                     <td style="padding:10px; border:1px solid #ddd;">
//                         ${attempt.student.name}
//                     </td>
//
//                     <td style="padding:10px; border:1px solid #ddd;">
//                         ${attempt.student.registerNumber}
//                     </td>
//
//                     <td style="padding:10px; border:1px solid #ddd;">
//                         ${attempt.score}
//                     </td>
//
//                     <td style="padding:10px; border:1px solid #ddd;">
//                         ${attempt.status}
//                     </td>
//
//                 </tr>
//             `;
//         });
//
//         html += "</table>";
//
//         results.innerHTML = html;
//
//     } catch (error) {
//
//         results.innerHTML =
//             "<p>Unable to connect to server.</p>";
//     }
// }