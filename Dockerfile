# Étape 1 : Utiliser une image de base légère Java
FROM openjdk:17-jdk-alpine

# Étape 2 : Définir le répertoire de travail
WORKDIR /app

# Étape 3 : Copier le fichier JAR dans le conteneur
COPY target/app.jar app.jar

# Étape 4 : Exposer le port 8080
EXPOSE 8080

# Étape 5 : Commande pour démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
