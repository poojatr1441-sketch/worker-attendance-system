# Worker Attendance & Overtime Settlement System

---

## 📌 Overview
This project is a backend system for managing construction workforce attendance, overtime tracking, and monthly settlement.

It simulates real-world HR operations for site workers, supervisors, and payroll operators in a construction environment.

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
- Automatic overtime calculation (8-hour base rule)
- Monthly overtime settlement system
- Event-driven SMS notification after settlement
- Paginated attendance logs
- Graceful handling of Redis failures

---

## 🏗️ System Architecture

- Controller → Service → Repository layered architecture
- PostgreSQL as primary source of truth
- Redis used for active worker session tracking
- @Transactional ensures atomic settlement
- AFTER_COMMIT event triggers notifications safely

---

## 🔑 API Endpoints

### Attendance APIs
- POST /api/attendance/clock-in
- POST /api/attendance/clock-out
- GET /api/attendance/active
- GET /api/attendance/log?workerId={id}&from={date}&to={date}

### Overtime APIs
- GET /api/overtime/summary/{workerId}?month={YYYY-MM}
- POST /api/overtime/settle/{workerId}?month={YYYY-MM}

---

## 🧠 Design Decisions

- Redis is used only for active session tracking (not source of truth)
- Overtime settlement is fully transactional (all-or-nothing)
- SMS notification is triggered after commit using Spring Events
- External wage multiplier is fetched before DB transaction starts
- Pagination prevents full table scans and improves performance
- Connection pooling tuned for Supabase (PostgreSQL)

---

## 🧯 Resilience Strategy

- Redis failure does NOT break the application
- Cache entries have TTL of 16 hours for safety
- PostgreSQL remains the single source of truth
- System gracefully degrades when cache is unavailable

---

## 🤖 AI Usage

Used ChatGPT for:
- Architecture validation
- Transaction boundary design improvements
- Redis caching strategy guidance
- Debugging and runtime issue fixes
- Code review and optimization suggestions

---

## 🛠️ Setup Instructions

1. Configure PostgreSQL (Supabase connection in `application.properties`)
2. Start Redis locally:
   ```bash
   redis-server
3. Run Spring Boot application:
   ```bash
   mvn spring-boot:run   
## 📦 Postman Collection

Included in /postman directory for API testing.

## HUMAN + BUSINESS CONTEXT
REAL USERS:

- Site Worker → wants correct overtime payout SMS
- Supervisor → tracks who is currently on-site
- Payroll Team → depends on settlement accuracy

BUSINESS RULES:

- 8 hour shift standard
- 60 hour monthly overtime cap
- Redis can fail → system must still work
- Settlement must be ALL OR NOTHING
## 👨‍💻 Author Notes

This system is designed to reflect real-world HR workflows in construction workforce management, focusing on correctness, resilience, and backend design clarity rather than UI complexity.
