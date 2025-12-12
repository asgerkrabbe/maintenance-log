# Moto Maintenance Tracker

Lightweight single-user Spring Boot app for tracking motorcycle maintenance.

## Prerequisites
- Java 17
- Maven 3.9+

## Run locally
```bash
mvn spring-boot:run
```
App listens on `APP_PORT` (default 8080). Configure credentials via environment variables:
- `ADMIN_USER` (default `admin`)
- `ADMIN_PASSWORD` (default `changeme`)
- `DB_FILE_PATH` (default `./data/moto-maintenance`)

## Docker
Build and run with Docker Compose:
```bash
docker compose up --build
```
Data persists in the `moto-data` volume.

## Features
- Manage multiple motorcycles and odometer readings
- Define reusable maintenance templates and attach to bikes
- Automatic status computation (OK / DUE_SOON / OVERDUE)
- Record maintenance history and auto-update due intervals
- Dashboard, per-bike detail, and global upcoming tasks view
- JSON import/export
- H2 database stored on disk for persistence

## Authentication
Simple form login using Spring Security. Credentials come from environment variables above.
