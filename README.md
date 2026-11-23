[project_readme.md](https://github.com/user-attachments/files/35301128/project_readme.md)
# 💰 Expense Tracker Full-Stack (Spring Boot + Angular + Docker)
URL Project: https://roadmap.sh/projects/expense-tracker-api

A full-stack personal expense tracking application built with a **Java / Spring Boot** REST API, an **Angular** dark-mode frontend served via **Nginx**, and a multi-service **Docker Compose** setup including **PostgreSQL**, **RabbitMQ**, and **Adminer**.

---

## 📌 Overview

**Expense Tracker** helps manage personal expenses and categories with JWT authentication, self-service user registration directly from the UI, and streamlined local execution via Docker containers.

### ✨ Key Features
* **Authentication & User Registration**: Sign in with JWT, create new accounts directly from the authentication screen (`POST /users/save-users`), and automatically redirect to login with an expiration notice when a token expires.
* **Expense Management (CRUD)**: Create, list, update, and delete expenses, including `datetime-local` date and time handling.
* **Dynamic Categories**: Category dropdowns populated from backend category data when creating and editing expenses.
* **Dark-Mode Frontend**: Angular UI styled in dark mode and served statically by Nginx inside a container.
* **Containerized Stack**: Orchestrates the API, frontend, relational database, message broker, and database management UI with Docker Compose.

---

## 🏗️ Architecture & Project Structure

In the containerized setup, **Nginx** serves the built Angular static assets on port `80` (mapped to port `4200` on the host), while **Spring Boot** exposes the REST API and connects to **PostgreSQL** and **RabbitMQ** over the internal Docker network. **Adminer** provides a web UI for the database, and **RabbitMQ Management** exposes broker monitoring.

```text
expense-tracker-api/
├── docker/
│   └── Dockerfile                  # Spring Boot API image build
├── frontend/
│   ├── src/
│   │   └── app/
│   │       ├── services/           # Angular services (auth.ts, expenses.ts)
│   │       ├── app.ts / app.html   # Main component in Dark Mode
│   │       └── ...
│   ├── .dockerignore               # Ignores node_modules/dist/from host
│   ├── Dockerfile                  # Multi-stage Angular build (Node) + Nginx runtime
│   ├── nginx.conf                  # Nginx SPA / routing configuration
│   ├── package.json
│   └── angular.json
├── src/
│   └── main/
│       ├── java/com/roadmap/expense_tracker_api/
│       │   ├── config/             # SecurityConfig, CorsConfig, etc.
│       │   ├── controller/         # AuthController, UserController, CategoriesController, ExpensesController
│       │   ├── dto/                # Request/response and auth DTOs
│       │   ├── entity/             # JPA entities (User, Categories, Expenses)
│       │   ├── repository/         # Spring Data JPA repositories
│       │   └── service/            # Business logic, JWT, and services
│       └── resources/
│           └── application.yaml    # Application configuration
├── docker-compose.yaml             # Multi-service orchestration (backend, frontend, db, rabbitmq, adminer)
└── pom.xml
```

---

## 🛠️ Tech Stack

### Backend
* **Language & Framework**: Java 21 / Spring Boot 3
* **Security**: Spring Security & JWT (JSON Web Token)
* **Persistence**: Spring Data JPA & Hibernate
* **Messaging**: RabbitMQ

### Frontend
* **Framework**: Angular
* **Web Server**: Nginx (Alpine)
* **Language/Styling**: TypeScript, HTML5, CSS3 (Dark Mode)

### Infrastructure & Tooling
* **Containerization**: Docker & Docker Compose
* **Database**: PostgreSQL 16
* **DB Client**: Adminer

---

## 🚀 Getting Started

### Prerequisites
* **Docker** and **Docker Compose** installed on your system.

---

### Option 1: Run with Docker Compose (Recommended)

1. **Clone the repository and switch to the `dev` branch:**
   ```bash
   git clone https://github.com/MatheusAlvesSilvaCode/expense-tracker-api.git
   cd expense-tracker-api
   git checkout dev
   ```

2. **Build and start all services:**
   ```bash
   docker compose up --build -d
   ```

3. **View container status and logs (optional):**
   ```bash
   docker compose ps
   docker compose logs -f
   ```

4. **Access the services:**

   | Service | Purpose | Default URL / Address |
   | :--- | :--- | :--- |
   | **Frontend (Angular + Nginx)** | Web UI | `http://localhost:4200` |
   | **Backend (Spring Boot API)** | REST API endpoints | `http://localhost:8080` |
   | **PostgreSQL** | Relational database | `localhost:5434` *(host port)* |
   | **Adminer** | Web database client | `http://localhost:8090` |
   | **RabbitMQ Management** | Broker management UI | `http://localhost:15672` |

5. **Stop the containers:**
   ```bash
   docker compose down
   ```

---

### Option 2: Run Locally (Without Docker)

1. **Start PostgreSQL and RabbitMQ** (via Docker or local installations) and ensure `src/main/resources/application.yaml` matches your local environment connection settings.
2. **Start the backend (Spring Boot):**
   ```bash
   ./mvnw spring-boot:run
   ```
   *(On Windows PowerShell, use `.\mvnw.cmd spring-boot:run` or `mvn spring-boot:run`).*
3. **Start the frontend (Angular):**
   ```bash
   cd frontend
   npm ci
   npx ng serve
   ```
   Open `http://localhost:4200` in your browser.

---

## 🔒 Configuration & CORS

* **CORS (`CorsConfig`)**: Configured on the backend to allow development and containerized origins:
  * `http://localhost:4200`
  * `http://localhost`
  * `http://localhost:80`
* **Environment Variables / `application.yaml`**: Ensure connection strings for PostgreSQL (`spring.datasource.url`), JWT secret, and RabbitMQ parameters are appropriately set.

---

## 🛠️ API Endpoints Summary

| Area | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/auth/login` | Authenticates user and returns a JWT token |
| **Users** | `POST` | `/users/save-users` | Registers a new user from the UI |
| **Categories** | `GET` | `/categories` | Lists available expense categories |
| **Expenses** | `GET` | `/expenses` | Lists expenses for the authenticated user |
| **Expenses** | `POST` | `/expenses` | Creates a new expense (with date/time and category) |
| **Expenses** | `PUT` | `/expenses/{id}` | Updates an existing expense |
| **Expenses** | `DELETE` | `/expenses/{id}` | Deletes an expense |

---

## ⚡ Troubleshooting

* **Port already allocated (`Bind for ... failed: port is already allocated`)**: An existing local process or container is using ports such as `5434` or `8090`. Stop/remove the container using `docker rm -f <container_name>` or adjust the host port mapping on the left side of `docker-compose.yaml`.
* **Windows binaries error (`node.exe: not found`) during frontend build**: Make sure `frontend/.dockerignore` excludes `node_modules`, `.angular`, and `dist` so Linux binaries are installed cleanly inside the container during `npm ci`.
* **Rebuilding after changes**: Run `docker compose up --build -d` to compile and deploy updated code.

---

## 👨‍💻 Author

Developed by **Matheus Alves da Silva**

GitHub: [@MatheusAlvesSilvaCode](https://github.com/MatheusAlvesSilvaCode)
