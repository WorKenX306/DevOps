FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the JAR file built by Maven
COPY target/Order-1.0-SNAPSHOT.jar app.jar

# Install X11 dependencies, Xvfb, and JavaFX for headless JavaFX
RUN apt-get update && \
    apt-get install -y \
    libgtk-3-0 \
    libgl1 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    xvfb \
    netcat-openbsd \
    openjfx && \
    rm -rf /var/lib/apt/lists/*

# Environment variables (will be overridden by Kubernetes)
ENV DB_HOST=mysql-service \
    DB_PORT=3306 \
    DB_NAME=orderdb \
    DB_USER=orderuser \
    DB_PASSWORD=orderpass \
    DISPLAY=:99

EXPOSE 8080

# Start Xvfb and run the application
CMD Xvfb :99 -screen 0 1024x768x16 & sleep 2 && \
    java -Djava.awt.headless=false \
         -Dprism.order=sw \
         --module-path /usr/share/openjfx/lib \
         --add-modules javafx.controls,javafx.fxml,javafx.graphics \
         -jar app.jar
