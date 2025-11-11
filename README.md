# Trackify
**Trackify** is a full-stack web application built with **React (Vite)** and **Spring Boot**, featuring secure JWT-based authentication and role-based access control (RBAC) for Admin, Faculty, and Students.

---

## 🧱 Tech Stack

**Frontend:** React 19, Vite, React Router, Axios, JWT  
**Backend:** Spring Boot, Spring Security, Hibernate, MySQL  
**Database:** MySQL  
**Auth:** JWT (HS256), BCrypt password hashing  
**Architecture:** Layered (Controller → Service → DAO → Entity)

---

## ⚙️ How to Run

### Backend/Frontend
```bash
cd backend
mvnw spring-boot:run

cd frontend
npm install
npm run dev

---

👥 Roles & Access

| Role        | Permissions                                  |
| ----------- | -------------------------------------------- |
| **Admin**   | Manage users, students, subjects, attendance |
| **Faculty** | Take and view attendance, manage subjects    |
| **Student** | View own attendance                          |


🔐 Security

JWT-based authentication
Spring Security with @PreAuthorize
BCrypt password encryption
CORS configured for local dev origins
