FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY settings.gradle.kts build.gradle.kts ./
COPY src src

RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
