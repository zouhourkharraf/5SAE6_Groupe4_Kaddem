FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/kaddem-1.0.0.jar app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "/app/app.jar"]