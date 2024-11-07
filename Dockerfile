# Étape 1: Utilisation d'une image de base avec JDK
FROM openjdk:17-jdk-slim

# Étape 2: Copie du JAR généré dans l'image
COPY target/kaddem-0.0.1-SNAPSHOT.jar /app.jar

# Étape 3: Exposer le port utilisé par Spring Boot
EXPOSE 8080

# Étape 4: Commande pour exécuter l'application Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]
