# Moto Maintenance Tracker

Lightweight Spring Boot web app for tracking personal motorcycle maintenance with H2 file-based storage.

## Features
- CRUD for motorcycles with odometer tracking
- Maintenance templates and per-motorcycle task instances with status (OK / DUE_SOON / OVERDUE)
- Maintenance records that update task schedules
- Dashboard and upcoming tasks views
- Import/export all data as JSON
- Simple login using environment-provided credentials

## Requirements
- Java 17
- Maven 3.9+

## Running locally
```bash
mvn spring-boot:run
```
Application listens on `APP_PORT` (default 8080). H2 database file defaults to `./data/moto-maintenance`. Configure credentials via `ADMIN_USER` and `ADMIN_PASSWORD`.

## Building
```bash
mvn package
```
Jar is produced at `target/moto-maintenance-0.0.1-SNAPSHOT.jar`.

## Docker
Build and run with Docker Compose:
```bash
docker compose up --build
```
The service binds to port 8080 by default and persists data in `./data` on the host. Override env vars as needed in `docker-compose.yml`.

## Import/Export
Use the Import/Export page in the UI to download or upload a JSON export. Imports replace existing data.
