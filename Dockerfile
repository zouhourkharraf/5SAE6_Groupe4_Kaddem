FROM openjdk:17-jdk-alpine
EXPOSE 8082
ADD target/kaddem-0.1.jar kaddem-0.1.jar
ENTRYPOINT ["java","-jar","/kaddem-1.0.jar"]