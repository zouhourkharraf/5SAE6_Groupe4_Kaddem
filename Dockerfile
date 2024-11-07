FROM openjdk:17
EXPOSE 8089
ADD target/5SAE6-Groupe4-kaddem.jar 5SAE6-Groupe4-kaddem.jar
ENTRYPOINT ["java", "-jar", "5SAE6-Groupe4-kaddem.jar"]

