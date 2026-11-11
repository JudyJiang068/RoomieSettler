// =====================================
// 1. Switch between different pages
// =====================================

function showPage(pageName) {

    const pages = document.querySelectorAll(".page");

    pages.forEach(page => {
        page.classList.add("hidden");
    });

    document.getElementById(pageName)
        .classList.remove("hidden");

    document.getElementById("page-title")
        .textContent = pageName.charAt(0).toUpperCase()
        + pageName.slice(1);
}


// =====================================
// 2. Add a new bill
// =====================================

function addBill() {

    const billName = prompt("Enter bill name:");

    if (!billName || !billName.trim()) {
        return;
    }

    const billList = document.getElementById("bill-list");

    const item = document.createElement("li");

    item.textContent = billName.trim();

    billList.appendChild(item);
}


// =====================================
// 3. Connect to Spring Boot Backend
// =====================================

async function calculateSplit() {

    const amount = document.getElementById("bill-amount").value;

    const people = document.getElementById("people-count").value;

    const resultDiv = document.getElementById("split-result");

    // Validate input

    if (amount === "" || people === "") {
        resultDiv.textContent =
            "Please enter the bill amount and number of people.";
        return;
    }

    if (Number(amount) <= 0 || Number(people) <= 0 ||
        !Number.isInteger(Number(people))) {

        resultDiv.textContent =
            "Please enter a valid amount and number of people.";
        return;
    }

    resultDiv.textContent = "Calculating...";

    try {

        // Send request to Spring Boot

        const response = await fetch(
            `/api/calculator/split?amount=${encodeURIComponent(amount)}&people=${encodeURIComponent(people)}`
        );

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.error || "Calculation failed.");
        }

        // Display result

        resultDiv.innerHTML = `
            <h3>Calculation Result</h3>
            <p>Total Amount: $${Number(data.totalAmount).toFixed(2)}</p>
            <p>Number of People: ${data.numberOfPeople}</p>
            <h2>Each Person Pays: $${Number(data.eachPersonPays).toFixed(2)}</h2>
        `;

    } catch (error) {

        resultDiv.textContent =
            "Error: " + error.message;

        console.error(error);
    }
}