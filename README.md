# Moto Maintenance

A lightweight Spring Boot web app for tracking personal motorcycle maintenance. Runs on Java 17 with H2 file-based storage and Thymeleaf UI.

## Features
- Manage motorcycles with odometer tracking.
- Define reusable maintenance templates and attach them to motorcycles.
- Track maintenance records and automatically update next due dates/odometers.
- Dashboard and upcoming maintenance views with status highlighting.
- Import/export all data as JSON.
- Simple login secured by environment-provided credentials.

## Requirements
- Java 17
- Maven

## Running locally
```bash
mvn spring-boot:run
```
Application defaults:
- Port: `8080` (override with `APP_PORT`)
- H2 file path: `./data/moto-maintenance` (override with `DB_FILE_PATH`)
- Login: `ADMIN_USER` / `ADMIN_PASSWORD` (default `admin` / `password`)

## Docker
Build and run with Docker Compose:
```bash
docker compose up --build
```
Data persists in the `moto-data` volume. Override credentials and port by setting environment variables before running Compose.

## Import/Export
Use the Import/Export link in the navigation to download a JSON export or upload a previous export to restore data (overwrites existing records).

## Tests
Run the test suite:
```bash
mvn test
```
