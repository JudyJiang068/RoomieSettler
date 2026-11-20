
# RoomieSettler

## 1. Project Overview

RoomieSettler is a shared-expense application designed to help students, roommates, international students, and travel groups manage shared bills and expenses.

When people share bills, some expenses are divided equally among everyone, some are shared by only selected members, and others are paid individually. Calculating these expenses manually can be time-consuming and error-prone.

RoomieSettler simplifies this process by organizing bills, managing individual expense items, calculating expense shares, and helping users understand how much each person owes.

The application provides a web-based interface, a Java Spring Boot backend, and MySQL database integration to support expense management and calculations.

## 2. Tech Stack

| Area | Technology | Current use |
| --- | --- | --- |
| Frontend | HTML, CSS, JavaScript | Browser-based interface |
| Backend | Java 17, Spring Boot 3.5.6 | Local REST API and calculation logic |
| Database | MySQL, Spring JDBC, JdbcTemplate | Database operations |
| Build tool | Maven | Backend dependencies and execution |
| API | HTTP / REST (JSON) | Browser-to-backend communication |
| IDE | IntelliJ IDEA | Development environment |
| Version Control | Git and GitHub | Source control and collaboration |
| Hosting | Local computer, port 8080 | Local application execution |

The technology choices support the current browser-based interface, Java backend, database operations, and local development workflow.

## 3. System Architecture

RoomieSettler follows a **modular architecture** designed around six subsystems:

1. **User Interface** — receives expense and participant input and displays results.
2. **Application Coordination** — coordinates user requests and business operations.
3. **Bill & Expense Management** — manages bills, expense items, and splitting rules.
4. **Calculation & Settlement** — calculates participant shares and settlement results.
5. **Currency Conversion** — applies exchange rates and currency rounding.
6. **Data Management** — stores expense, participant, and historical data.

### Subsystem/Component Diagram

![Subsystem diagram](docs/subsystem-diagram.png)

### Deployment Diagram

![Deployment diagram](docs/deployment-diagram.png)

**Current deployment:** The web browser and Spring Boot/Tomcat backend run locally. The browser communicates with the backend using HTTP. The backend uses MySQL for database operations. The deployment diagram shows the intended placement of all six subsystems. Some advanced functions, including live currency conversion and cloud deployment, remain planned improvements.

## 4. Repository Structure and Subsystem Mapping

```text
RoomieSettler/
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── script.js
│
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── com/
│       │   │       └── financialcalculator/
│       │   │           ├── CalculatorApplication.java
│       │   │           ├── controller/
│       │   │           │   └── CalculatorController.java
│       │   │           ├── service/
│       │   │           │   └── CalculatorService.java
│       │   │           └── model/
│       │   │               └── SplitResponse.java
│       │   │
│       │   └── resources/
│       │       ├── application.properties
│       │       └── static/
│       │
│       └── test/
│
├── docs/
│   ├── subsystem-diagram.png
│   ├── deployment-diagram.png
│   └── screenshots/
│       ├── backend-running.png
│       └── frontend-integration.png
│
├── README.md
└── user-stories.md
```

| Subsystem | Current location / implementation | Status |
| --- | --- | --- |
| User Interface | `frontend/` and `backend/src/main/resources/static/` | Initial UI implemented |
| Application Coordination | `backend/src/main/java/com/financialcalculator/controller/` | API request handling implemented |
| Calculation & Settlement | `backend/src/main/java/com/financialcalculator/service/` and `model/` | Expense calculation and splitting implemented |
| Bill & Expense Management | Bill creation, bill retrieval, and expense item management functionality | Core functionality implemented |
| Currency Conversion | UI placeholder; no conversion service | Planned |
| Data Management | MySQL database and Spring JDBC (`JdbcTemplate`) | Database integration implemented |

The Java `controller`, `service`, and `model` packages represent the current technical implementation structure. They do not correspond to six fully independent subsystem modules. The subsystem mapping describes the system's logical architecture and its current implementation status. Additional modules or folders may be introduced as development continues.

## 5. How to Run Locally

### Prerequisites

Before running the application, make sure you have installed:
- JDK 17
- IntelliJ IDEA.
- MySQL Server and MySQL Workbench
- Maven (or IntelliJ IDEA's built-in Maven)
- Git
- A modern web browser

### Clone and start

```bash
git clone https://github.com/JudyJiang068/RoomieSettler.git
cd RoomieSettler
```

### Set Up the Database

1.Start MySQL Server.
2.Open MySQL Workbench.
3.Create the project database:
```
CREATE DATABASE roomiesettler;
```
4.If database initialization SQL scripts are provided in the project, execute them to create the required tables.

### Configure the Database Connection

Configure the database connection using environment variables.

Example application.properties configuration:

```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Set the following environment variables in your IntelliJ IDEA run configuration:

```
DB_URL=jdbc:mysql://localhost:3306/roomiesettler
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
```

Important: Replace the example values with your own MySQL credentials. Never commit actual passwords or other sensitive information to GitHub.

### Run the Backend

1.Open the project in IntelliJ IDEA.
2.Open the backend directory.
3.Locate CalculatorApplication.java.
4.Run the Spring Boot application.

The backend uses port 8080 by default unless configured otherwise.

### Run the Frontend

1.Open the frontend directory.
2.Run or serve index.html using your preferred local web server.
3.Make sure the frontend API configuration points to the correct backend address.

### Verify the Backend API

After starting the backend, verify that the application is running by accessing the appropriate API endpoint.

For example, if the calculation endpoint is available, open:

```text
http://localhost:8080/api/calculator/split?amount=100&people=4
```

The expected result is a JSON response containing the total amount, participant count, and calculated share for each person.

The exact endpoint and response should be verified against the current backend implementation.

## 6. API Endpoints

The backend provides REST API endpoints for bill management and expense sharing.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/bills` | Retrieve all bills |
| POST | `/api/bills` | Create a bill |
| GET | `/api/bills/{billId}` | Retrieve a specific bill |
| POST | `/api/bills/{billId}/items` | Add an expense item to a bill |
| GET | `/api/bills/{billId}/items` | Retrieve expense items associated with a bill |
| POST | `/api/items/{itemId}/shares` | Create expense shares |
| GET | `/api/items/{itemId}/shares` | Retrieve expense shares |

**Note:** Request formats and response structures should be verified against the current controller implementations.

## 7. Testing and Running Evidence

### Testing

The project can be tested using:

- Manual API testing with Postman.
- Database verification with MySQL Workbench.
- Automated tests using JUnit and Spring Boot Test.

Important test scenarios include:

- Creating and retrieving bills.
- Adding expense items to bills.
- Validating expense-sharing amounts.
- Handling invalid input.
- Handling nonexistent bill or item IDs.
- Verifying individual expense calculations.

### Running Evidence
Initial screenshots are stored at:

- [Backend running](docs/screenshots/backend-running.png)
- [Frontend integration](docs/screenshots/frontend-integration.png)

![Backend running](docs/screenshots/backend-running.png)

![Frontend integration](docs/screenshots/frontend-integration.png)

Each team member should independently clone and run the project to verify that the setup instructions work on their computer.

## 8. Team Collaboration and Future Improvements

### Team Collaboration

RoomieSettler is developed collaboratively using Git and GitHub.

Team members contribute through:

- Backend development.
- Frontend development.
- Database design and validation.
- API testing and debugging.
- Documentation and project maintenance.

Feature branches and Pull Requests are used to organize code changes and facilitate collaboration.

### Future Improvements

Potential future enhancements include:

- Multi-currency expense conversion (USD to RMB).
- Group and participant management.
- More flexible expense-sharing rules.
- Automatic settlement suggestions.
- Cloud database integration.
- Secure user authentication and authorization.
- Deployment for remote access.

### Security and Development Notes

Do not commit passwords, API keys, or other secrets to the repository.

Use environment variables to configure database credentials. If environment variables are introduced, provide an `.env.example` file without real credentials and add local `.env` files to `.gitignore`.

