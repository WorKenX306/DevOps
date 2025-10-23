# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Install JavaFX dependencies for headless mode
RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libxtst6 \
    libxxf86vm1 \
    libgl1-mesa-glx \
    netcat-openbsd \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the JAR file from Maven build output
# Since we're building from Order/Order/, the JAR is at target/
COPY target/Order-1.0-SNAPSHOT.jar app.jar

# Environment variables for database connection
ENV DB_HOST=mysql-service \
    DB_PORT=3306 \
    DB_NAME=orderdb \
    DB_USER=orderuser \
    DB_PASSWORD=orderpass

# Expose port for REST API
EXPOSE 8080

# Run the application with the main class specified
CMD ["java", "-cp", "app.jar", "tn.esprit.MainApp"]
