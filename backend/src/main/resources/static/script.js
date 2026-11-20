
// =====================================
// 1. Global data
// =====================================

let currentBills = [];
let shareRows = [];

// =====================================
// 2. Switch between pages
// =====================================

function showPage(pageName) {
    document.querySelectorAll(".page").forEach(page => {
        page.classList.add("hidden");
    });

    const page = document.getElementById(pageName);

    if (page) {
        page.classList.remove("hidden");
    }

    const title = document.getElementById("page-title");

    if (title) {
        title.textContent =
            pageName.charAt(0).toUpperCase() + pageName.slice(1);
    }

    if (pageName === "bills" || pageName === "settlement") {
        loadBills();
    }
}

// =====================================
// 3. Create a new bill
// =====================================

async function createBill() {
    const title = document.getElementById("bill-title").value.trim();
    const currency = document.getElementById("bill-currency").value;
    const result = document.getElementById("create-bill-result");

    if (!title) {
        result.textContent = "Please enter a bill name.";
        return;
    }

    result.textContent = "Creating bill...";

    try {
        const response = await fetch("/api/bills", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                title: title,
                currency: currency
            })
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.error || "Failed to create bill.");
        }

        result.textContent = "Bill created successfully!";

        document.getElementById("bill-title").value = "";

        await loadBills();

    } catch (error) {
        result.textContent = "Error: " + error.message;
        console.error(error);
    }
}

// =====================================
// 4. Load all bills
// =====================================

async function loadBills() {
    try {
        const response = await fetch("/api/bills");

        if (!response.ok) {
            throw new Error("Could not load bills.");
        }

        currentBills = await response.json();

        const billList = document.getElementById("bill-list");

        if (billList) {
            billList.replaceChildren();

            currentBills.forEach(bill => {
                const li = document.createElement("li");

                li.textContent =
                    `${bill.title} - ${bill.currency} ${Number(bill.amount).toFixed(2)}`;

                billList.appendChild(li);
            });
        }

        const billSelects = [
            document.getElementById("selected-bill"),
            document.getElementById("share-bill"),
            document.getElementById("total-bill")
        ];

        billSelects.forEach(select => {
            if (!select) return;

            const previousValue = select.value;

            select.replaceChildren();

            const defaultOption = document.createElement("option");
            defaultOption.value = "";
            defaultOption.textContent = "Select a bill";
            select.appendChild(defaultOption);

            currentBills.forEach(bill => {
                const option = document.createElement("option");
                option.value = bill.id;
                option.textContent = `${bill.title} (${bill.currency})`;
                select.appendChild(option);
            });

            if (currentBills.some(bill =>
                String(bill.id) === previousValue
            )) {
                select.value = previousValue;
            }
        });

        const dashboardBills = document.getElementById("dashboard-bills");

        if (dashboardBills) {
            dashboardBills.textContent = currentBills.length;
        }

        const dashboardTotal = document.getElementById("dashboard-total");

        if (dashboardTotal) {
            const total = currentBills.reduce(
                (sum, bill) => sum + Number(bill.amount),
                0
            );

            dashboardTotal.textContent = "$" + total.toFixed(2);
        }

    } catch (error) {
        console.error("Load bills error:", error);
    }
}

// =====================================
// 5. Add a receipt item
// =====================================

async function addItem() {
    const billId = document.getElementById("selected-bill").value;
    const itemName = document.getElementById("item-name").value.trim();
    const amount = document.getElementById("item-amount").value;

    const result = document.getElementById("add-item-result");

    if (!billId || !itemName || !amount) {
        result.textContent = "Please complete all fields.";
        return;
    }

    if (!Number.isFinite(Number(amount)) || Number(amount) <= 0) {
        result.textContent = "Please enter a valid amount.";
        return;
    }

    result.textContent = "Adding item...";

    try {
        const response = await fetch(`/api/bills/${billId}/items`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                itemName: itemName,
                amount: Number(amount)
            })
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.error || "Failed to add item.");
        }

        result.textContent = "Item added successfully!";

        document.getElementById("item-name").value = "";
        document.getElementById("item-amount").value = "";

        await loadItems();
        await loadBills();

    } catch (error) {
        result.textContent = "Error: " + error.message;
        console.error(error);
    }
}

// =====================================
// 6. Load receipt items
// =====================================

async function loadItems() {
    const billId = document.getElementById("selected-bill").value;
    const itemList = document.getElementById("item-list");

    itemList.replaceChildren();

    if (!billId) {
        itemList.textContent = "Please select a bill.";
        return;
    }

    try {
        const response = await fetch(`/api/bills/${billId}/items`);

        if (!response.ok) {
            throw new Error("Could not load receipt items.");
        }

        const items = await response.json();

        if (items.length === 0) {
            itemList.textContent = "No receipt items yet.";
        }

        items.forEach(item => {
            const li = document.createElement("li");

            li.textContent =
                `${item.itemName || item.item_name} - $${Number(item.amount).toFixed(2)}`;

            itemList.appendChild(li);
        });

        if (document.getElementById("share-bill").value === billId) {
            updateShareItemOptions(items);
        }

    } catch (error) {
        itemList.textContent = "Error: " + error.message;
        console.error(error);
    }
}

// =====================================
// 7. Load items for expense sharing
// =====================================

async function loadShareItems() {
    const billId = document.getElementById("share-bill").value;
    const itemSelect = document.getElementById("share-item");

    itemSelect.replaceChildren();

    const defaultOption = document.createElement("option");
    defaultOption.value = "";
    defaultOption.textContent = "Select an item";
    itemSelect.appendChild(defaultOption);

    if (!billId) return;

    try {
        const response = await fetch(`/api/bills/${billId}/items`);

        if (!response.ok) {
            throw new Error("Could not load items.");
        }

        const items = await response.json();
        updateShareItemOptions(items);

    } catch (error) {
        console.error(error);
    }
}

function updateShareItemOptions(items) {
    const itemSelect = document.getElementById("share-item");
    const previousValue = itemSelect.value;

    itemSelect.replaceChildren();

    const defaultOption = document.createElement("option");
    defaultOption.value = "";
    defaultOption.textContent = "Select an item";
    itemSelect.appendChild(defaultOption);

    items.forEach(item => {
        const option = document.createElement("option");
        option.value = item.id;

        option.textContent =
            `${item.itemName || item.item_name} - $${Number(item.amount).toFixed(2)}`;

        itemSelect.appendChild(option);
    });

    if (items.some(item => String(item.id) === previousValue)) {
        itemSelect.value = previousValue;
    }
}

// =====================================
// 8. Add member share
// =====================================

function addShareRow() {
    const memberName =
        document.getElementById("member-name").value.trim();

    const shareAmount =
        document.getElementById("share-amount").value;

    const result = document.getElementById("share-result");

    if (!memberName || shareAmount === "") {
        result.textContent = "Please enter member name and amount.";
        return;
    }

    const amount = Number(shareAmount);

    if (!Number.isFinite(amount) || amount < 0) {
        result.textContent = "Please enter a valid share amount.";
        return;
    }

    if (shareRows.some(row =>
        row.memberName.toLowerCase() === memberName.toLowerCase()
    )) {
        result.textContent = "This member has already been added.";
        return;
    }

    shareRows.push({
        memberName: memberName,
        shareAmount: amount.toFixed(2)
    });

    document.getElementById("member-name").value = "";
    document.getElementById("share-amount").value = "";

    renderShareRows();
}

function renderShareRows() {
    const shareList = document.getElementById("share-list");

    shareList.replaceChildren();

    shareRows.forEach((row, index) => {
        const li = document.createElement("li");

        li.textContent =
            `${row.memberName}: $${Number(row.shareAmount).toFixed(2)} `;

        const removeButton = document.createElement("button");
        removeButton.textContent = "Remove";

        removeButton.onclick = () => {
            shareRows.splice(index, 1);
            renderShareRows();
        };

        li.appendChild(removeButton);
        shareList.appendChild(li);
    });
}

// =====================================
// 9. Save expense shares
// =====================================

async function saveShares() {
    const itemId = document.getElementById("share-item").value;
    const result = document.getElementById("share-result");

    if (!itemId) {
        result.textContent = "Please select a receipt item.";
        return;
    }

    if (shareRows.length === 0) {
        result.textContent = "Please add at least one member.";
        return;
    }

    result.textContent = "Saving shares...";

    try {
        const response = await fetch(`/api/items/${itemId}/shares`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                shares: shareRows
            })
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.error || "Failed to save shares.");
        }

        result.textContent = "Shares saved successfully!";

        shareRows = [];
        renderShareRows();

    } catch (error) {
        result.textContent = "Error: " + error.message;
        console.error(error);
    }
}

// =====================================
// 10. Calculate individual totals
// =====================================

async function loadTotals() {
    const billId = document.getElementById("total-bill").value;
    const totalList = document.getElementById("total-list");

    totalList.replaceChildren();

    if (!billId) {
        totalList.textContent = "Please select a bill.";
        return;
    }

    try {
        const response = await fetch(
            `/api/calculator/totals?billId=${billId}`
        );

        if (!response.ok) {
            throw new Error("Could not calculate totals.");
        }

        const totals = await response.json();

        if (totals.length === 0) {
            totalList.textContent = "No assigned expenses yet.";
            return;
        }

        totals.forEach(member => {
            const li = document.createElement("li");

            const name = member.memberName || member.member_name;
            const amount = member.totalAmount ?? member.total_amount;

            li.textContent = `${name}: $${Number(amount).toFixed(2)}`;

            totalList.appendChild(li);
        });

    } catch (error) {
        totalList.textContent = "Error: " + error.message;
        console.error(error);
    }
}

// =====================================
// 11. Equal split calculator
// =====================================

async function calculateSplit() {
    const amount = document.getElementById("bill-amount").value;
    const people = document.getElementById("people-count").value;
    const resultDiv = document.getElementById("split-result");

    if (amount === "" || people === "") {
        resultDiv.textContent =
            "Please enter the bill amount and number of people.";
        return;
    }

    if (Number(amount) <= 0 ||
        Number(people) <= 0 ||
        !Number.isInteger(Number(people))) {
        resultDiv.textContent =
            "Please enter a valid amount and number of people.";
        return;
    }

    resultDiv.textContent = "Calculating...";

    try {
        const response = await fetch(
            `/api/calculator/split?amount=${encodeURIComponent(amount)}&people=${encodeURIComponent(people)}`
        );

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.error || "Calculation failed.");
        }

        resultDiv.replaceChildren();

        const heading = document.createElement("h3");
        heading.textContent = "Calculation Result";

        const total = document.createElement("p");
        total.textContent =
            `Total Amount: $${Number(data.totalAmount).toFixed(2)}`;

        const count = document.createElement("p");
        count.textContent =
            `Number of People: ${data.numberOfPeople}`;

        const each = document.createElement("h2");
        each.textContent =
            `Each Person Pays: $${Number(data.eachPersonPays).toFixed(2)}`;

        resultDiv.append(heading, total, count, each);

    } catch (error) {
        resultDiv.textContent = "Error: " + error.message;
        console.error(error);
    }
}

// =====================================
// 12. Initialize page
// =====================================

document.addEventListener("DOMContentLoaded", () => {
    loadBills();

    document.getElementById("selected-bill")
        ?.addEventListener("change", loadItems);

    document.getElementById("share-bill")
        ?.addEventListener("change", () => {
            shareRows = [];
            renderShareRows();
            loadShareItems();
        });

    document.getElementById("share-item")
        ?.addEventListener("change", () => {
            shareRows = [];
            renderShareRows();
        });
});
