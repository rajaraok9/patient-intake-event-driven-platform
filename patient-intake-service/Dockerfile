# Use a lightweight JDK 17 image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the JAR file from the target folder
# Note: Ensure your pom.xml produces a JAR with this name
COPY target/patient-intake-service-*.jar app.jar

# Expose the port from your application.yml
EXPOSE 8888

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]