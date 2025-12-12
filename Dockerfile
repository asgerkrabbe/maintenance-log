FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
ENV APP_PORT=8080 \
    DB_FILE_PATH=/data/moto-maintenance \
    ADMIN_USER=admin \
    ADMIN_PASSWORD=changeme
WORKDIR /app
COPY --from=build /app/target/moto-maintenance-0.0.1-SNAPSHOT.jar app.jar
EXPOSE ${APP_PORT}
VOLUME ["/data"]
ENTRYPOINT ["java","-jar","/app/app.jar"]
