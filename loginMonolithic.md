#Access:
-http://localhost:8888/index.html (hit the url after running the application)

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
