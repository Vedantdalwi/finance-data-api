**FINANCE DATA PROCESSING API**


A backend REST API for a finance dashboard system that supports financial record management, role-based access control, and analytics.


Built with Spring Boot and deployed on Render.


**🚀 Live Demo**


Base URL:

https://finance-data-api-0q8l.onrender.com


API Docs (ReDoc):

https://finance-data-api-0q8l.onrender.com/redoc


⚠️ Hosted on Render free tier — first request may take ~50 seconds to wake up.



 **Tech Stack**


Language: Java 17

Framework: Spring Boot

Database: PostgreSQL (Render)

Authentication: JWT (JJWT 0.12.3)

Security: Spring Security (@PreAuthorize)

ORM: Hibernate / Spring Data JPA

API Docs: SpringDoc OpenAPI (ReDoc + Swagger UI)

Deployment: Docker (Render)




 **Features**

🔐 JWT-based stateless authentication

👥 Role-based access control (ADMIN, ANALYST, VIEWER)

📊 Financial record CRUD with soft delete

🔍 Advanced filtering (date range, category, type, keyword)

📄 Pagination support

📈 Dashboard analytics with aggregated insights

⚠️ Global exception handling with structured responses

✅ Input validation with meaningful error messages




**👮 Roles & Permissions**

Action	                     ADMIN	ANALYST	VIEWER

Register / Login	                ✅	✅	✅

View financial records	          ✅	✅	✅


Create / Update / Delete records	✅	❌	❌


View dashboard analytics	        ✅	✅	❌




**📌 Assumptions**


ADMIN → Full access

ANALYST → Read + analytics access

VIEWER → Read-only access

Inactive users cannot log in





**🧑‍💻 Getting Started Locally**



📋 Prerequisites

Java 17

Maven

PostgreSQL



⚙️ Setup


1. Clone the repository

git clone https://github.com/Vedantdalwi/finance-data-api

cd finance-data-api/dataprocessing

2. Create a PostgreSQL database

3. Add env.properties



📍 src/main/resources/env.properties


spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name

spring.datasource.username=your_username

spring.datasource.password=your_password


jwt.secret=your-secret-key-at-least-32-characters-long

jwt.expiration=86400000



4. Run the app

mvn spring-boot:run


5. Access API docs
   
http://localhost:8080/redoc




**🔐 Authentication Flow**


Step 1 — Register

POST /users

Content-Type: application/json


{

  "fullName": "John Doe",
  
  "userName": "johndoe",
  
  "email": "john@example.com",
  
  "password": "password123",
  
  "role": "ADMIN"
  
}

Step 2 — Login

POST /users/login

Content-Type: application/json


{

  "email": "john@example.com",
  
  "password": "password123"
  
}

Step 3 — Use Token

GET /financial-records

Authorization: Bearer <token>


**⚠️ Error Responses**

All errors follow a consistent structure:

{

  "success": false,
  
  "error": "ACCESS_DENIED",
  
  "message": "You don't have access to perform this action",
  
  "timestamp": "2026-04-06T10:00:00Z"
  
}


**📌 Error Codes**


UNAUTHORIZED	401	Missing/invalid token


ACCESS_DENIED	403	Insufficient role


INVALID_CREDENTIALS	401	Wrong login


NOT_FOUND	404	Resource not found


CONFLICT	409	Duplicate data


VALIDATION_ERROR	400	Invalid input


INTERNAL_ERROR	500	Server error





**🚀 Deployment**


Containerized using Docker
Deployed on Render


🔧 Environment Variables


SPRING_DATASOURCE_URL

SPRING_DATASOURCE_USERNAME

SPRING_DATASOURCE_PASSWORD

JWT_SECRET



**⚖️ Tradeoffs & Notes**

❌ No refresh tokens → users re-login after expiry (24h)

🔐 userId derived from JWT (not passed in request)

💤 Render free tier sleeps after inactivity (~50s wake-up)
