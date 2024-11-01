pipeline {
    agent any

    environment {
        GIT_REPO_URL = 'https://github.com/zouhourkharraf/5SAE6_Groupe4_Kaddem.git'
        MAVEN_HOME = '/usr/share/maven'
        SONARQUBE_SERVER = 'SonarQube'
        SONAR_TOKEN = credentials('sonar-token') // Nom du credential Jenkins pour SonarQube
    }

    stages {

        stage('Clone Repository') {
            steps {
                git branch: 'KharrafZouhour_5SAE6_Groupe4', url: 'https://github.com/zouhourkharraf/5SAE6_Groupe4_Kaddem.git'
            }
        }

        stage('Build and Compile') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean test"
                 // Lister les fichiers dans le répertoire target
                 sh 'ls -la target/'
            }
        }

        stage('Generate Jacoco Report') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn jacoco:report"
            }
        }

        stage('Code Analysis with SonarQube') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh "${MAVEN_HOME}/bin/mvn sonar:sonar -Dsonar.projectKey=ProjetSonarKaddemKey -Dsonar.login=${SONAR_TOKEN} -Dsonar.jacoco.reportPath=target/jacoco.exec"
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline exécuté avec succès'
        }
        failure {
            echo 'Pipeline échoué'
        }
    }
}
