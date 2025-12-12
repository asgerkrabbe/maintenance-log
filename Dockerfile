FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn -q -DskipTests package || true
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
ENV APP_PORT=8080
ENV DB_FILE_PATH=/data/moto-maintenance
COPY --from=build /app/target/moto-maintenance-0.0.1-SNAPSHOT.jar app.jar
EXPOSE ${APP_PORT}
ENTRYPOINT ["java","-jar","/app/app.jar"]
