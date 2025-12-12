FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw -v >/dev/null 2>&1 || true
RUN apt-get update && apt-get install -y maven && mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
ENV APP_PORT=8080 \
    DB_FILE_PATH=/data/moto-maintenance
WORKDIR /app
COPY --from=builder /app/target/moto-maintenance-0.0.1-SNAPSHOT.jar app.jar
EXPOSE ${APP_PORT}
VOLUME ["/data"]
ENTRYPOINT ["java","-jar","/app/app.jar"]
