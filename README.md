# Moto Maintenance Tracker

Lightweight Spring Boot web app for single-user motorcycle maintenance tracking.

## Features
- Manage multiple motorcycles with odometer tracking.
- Define maintenance templates and attach them to bikes to create upcoming tasks.
- Record maintenance history and automatically roll forward task due dates/kilometers.
- Dashboard and upcoming maintenance views with status (OK / DUE_SOON / OVERDUE).
- Import/export all data as JSON.
- Simple login using credentials from environment variables.
- Embedded H2 database stored on disk for persistence.

## Requirements
- Java 17+
- Maven 3.9+

## Running locally
```bash
mvn spring-boot:run
```
App listens on `APP_PORT` (default 8080). Credentials default to `admin` / `changeme`.

## Docker
Build and run using Docker Compose:
```bash
docker compose up --build
```
Persistent H2 data is stored in the `moto-data` volume. Adjust credentials or port via environment variables in `docker-compose.yml`.

## Import/Export
- Export: `GET /data/export` (download JSON)
- Import: `POST /data/import` with `file` form field containing a previous export.
