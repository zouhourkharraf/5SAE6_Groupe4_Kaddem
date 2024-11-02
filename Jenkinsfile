pipeline {
    agent any
    stages {
        stage('Hello Master') {
            steps {
                echo 'Hello Master........'
                sh 'mvn --version'
                sh 'java -version'
            }
        }

        stage('Git Pull') {
            steps {
                echo 'Git Pulling........'
                git branch: 'EzzineWael_5SAE6_Groupe4',
                    url: 'https://github.com/zouhourkharraf/5SAE6_Groupe4_Kaddem',
                    credentialsId: 'github-creds' // Uncomment if you need credentials
            }
        }

        
        stage('MAVEN CLEAN') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('MAVEN Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('SONA things') {
            steps {
               sh  'mvn clean install -U'
                withSonarQubeEnv('SonarQube servers') {
                    sh 'mvn sonar:sonar -Dmaven.test.skip=true'
                }
            }
        }



        stage('MAVEN Install') {
            steps {
                sh 'mvn install'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                sh 'mvn deploy'
            }
        }

        stage('DOCKER StartUP') {
            steps {
                sh 'docker compose down'
                sh 'docker compose up -d'
            }
        }
        stage('Docker Build & Push') {
            steps {
                script {
                    dockerImage = 'your-image-name' // Set your Docker image name here
                    dockerTag = "latest" // Use "latest" or any other specific tag you prefer

                    withCredentials([usernamePassword(credentialsId: 'DockerCreds',
                                                      usernameVariable: 'DOCKER_USER',
                                                      passwordVariable: 'DOCKER_PASS')]) {
                        sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                    }

                    sh "docker build -t ${dockerImage}:${dockerTag} ."
                    sh "docker tag ${dockerImage}:${dockerTag} ${dockerRegistry}/${dockerImage}:${dockerTag}"
                    sh "docker push ${dockerImage}:${dockerTag}"

                    sh 'docker logout'
                }
            }
        }
    }
    post {
        success {
            echo 'Build completed successfully!'
        }
        failure {
            sh 'docker compose down'
            echo 'Build failed!'
        }
    }
}
