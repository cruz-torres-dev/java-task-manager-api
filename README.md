# 📋 Task Manager API

> A RESTful API for task management built with **Java 17** and **Spring Boot 3**. Designed as a portfolio project to demonstrate clean backend development practices.

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2-In--Memory%20DB-0000BB?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

---

## 📌 About the Project

This project is a **Task Manager REST API** that provides full CRUD functionality for managing tasks. It was built to showcase backend development skills relevant to a **Junior/Trainee Backend Developer** position, following industry best practices such as:

- **Clean Architecture** — Controller → Service → Repository layered design
- **DTO Pattern** — Separation between API contracts and database entities
- **Global Exception Handling** — Consistent, user-friendly error responses
- **Input Validation** — Prevents invalid data from reaching the database
- **Unit Testing** — Service layer tested with JUnit 5 and Mockito

---

## ✨ Features

| Feature | Description |
|---|---|
| ✅ Create tasks | Add new tasks with title, description, and priority |
| 📄 List all tasks | Retrieve all tasks ordered by creation date |
| 🔍 Filter by status | Get tasks filtered by PENDING, IN_PROGRESS, or COMPLETED |
| ✏️ Update tasks | Modify task title, description, and priority |
| 🗑️ Delete tasks | Remove tasks by ID |
| ☑️ Mark as completed | Dedicated endpoint to mark a task as done |
| ⚠️ Validation | Input validation with meaningful error messages |
| 🧪 Unit tests | Service layer tests with Mockito |
| 📦 Sample data | Pre-loaded demo data on startup |

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| **Java 17** | Programming language |
| **Spring Boot 3.3.5** | Application framework |
| **Spring Data JPA** | Database access and ORM |
| **H2 Database** | In-memory database for development |
| **Hibernate** | JPA implementation |
| **Lombok** | Reduces boilerplate code |
| **Jakarta Validation** | Input validation |
| **Maven** | Build and dependency management |
| **JUnit 5 + Mockito** | Unit testing |

---

## 📁 Project Structure

```
src/main/java/com/portfolio/taskmanager/
├── TaskManagerApplication.java          # Application entry point
├── config/
│   └── DataInitializer.java             # Loads sample data on startup
├── controller/
│   └── TaskController.java              # REST endpoints (HTTP layer)
├── dto/
│   ├── TaskRequest.java                 # Input DTO with validation
│   └── TaskResponse.java               # Output DTO
├── exception/
│   ├── ErrorResponse.java              # Standardized error format
│   ├── GlobalExceptionHandler.java     # Catches and handles all exceptions
│   └── TaskNotFoundException.java      # Custom 404 exception
├── model/
│   ├── Task.java                       # JPA Entity (database table)
│   ├── TaskStatus.java                 # Enum: PENDING, IN_PROGRESS, COMPLETED
│   └── Priority.java                   # Enum: LOW, MEDIUM, HIGH
├── repository/
│   └── TaskRepository.java            # Data access layer (JPA)
└── service/
    ├── TaskService.java               # Service interface
    └── impl/
        └── TaskServiceImpl.java       # Business logic implementation
```

---

## 🚀 How to Run

### Prerequisites

- **Java 17** or higher → [Download](https://adoptium.net/)
- **Maven 3.8+** → [Download](https://maven.apache.org/download.cgi) (or use the included wrapper)

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/task-manager-api.git
   cd task-manager-api
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **The API is now running at:**
   ```
   http://localhost:8080/api/v1/tasks
   ```

5. **Access the H2 Database Console (optional):**
   ```
   http://localhost:8080/h2-console
   JDBC URL: jdbc:h2:mem:taskdb
   Username: sa
   Password: (leave empty)
   ```

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/tasks` | Create a new task |
| `GET` | `/api/v1/tasks` | Get all tasks |
| `GET` | `/api/v1/tasks?status=PENDING` | Get tasks filtered by status |
| `GET` | `/api/v1/tasks/{id}` | Get a task by ID |
| `PUT` | `/api/v1/tasks/{id}` | Update a task |
| `DELETE` | `/api/v1/tasks/{id}` | Delete a task |
| `PATCH` | `/api/v1/tasks/{id}/complete` | Mark a task as completed |

---

## 📝 Sample JSON Requests

### ➕ Create a Task

```http
POST /api/v1/tasks
Content-Type: application/json
```

```json
{
    "title": "Learn Spring Boot",
    "description": "Complete the Spring Boot REST API tutorial",
    "priority": "HIGH"
}
```

**Response (201 Created):**
```json
{
    "id": 6,
    "title": "Learn Spring Boot",
    "description": "Complete the Spring Boot REST API tutorial",
    "status": "PENDING",
    "priority": "HIGH",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 📄 Get All Tasks

```http
GET /api/v1/tasks
```

**Response (200 OK):**
```json
[
    {
        "id": 1,
        "title": "Set up Spring Boot project",
        "description": "Initialize the project with Maven...",
        "status": "COMPLETED",
        "priority": "HIGH",
        "createdAt": "2024-01-15T09:00:00",
        "updatedAt": "2024-01-15T09:00:00"
    },
    {
        "id": 2,
        "title": "Implement REST API endpoints",
        "description": "Create CRUD endpoints...",
        "status": "IN_PROGRESS",
        "priority": "HIGH",
        "createdAt": "2024-01-15T09:01:00",
        "updatedAt": "2024-01-15T09:01:00"
    }
]
```

---

### 🔍 Filter Tasks by Status

```http
GET /api/v1/tasks?status=PENDING
```

---

### ✏️ Update a Task

```http
PUT /api/v1/tasks/1
Content-Type: application/json
```

```json
{
    "title": "Updated Task Title",
    "description": "Updated description with more details",
    "priority": "LOW"
}
```

**Response (200 OK):**
```json
{
    "id": 1,
    "title": "Updated Task Title",
    "description": "Updated description with more details",
    "status": "PENDING",
    "priority": "LOW",
    "createdAt": "2024-01-15T09:00:00",
    "updatedAt": "2024-01-15T11:00:00"
}
```

---

### 🗑️ Delete a Task

```http
DELETE /api/v1/tasks/1
```

**Response:** `204 No Content`

---

### ☑️ Mark as Completed

```http
PATCH /api/v1/tasks/2/complete
```

**Response (200 OK):**
```json
{
    "id": 2,
    "title": "Implement REST API endpoints",
    "description": "Create CRUD endpoints...",
    "status": "COMPLETED",
    "priority": "HIGH",
    "createdAt": "2024-01-15T09:01:00",
    "updatedAt": "2024-01-15T11:30:00"
}
```

---

### ⚠️ Validation Error Example

```http
POST /api/v1/tasks
Content-Type: application/json
```

```json
{
    "title": "",
    "description": "A task with an empty title"
}
```

**Response (400 Bad Request):**
```json
{
    "status": 400,
    "message": "Validation failed — title: Title is required and cannot be empty",
    "timestamp": "2024-01-15T10:45:00"
}
```

---

## 🧪 Running Tests

```bash
mvn test
```

The project includes unit tests for the service layer covering:
- Task creation (with and without priority)
- Retrieval of all tasks and by ID
- Update operations
- Deletion (existing and non-existing tasks)
- Status marking
- Status filtering (valid and invalid)

---

## 🏗️ Architecture Overview

```
Client (Postman, Frontend, etc.)
        │
        ▼
┌─────────────────────────┐
│     Controller Layer     │  ← Handles HTTP requests/responses
│   (TaskController.java)  │
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│      Service Layer       │  ← Contains business logic
│  (TaskServiceImpl.java)  │
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│    Repository Layer      │  ← Communicates with the database
│ (TaskRepository.java)    │
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│      H2 Database         │  ← In-memory storage
│    (tasks table)         │
└─────────────────────────┘
```

---

## 🔮 Possible Future Improvements

- [ ] Add pagination and sorting for large datasets
- [ ] Implement task categories or tags
- [ ] Add due dates with overdue notifications
- [ ] Switch to PostgreSQL/MySQL for persistent storage
- [ ] Add Spring Security with JWT authentication
- [ ] Implement Swagger/OpenAPI documentation
- [ ] Add Docker support for containerized deployment
- [ ] Implement integration tests with @SpringBootTest

---

## 👨‍💻 Author

Developed as a portfolio project for a **Junior/Trainee Backend Developer** position.

> **Purpose:** Demonstrates proficiency in Java, Spring Boot, REST API design, clean architecture, and testing — key skills for backend development roles at companies like **NTT DATA**.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
