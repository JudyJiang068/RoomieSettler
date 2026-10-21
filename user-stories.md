# E1: Expense Creation and Calculation

## US1: Create Shared Expenses

As a payer, I want to create a shared expense list so that I can record shared expenses.

FR: FR1  
Priority: Must  
MVP: yes

Acceptance Criteria:
- [ ] The user can enter the expense name, number, and date.
- [ ] The user can identify everyone's cost.

## US2: Add a New Participant into a List

As a payer, I want to add a new participant into a list so that the participant can share the expense.

FR: FR2  
Priority: Should  
MVP: yes

Acceptance Criteria:
- [ ] The user can create a new item in an expense list.
- [ ] The user can enter the expense name, number, and date of the item.

## US3: Enter Receipt Items

As a payer, I want to import a receipt so that I can record the items in the receipt.

FR: FR3  
Priority: Must  
MVP: yes

Acceptance Criteria:
- [ ] The user can import every item's name, price, and tips.
- [ ] The system records the item information correctly.
- [ ] The user can import at most 100 items at a time.

## US4: Assign Everyone's Cost

As a payer, I want to assign everyone's cost so that each person is responsible for the correct items.

FR: FR4  
Priority: Must  
MVP: yes

Acceptance Criteria:
- [ ] The user can choose the owner of each cost and record the assignment.
- [ ] The user can clearly view each item's assignment.

## US5: Calculate Subtotals of Receipts

As a payer, I want to calculate the subtotals of receipts so that I can see the cost before tax.

FR: FR5  
Priority: Must  
MVP: yes

Acceptance Criteria:
- [ ] The system calculates the subtotal for each item.
- [ ] The system displays the subtotal for each item.
- [ ] The calculated amount is accurate to $0.01.

## US6: Calculate the Tax

As a payer, I want to calculate the tax so that tax can be included in the shared expense.

FR: FR6  
Priority: Should  
MVP: yes

Acceptance Criteria:
- [ ] The user can enter the tax amount and the system records it.
- [ ] The user can choose a tax assignment method.
- [ ] The system calculates the tax correctly.

## US7: Calculate Individual Totals

As a payer, I want to calculate individual totals so that each participant knows how much they owe.

FR: FR7  
Priority: Must  
MVP: yes

Acceptance Criteria:
- [ ] The system calculates each person's shared cost and tax correctly.
- [ ] The system displays each person's total.
- [ ] The calculated amount is accurate to $0.01.

# E2: Expense Review and Conversion

## US8: Convert the Currency

As a payer, I want to convert the currency so that participants can view expenses in different currencies.

FR: FR8  
Priority: Could  
MVP: yes

Acceptance Criteria:
- [ ] The user can choose the currencies.
- [ ] The system obtains the exchange rate and calculates the converted amount.
- [ ] The system displays the conversion result.

## US9: Generate Participant Debts

As a payer, I want to generate participant debts so that everyone knows who owes money.

FR: FR9  
Priority: Should  
MVP: yes

Acceptance Criteria:
- [ ] The system calculates each person's debts.
- [ ] The system displays the calculated debts.
- [ ] The system identifies the payer and the other participants.

## US10: Simplify Transfers

As a payer, I want to simplify transfers so that participants can settle debts with fewer transactions.

FR: FR10  
Priority: Should  
MVP: no

Acceptance Criteria:
- [ ] The system combines multiple transfers when possible.
- [ ] The system displays the simplified transfers.

## US11: View Expense Details

As a participant, I want to view expense details so that I can understand how the expense was calculated.

FR: FR11  
Priority: Must  
MVP: yes

Acceptance Criteria:
- [ ] The system displays expense information, tax, and receipt details.
- [ ] The system displays subtotals and individual totals.
- [ ] The system displays the final debts.

## US12: Edit an Expense

As a payer, I want to edit an expense so that I can correct mistakes.

FR: FR12  
Priority: Should  
MVP: yes

Acceptance Criteria:
- [ ] The user can correct item information and assignments.
- [ ] The system notifies everyone about the change.
- [ ] The system recalculates the results.

## US13: Mark an Expense as Paid

As a participant, I want to mark an expense as paid so that everyone knows it has been settled.

FR: FR13  
Priority: Could  
MVP: no

Acceptance Criteria:
- [ ] The user can mark an expense with a symbol indicating that it is paid.
- [ ] The system records the payment status.
- [ ] Other users can see the payment status.

## US14: View Expense History

As a participant, I want to view expense history so that I can review previous expenses.

FR: FR14  
Priority: Could  
MVP: no

Acceptance Criteria:
- [ ] The system displays previous expenses.
- [ ] The user can select a historical expense to view its details.
