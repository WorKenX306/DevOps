# Stage 1: Build the JAR
FROM maven:3.9.9-eclipse-temurin-22 AS builder
WORKDIR /app

# Copy only pom.xml first to leverage Docker cache
COPY pom.xml .

# Download dependencies (cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B || mvn dependency:resolve -B

# Copy the rest of the source code
COPY . /app

# Build the application
RUN mvn clean package -DskipTests -o || mvn clean package -DskipTests

# Stage 2: Run the JAR
FROM openjdk:22-jdk-slim
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
