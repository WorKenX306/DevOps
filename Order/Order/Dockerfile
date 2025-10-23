FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the JAR file built by Maven
COPY target/Order-1.0-SNAPSHOT.jar app.jar

# Install dependencies
RUN apt-get update && \
    apt-get install -y libgtk-3-0 libgl1 libxext6 netcat-openbsd && \








    rm -rf /var/lib/apt/lists/*

# Environment variables (will be overridden by Kubernetes)
ENV DB_HOST=mysql-service \
    DB_PORT=3306 \
    DB_NAME=orderdb \
    DB_USER=orderuser \
    DB_PASSWORD=orderpass


EXPOSE 8080

# CRITICAL FIX: Use -cp instead of -jar to specify main class
# This fixes the "ClassNotFoundException: tn.esprit.MainApp" error
CMD ["java", \
     "-cp", "app.jar", \
     "tn.esprit.MainApp"]
