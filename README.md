# Worker Attendance & Overtime Settlement System

## 📌 Overview
This project is a backend system for managing construction workforce attendance, overtime tracking, and monthly settlement.

It simulates real-world HR operations for site workers, supervisors, and payroll operators.

---

## ⚙️ Tech Stack
- Java 17  
- Spring Boot  
- Spring Data JPA (Hibernate)  
- PostgreSQL (Supabase)  
- Redis (Active Worker Cache)  
- Maven  

---

## 🚀 Features
- Worker clock-in / clock-out tracking  
- Real-time active worker tracking using Redis  
- Automatic overtime calculation  
- Monthly overtime settlement system  
- Event-driven SMS notification after settlement  
- Paginated attendance logs  
- Graceful handling of Redis failures  

---

## 🏗️ System Architecture
- Controller → Service → Repository layered architecture  
- Redis used for active worker session tracking  
- PostgreSQL stores attendance and overtime data  
- @Transactional ensures atomic settlement  
- AFTER_COMMIT event triggers notifications safely  

---

## 🔑 API Endpoints

### Attendance APIs
- POST `/api/attendance/clock-in`
- POST `/api/attendance/clock-out`
- GET `/api/attendance/active`
- GET `/api/attendance/log`

### Overtime APIs
- GET `/api/overtime/summary/{workerId}`
- POST `/api/overtime/settle/{workerId}`

---

## 🧠 Design Decisions
- Redis used only for active session tracking (not source of truth)  
- Overtime settlement is fully transactional (all-or-nothing)  
- SMS notification is triggered after commit using events  
- External wage multiplier is fetched before DB transaction  
- Pagination added to prevent full table scans  
- Connection pool tuned for Supabase  

---

## 🧯 Resilience Strategy
- Redis failure does NOT break application  
- Cache has TTL of 16 hours for safety  
- PostgreSQL remains the single source of truth  
- System degrades gracefully when cache is down  

---

## 🤖 AI Usage
Used ChatGPT for:
- Architecture validation  
- Transaction design improvements  
- Redis implementation guidance  
- Code review and debugging support  

---

## 🛠️ Setup Instructions

1. Configure PostgreSQL (Supabase connection in `application.properties`)  
2. Start Redis locally  
   ```bash
   redis-server
