# HEDI (Health Diary System) - Backend API

HEDI is a comprehensive backend API built with Spring Boot that serves as the foundation for a personal health tracking application. It provides secure authentication, health metric logging (blood pressure, heart rate, blood sugar, etc.), and user management.

## 🚀 Tech Stack

*   **Language:** Java 21
*   **Framework:** Spring Boot 
*   **Database:** PostgreSQL
*   **Security:** Spring Security & JWT (JSON Web Tokens)
*   **API Documentation:** Springdoc OpenAPI (Swagger UI)
*   **Utilities:** Lombok, MapStruct
*   **Build Tool:** Maven

## ✨ Core Features

*   **Secure Authentication:** User registration, login, and logout using JWT.
*   **OAuth Ready:** Endpoints configured to accept OAuth provider callbacks (e.g., Google).
*   **Token Management:** Secure refresh token rotation and revocation.
*   **Health Metrics Tracking:** Core entities ready for tracking Blood Sugar, Blood Pressure, Heart Rate, SpO2, and Body Temperature.
*   **Auto-Generating Documentation:** Swagger UI launches automatically upon server startup.

## 🛠️ Prerequisites

Before you begin, ensure you have the following installed:
*   [Java Development Kit (JDK) 21](https://jdk.java.net/21/)
*   [PostgreSQL](https://www.postgresql.org/download/) (running on default port `5432`)

## ⚙️ Getting Started

### 1. Database Setup
Create a new PostgreSQL database for the application. You can name it `hedi_db` or anything you prefer.

### 2. Configuration
Navigate to `src/main/resources/application.properties` and update the database credentials to match your local PostgreSQL setup:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
