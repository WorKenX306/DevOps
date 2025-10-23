# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Install JavaFX dependencies for headless mode
RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libxtst6 \
    libxxf86vm1 \
    libgl1-mesa-glx \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the JAR file from Maven build output (ensure it exists in Order/Order/target)
COPY Order/Order/target/*.jar app.jar

# Expose port for REST API
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
