# Use OpenJDK 17
FROM openjdk:17-jdk-slim

# Install JavaFX dependencies (for headless mode if needed)
RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libxtst6 \
    libxxf86vm1 \
    libgl1-mesa-glx \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the JAR file (adjust path to match your build)
ENV DB_HOST=mysql-service \
    DB_PORT=3306 \
    DB_NAME=orderdb \
    DB_USER=orderuser \
    DB_PASSWORD=orderpass



COPY Order/Order/target/*.jar app.jar

# Expose port (if your app has REST API)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
