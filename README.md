# To-Do Application (Spring Boot + Vite + MySQL + Docker)

This is a full-stack To-Do application built with:

- **Backend:** Spring Boot (Java 25 , Spring Boot 4)
- **Frontend:** Vite + React 
- **Styling:** Tailwind CSS 
- **Database:** MySQL 8
- **Containerization:** Docker & Docker Compose

## 🧱 Project Structure

```text
to-do-application/
  backend/          # Spring Boot API
    Dockerfile
    pom.xml
    src/
  front-end/        # Vite + React frontend
    Dockerfile
    nginx.conf
    package.json
    src/
  docker-compose.yml
```
## 🚀 Prerequisites

To run this app, you only need:
  - Docker Desktop installed and running
  - Git (to clone the repo)

## How to Run the App

1. Clone the repository:
   ```bash
   git clone https://github.com/Kalana-indula/Task-App-Full-Stack.git
   cd Task-App-Full-Stack-master
   ```
2. Build and start all services (backend, frontend, MySQL):
  ```bash
  docker compose up --build
  ```
3. Open the app:
   - Frontend UI: http://localhost:3000
   - Backend API (direct): http://localhost:8080/api/tasks
To stop everything, press Ctrl + C in the terminal, then run:
  ```bash
  docker compose down
  ```
### 🗄️ MySQL (Database)
## 🧩 Services Overview
- Image: `mysql:8.0`
- Database name: `todoApp`
- User: `todo_user`
- Password: `todo_pass`
- Runs **inside Docker only** (not exposed to the host by default)

### ⚙️ Backend (Spring Boot)
- Path: `backend/`
- Exposed at: `http://localhost:8080`
- Main endpoints (example):
    - `GET /api/tasks` 
    - `GET /api/tasks/recent`
    - `POST /api/tasks`
    - `PUT /api/tasks/{id}/complete`
 
Configuration in Docker is passed via environment variables in `docker-compose.yml`:
  ```yaml
  SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/todoApp?createDatabaseIfNotExist=true
  SPRING_DATASOURCE_USERNAME: todo_user
  SPRING_DATASOURCE_PASSWORD: todo_pass
  ```
### 🎨 Frontend (Vite + Nginx)
- Path: `front-end/` 
- Built with Node, served by Nginx
- Exposed at: `http://localhost:3000`
Nginx is configured to:
- Serve the static frontend
- Proxy `/api` requests to the backend service:

  ```nginx
  location /api/ {
      proxy_pass http://backend:8080/api/;
  }
  ```
This allows the frontend to simply call `/api/...` without hardcoding backend URLs.

## Video 
This video shows how the app is run an used after creating a container
https://www.youtube.com/watch?v=4hk8Za5UOBU

## UI of the app
![Screenshot_10-12-2025_155631_localhost](https://github.com/user-attachments/assets/ae882213-00d8-4a8d-9674-7b6e0cf6229b)
![Screenshot_10-12-2025_15562_localhost](https://github.com/user-attachments/assets/cb836bb9-5569-46a3-ac3e-01f8ddf495ac)

UI is completely responsive

## 📦Testing 

## 🔧 Backend Testing (Spring Boot + JUnit + Mockito)

The backend contains the following test layers:

### ✔️ **1. Unit Tests (Service Layer)**
These tests validate:
- Business logic in `TaskServiceImpl`
- Completing a task
- Recent task retrieval
- Error handling (e.g., `TaskNotFoundException`)
- Validation scenarios (empty title/description)

### ✔️ **2. Controller Tests (WebMvcTest)**
These tests verify:
- Request → Controller → Response flow
- HTTP status codes
- Validation errors using `@Valid`
- Proper JSON response structure
- Successful and failed request scenarios

### ✔️ **3. Integration Tests (SpringBootTest + MockMvc)**
These tests run against an **H2 in-memory database** and fully boot the Spring context.
Coverage includes:
- Creating tasks (POST /api/tasks)
- Fetching recent tasks
- Completing tasks
- Error scenarios (`404` for missing tasks)

This ensures the entire Spring Boot application behaves correctly end-to-end.

## 🎨 Frontend Testing (Vite + React)
### ✔️ **Component-level tests**
- Testing UI behavior for task creation inputs
- Ensuring empty validation messages appear correctly
- Verifying that the task list renders correctly
- Confirming task completion events fire as expected

### ✔️ **Integration-level tests**
- Testing API interaction using mocked endpoints
- Ensuring frontend reacts correctly to backend responses
- Edge case handling (empty task list UI state)

Frontend tests ensure the UI remains reliable and stable across changes.

**All the test codes are included in the repository**

## 🔧 Useful Docker Commands
Start the stack:

  ```bash
  docker compose up --build
  ```
Start in the background:
  ```bash
  docker compose up -d
  ```
Stop containers:
  ```bash
  docker compose down
  ```
Stop containers and remove DB data:
  ```bash
  docker compose down -v
  ```
View logs:
  ```bash
  docker compose logs -f
  ```
## 🧪 Running Locally Without Docker
### Backend
Make sure `application.properties` points to your local MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/todoApp?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
```
### Frontend
```bash
cd front-end
npm install
npm run dev
```
## 📝 Notes
- This app is designed to be **easy to spin up** for testing or demos.
- Feel free to fork and extend (authentication, more fields, filters, etc.).

    


