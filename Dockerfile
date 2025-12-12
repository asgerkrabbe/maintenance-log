FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw -v >/dev/null 2>&1 || true
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre
ENV APP_PORT=8080 \
    DB_FILE_PATH=/data/moto-maintenance
WORKDIR /app
COPY --from=build /app/target/moto-maintenance-0.0.1-SNAPSHOT.jar app.jar
EXPOSE ${APP_PORT}
VOLUME ["/data"]
ENTRYPOINT ["java","-jar","/app/app.jar"]
