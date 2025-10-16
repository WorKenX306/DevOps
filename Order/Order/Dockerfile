# Stage 1: Build the JAR
FROM maven:3.9.9-eclipse-temurin-22 AS builder
WORKDIR /app

# Copy all project files from the current context
COPY . /app

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR
FROM openjdk:22-jdk-slim
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
