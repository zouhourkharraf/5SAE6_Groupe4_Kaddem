pipeline {
    agent any

    environment {
        GIT_REPO_URL = 'https://github.com/zouhourkharraf/5SAE6_Groupe4_Kaddem.git'
        MAVEN_HOME = '/usr/share/maven'
        SONARQUBE_SERVER = 'SonarQube'
        SONAR_TOKEN = credentials('sonar-token')
    }

    stages {

        stage('Clone Repository') {
            steps {
                git branch: 'KharrafZouhour_5SAE6_Groupe4', url: 'https://github.com/zouhourkharraf/5SAE6_Groupe4_Kaddem.git'
            }
        }

        stage('Build and Test') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean test"
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

         stage('Deploy to Nexus') {
             steps {
                 withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                     sh "${MAVEN_HOME}/bin/mvn deploy -DskipTests=true -DaltDeploymentRepository=deploymentRepo::default::http://10.0.2.15:8081/repository/maven-releases/"
                 }
             }
         }

         stage('Build Docker Image') {
                     steps {
                         script {
                             docker.build("zouhourkharraf/kharrafzouhour-5sae6-groupe4-kaddem:1.0")
                         }
                     }
                 }

         stage('Push Docker Image to DockerHub') {
             steps {
                 script {
                     // Utilisation des credentials Docker Hub pour s'authentifier
                     withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                         docker.withRegistry('https://index.docker.io/v1/', '') {
                             docker.image("zouhourkharraf/kharrafzouhour-5sae6-groupe4-kaddem:1.0")
                                  .push()
                         }
                     }
                 }
             }
         }

         stage('Deploy with Docker Compose') {
                              steps {
                                  sh 'docker compose down || true' // Arrêter les conteneurs existants
                                  sh 'docker compose up -d' // Démarrer les conteneurs en mode détaché
                              }
                          }




    }

     post {
            success {
                mail to: 'zouhour.kharraf1@esprit.tn',
                     from: 'Kharraf Zouhour équipe Kaddem Devops <zeinebmeliti@gmail.com>',
                     subject: "Succès de l'exécution : ${currentBuild.fullDisplayName}",
                     body: "Le build a réussi ! Voir les détails à ${env.BUILD_URL}"
            }
            failure {
                mail to: 'zouhour.kharraf1@esprit.tn',
                     from: 'Kharraf Zouhour équipe Kaddem Devops <zeinebmeliti@gmail.com>',
                     subject: "Échec de l'exécution : ${currentBuild.fullDisplayName}",
                     body: "Le build a échoué ! Voir les détails à ${env.BUILD_URL}"
            }
        }
}
