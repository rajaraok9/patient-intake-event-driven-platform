FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Use a wildcard to find the jar regardless of version
COPY target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]