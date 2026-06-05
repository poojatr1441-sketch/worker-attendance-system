📌 Worker Attendance & Overtime Settlement System
🧾 Overview

This project is a backend system for managing construction workforce attendance, overtime tracking, and monthly settlement. It simulates real-world HR operations for site workers, supervisors, and payroll operators.

⚙️ Tech Stack
Java 17
Spring Boot
Spring Data JPA (Hibernate)
PostgreSQL (Supabase)
Redis (Active Worker Cache)
Maven
🚀 Features
Worker clock-in / clock-out tracking
Real-time active worker tracking (Redis)
Automatic overtime calculation
Monthly overtime settlement
Settlement event-based SMS notification
Pagination-enabled attendance logs
Safe handling of Redis failures
🏗️ Architecture
Controller → Service → Repository flow
Redis used for active worker session tracking
PostgreSQL stores attendance + overtime history
Transactional settlement ensures data consistency
AFTER_COMMIT event triggers notification system
🔑 API Endpoints
Attendance
POST /api/attendance/clock-in
POST /api/attendance/clock-out
GET /api/attendance/active
GET /api/attendance/log
Overtime
GET /api/overtime/summary/{workerId}
POST /api/overtime/settle/{workerId}
🧠 Design Decisions
Redis used only for active session tracking, not permanent storage
Overtime settlement is fully transactional (all-or-nothing)
SMS notification triggered after DB commit using events
External wage multiplier fetched before transaction starts
Pagination added to avoid full table scans
Connection pool tuned for Supabase compatibility
🧯 Resilience Strategy
Redis failures do not break application (graceful fallback)
Cache has TTL of 16 hours for safety
DB remains source of truth
Connection pooling configured for cloud DB (Supabase)
🤖 AI Usage

Used ChatGPT for:

Architecture validation
Code review and bug fixes
Transaction design improvements
Redis + Hibernate best practices
🛠️ Setup Instructions
Configure PostgreSQL (Supabase URL in application.properties)
Start Redis locally (redis-server)
Run:
mvn spring-boot:run
📦 Postman Collection

Included in /postman folder

📌 Author Notes

This system is designed for real-world construction workforce scenarios where:

Shift timing is critical
Overtime impacts payroll directly
Data consistency is more important than speed
System must tolerate partial infrastructure failures
