FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/workflow-worker-*.jar app.jar
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "app.jar"]