// async function registerStudent() {
//
//     const name =
//         document.getElementById("name").value;
//
//     const email =
//         document.getElementById("email").value;
//
//     const registerNumber =
//         document.getElementById("registerNumber").value;
//
//     const message =
//         document.getElementById("message");
//
//     if (!name || !email || !registerNumber) {
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
//             await fetch("/api/students", {
//
//                 method: "POST",
//
//                 headers: {
//                     "Content-Type": "application/json"
//                 },
//
//                 body: JSON.stringify({
//                     name: name,
//                     email: email,
//                     registerNumber: registerNumber
//                 })
//             });
//
//         const student = await response.json();
//
//         if (!response.ok) {
//
//             message.textContent =
//                 student.message || "Unable to register student.";
//
//             return;
//         }
//
//         message.textContent =
//             `Registration successful! Student ID: ${student.id}`;
//
//         document.getElementById("name").value = "";
//         document.getElementById("email").value = "";
//         document.getElementById("registerNumber").value = "";
//
//     } catch (error) {
//
//         message.textContent =
//             "Unable to connect to server.";
//     }
// }