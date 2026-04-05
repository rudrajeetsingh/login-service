# login-service

> A Spring Boot monolithic login service providing a simple authentication REST API and a browser-based login UI.

![Java 17](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)
![Spring Boot 3.4.4](https://img.shields.io/badge/Spring%20Boot-3.4.4-6DB33F?logo=springboot&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-Build%20Tool-02303A?logo=gradle&logoColor=white)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Browser UI](#browser-ui)
- [Building](#building)
- [Testing](#testing)
- [Security Notes](#security-notes)
- [Future Improvements](#future-improvements)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

`login-service` is a lightweight Spring Boot monolithic application that exposes a REST API for user authentication and serves a browser-based login form. It accepts a username and password via an HTTP POST request, validates the credentials, and returns a success or failure response. The project is intended as a starting point or learning reference for building authentication flows in Spring Boot.

---

## Architecture

The following diagram illustrates the end-to-end request flow from the browser UI through the Spring Boot application:

```
                        ┌────────────────────────────┐
                        │      User (Browser)        │
                        │  [HTML + JavaScript UI]    │
                        └────────────┬───────────────┘
                                     │
                      Enter Username + Password
                                     │
                                     ▼
                         HTTP POST: /api/auth/login
                         Content-Type: application/json
                         Body: { "username": "...", "password": "..." }

                                     │
                                     ▼
┌───────────────────────────────────────────────────────────────┐
│                    Spring Boot Application                    │
│                      (login-service)                          │
│                                                               │
│ ┌────────────────┐     ┌───────────────────────┐              │
│ │  AuthController│────▶│   AuthService         │              │
│ │  (REST API)    │     │  (Business Logic)     │              │
│ └────────────────┘     └───────────────────────┘              │
│           │                          │                        │
│           ▼                          ▼                        │
│   Validate Credentials    [Optional] Encrypt Password         │
│   Return Result String     (e.g., using BCrypt)               │
│                                                               │
│ ┌────────────────────────────┐     ┌───────────────────────┐  │
│ │   UserRepository (Optional)│<────│   Database (Optional) │  │
│ └────────────────────────────┘     └───────────────────────┘  │
└───────────────────────────────────────────────────────────────┘
                                     ▲
                                     │
                         Returns Success or Failure
                                     │
                                     ▼
                        ┌────────────────────────────┐
                        │  UI Shows Response Message │
                        └────────────────────────────┘
```

### Key Classes

| Class | Package | Role |
|---|---|---|
| `LoginController` | `com.monolithicservice.login.controller` | REST controller that handles `POST /api/auth/login` and maps HTTP request/response |
| `LoginService` | `com.monolithicservice.login.service` | Business logic layer; validates credentials and returns an authentication result string |
| `User` | `com.monolithicservice.login.model` | Plain POJO representing the login request payload with `username` and `password` fields |
| `SecurityConfig` | `config` | Holds the (currently commented-out) `SecurityFilterChain` bean; Spring Security is disabled |

---

## Prerequisites

Before running this project, ensure you have the following installed:

- **Java 17+** — [Download from Adoptium](https://adoptium.net/)
  ```bash
  java -version
  # Expected: openjdk version "17.x.x" or higher
  ```

- **Gradle 7+** — or use the included Gradle wrapper (no separate install required)
  ```bash
  gradle -version
  # Expected: Gradle 7.x or higher
  # Alternatively, use ./gradlew -version (no install needed)
  ```

- **Git** — for cloning the repository
  ```bash
  git --version
  # Expected: git version 2.x.x or higher
  ```

---

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-org/login-service.git
```

### 2. Navigate into the Project Directory

```bash
cd login-service
```

### 3. Run the Application

```bash
./gradlew bootRun
```

The application will start on port **8888**. You should see output similar to:

```
Started LoginServiceApplication in X.XXX seconds (process running for X.XXX)
```

### 4. Access the Browser UI

Open your browser and navigate to:

```
http://localhost:8888/index.html
```

---

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── com/monolithicservice/login/
│   │   │   ├── LoginServiceApplication.java
│   │   │   ├── controller/
│   │   │   │   └── LoginController.java
│   │   │   ├── model/
│   │   │   │   └── User.java
│   │   │   └── service/
│   │   │       └── LoginService.java
│   │   └── config/
│   │       └── SecurityConfig.java
│   └── resources/
│       ├── application.properties
│       └── static/
│           └── index.html
└── test/
    └── java/
        └── com/monolithicservice/login/
            └── LoginServiceApplicationTests.java
```

---

## Configuration

Configuration is managed via `src/main/resources/application.properties`.

| Property | Default | Description |
|---|---|---|
| `spring.application.name` | `login-service` | Application name registered with Spring |
| `server.port` | `8888` | HTTP port the server listens on |

To override any property at runtime, pass it as a JVM argument:

```bash
java -jar build/libs/login-service-0.0.1-SNAPSHOT.jar --server.port=9090
```

---

## API Documentation

### Base URL

```
http://localhost:8888
```

### Endpoints

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/auth/login` | Authenticate a user with username and password |
| `GET` | `/index.html` | Serves the browser-based login UI |

---

### POST `/api/auth/login`

Authenticates a user by validating the provided credentials.

#### Request Headers

| Header | Value |
|---|---|
| `Content-Type` | `application/json` |

#### Request Body

```json
{
  "username": "string",
  "password": "string"
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `username` | `string` | ✅ Yes | The user's login name |
| `password` | `string` | ✅ Yes | The user's password |

#### Response Codes

| HTTP Status | Condition | Body |
|---|---|---|
| `200 OK` | Credentials are valid (or `loginService.authenticate()` returns any non-null string) | `Login successful` |
| `400 Bad Request` | Request body is missing, or `username`/`password` fields are null | `Invalid request` |
| `401 Unauthorized` | `loginService.authenticate()` returns `null` (see [Security Notes](#security-notes)) | _(empty)_ |

---

### curl Examples

#### a. ✅ Successful Login

```bash
curl -v -X POST http://localhost:8888/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

**Expected response:**

```
< HTTP/1.1 200
< Content-Type: text/plain;charset=UTF-8
<
Login successful
```

---

#### b. ❌ Invalid Credentials

```bash
curl -v -X POST http://localhost:8888/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "wrongpassword"}'
```

**Expected response:**

```
< HTTP/1.1 200
< Content-Type: text/plain;charset=UTF-8
<
Login successful
```

> ⚠️ **Note:** Due to a known bug (see [Security Notes](#security-notes)), invalid credentials currently return `200 OK` with body `Login successful` instead of `401 Unauthorized`. The `LoginService` returns the string `"Invalid credentials"` (non-null), which the controller incorrectly interprets as a success.

---

#### c. ❌ Missing Body (Bad Request)

```bash
curl -v -X POST http://localhost:8888/api/auth/login \
  -H "Content-Type: application/json"
```

**Expected response:**

```
< HTTP/1.1 400
< Content-Type: text/plain;charset=UTF-8
<
Invalid request
```

---

## Browser UI

The application serves a static `index.html` login form at `http://localhost:8888/index.html`. The page presents a simple username and password input form. On submission, JavaScript uses the browser's **Fetch API** to send an HTTP `POST` request to `/api/auth/login` with the entered credentials serialised as JSON. The response text returned by the server is then displayed directly on the page as a status message, providing immediate feedback to the user without a full page reload.

---

## Building

To compile the project and produce a runnable JAR:

```bash
./gradlew build
```

The packaged artifact will be output to:

```
build/libs/login-service-0.0.1-SNAPSHOT.jar
```

You can run the JAR directly without Gradle:

```bash
java -jar build/libs/login-service-0.0.1-SNAPSHOT.jar
```

---

## Testing

To execute the test suite:

```bash
./gradlew test
```

The current test suite contains a single test class `LoginServiceApplicationTests` with one test method:

- **`contextLoads()`** — verifies that the Spring application context starts up successfully without errors.

Test reports are generated at `build/reports/tests/test/index.html`.

---

## Security Notes

> ⚠️ **Warning:** This project is a **development/learning prototype only** and must not be deployed to production in its current state. The following security issues are present:

- **Hardcoded credentials** — The only accepted login is `admin` / `admin123`, hardcoded directly in `LoginService.java`. These credentials must never be used in a production environment.

- **Spring Security filter chain is disabled** — The `SecurityFilterChain` bean in `SecurityConfig.java` is entirely commented out, meaning no HTTP security rules, CSRF protection, or session management are applied.

- **Spring Security dependency is commented out** — The `spring-boot-starter-security` dependency in `build.gradle` is commented out, so the Spring Security framework is not even on the classpath.

- **All endpoints are publicly accessible** — Because no security filter chain is active, every endpoint (including `/api/auth/login`) is open to unauthenticated requests with no access controls.

- **Known bug — incorrect 401 logic** — `LoginService.authenticate()` returns the string `"Invalid credentials"` (a non-null value) when credentials are wrong. `LoginController` only returns `401 Unauthorized` when the service returns `null`, which never happens in the current implementation. As a result, **all login attempts — valid or not — receive a `200 OK` response**. The `401` branch in `LoginController` is effectively unreachable.

---

## Future Improvements

The following enhancements are recommended to make this service production-ready:

- **Database-backed user store** — Replace hardcoded credentials with a proper user repository using JPA and a database such as H2 (for development) or PostgreSQL (for production).
- **Enable Spring Security** — Uncomment the `spring-boot-starter-security` dependency and configure the `SecurityFilterChain` bean in `SecurityConfig.java` with appropriate HTTP security rules, CSRF protection, and session policies.
- **JWT-based stateless authentication** — Issue signed JSON Web Tokens on successful login and validate them on subsequent requests to enable stateless, scalable authentication.
- **Fix the 401 response logic** — Refactor `LoginController` and `LoginService` so that invalid credentials correctly return `HTTP 401 Unauthorized` (e.g., by throwing a dedicated exception or returning an `Optional`/typed result instead of a raw string).
- **Input validation** — Add Bean Validation annotations (`@NotBlank`, `@Size`, etc.) to the `User` model and use `@Valid` in the controller to reject malformed requests early.
- **Password hashing with BCrypt** — Store and compare passwords using BCrypt (via `PasswordEncoder`) rather than plaintext comparisons.
- **Integration tests for the login endpoint** — Add `@SpringBootTest` + `MockMvc` tests covering the success path, invalid-credentials path, and bad-request path for `POST /api/auth/login`.
- **Containerise with Docker** — Add a `Dockerfile` and optionally a `docker-compose.yml` to make the service easy to build and run in any environment.

---

## Contributing

Contributions are welcome! Please follow this workflow:

1. **Fork** the repository on GitHub.
2. **Create a feature branch** from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Commit your changes** with clear, descriptive commit messages.
4. **Push** the branch to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```
5. **Open a Pull Request** against the `main` branch of the upstream repository, describing your changes and the problem they solve.

Please ensure all existing tests pass (`./gradlew test`) and add new tests where appropriate before submitting your PR.

---

## License

This project is licensed under the **MIT License**.

```
MIT License

Copyright (c) 2024 login-service contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
