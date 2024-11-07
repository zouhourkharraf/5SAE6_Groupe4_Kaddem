pipeline {
    agent none

    stages {
        stage('Greeting') {
            parallel {
                stage('Greeting from Agent 1') {
                    agent { label 'agent1' }
                    steps {
                        echo 'Hello from Agent 1! Ready to go!'
                    }
                }

                stage('Greeting from Agent 2') {
                    agent { label 'agent2' }
                    steps {
                        echo 'Greetings from Agent 2! Ready for action!'
                    }
                }
            }
        }

        stage('Build and Run') {
            parallel {
                stage('Agent 1') {
                    agent { label 'agent1' }
                    stages {
                        stage('Git Pull on Agent 1') {
                            steps {
                                echo 'Performing Git Pull...'
                                git branch: 'EzzineWael_5SAE6_Groupe4',
                                    url: 'https://github.com/zouhourkharraf/5SAE6_Groupe4_Kaddem',
                                    credentialsId: 'github-creds'
                            }
                        }

                        stage('Maven Clean') {
                            steps {
                                echo 'Cleaning the project...'
                                sh 'mvn clean'
                            }
                        }

                        stage('Maven Compile') {
                            steps {
                                echo 'Compiling the project...'
                                sh 'mvn compile'
                            }
                        }

                        stage('Maven Package') {
                            steps {
                                echo 'Creating package...'
                                sh 'mvn package -DskipTests'
                            }
                        }

                        stage('Docker-Compose') {
                            steps {
                                sh 'pwd'
                                sh 'docker compose up -d'
                            }
                        }

                        stage('SonarQube Analysis') {
                            steps {
                                sh 'mvn clean install -U'
                                withSonarQubeEnv('SonarQube servers') {
                                    sh 'mvn sonar:sonar -Dmaven.test.skip=true'
                                }
                            }
                        }

                        stage('Quality Gate') {
                            steps {
                                timeout(time: 2, unit: 'MINUTES') {
                                    waitForQualityGate abortPipeline: true
                                }
                            }
                        }

                        stage('Nexus') {
                            steps {
                                sh 'mvn deploy -Dmaven.test.skip=true'
                            }
                        }



                        stage('Run Unit Tests') {
                            steps {
                                echo 'Running unit tests...'
                                sh 'mvn test -X'
                            }
                        }

                        stage('Publish Test Results') {
                            steps {
                                echo 'Publishing test results...'
                                junit '**/target/surefire-reports/*.xml'
                            }
                        }

                        stage('JaCoCo Code Coverage') {
                            steps {
                                echo 'Generating JaCoCo code coverage report...'
                                sh 'mvn jacoco:report'
                            }
                        }

                        stage('Publish JaCoCo Report') {
                            steps {
                                jacoco execPattern: '**/target/jacoco.exec',
                                       classPattern: '**/target/classes',
                                       sourcePattern: '**/src/main/java',
                                       inclusionPattern: '**/*.class',
                                       exclusionPattern: '**/*Test*.class'
                            }
                        }
                          stage('Build Spring Image') {
                                                    steps {
                                                        echo 'Stopping existing containers and removing old images...'
                                                        sh 'docker compose down'
                                                        sh 'docker image rm cadevaccon/ezzine-wael-5sae6-kaddem-spring:1.0.0 || true'
                                                        echo 'Creating new Docker image...'
                                                        sh 'docker build -t cadevaccon/ezzine-wael-5sae6-kaddem-spring:1.0.0 .'
                                                        sh 'docker compose up -d'
                                                    }
                                                }
                         stage('Push to Dockerhub') {
                                                steps {
                                                    script {
                                                        withCredentials([usernamePassword(credentialsId: 'DockerCreds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                                                            sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                                                        }
                                                        sh 'docker push cadevaccon/ezzine-wael-5sae6-kaddem-spring:1.0.0'
                                                        sh 'docker logout'
                                                    }
                                                }
                                            }
                    }


                }

                stage('Agent 2') {
                    agent { label 'agent2' }
                    stages {
                        stage('Git Pull on Agent 2') {
                            steps {
                                echo 'Performing Git Pull...'
                                git branch: 'EzzineWael_5SAE6_Groupe4',
                                    url: 'https://github.com/zouhourkharraf/5SAE6_Groupe4_Kaddem',
                                    credentialsId: 'github-creds'
                            }
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution completed!'
        }
        success {
            echo 'Build completed successfully!'
            mail to: 'ezine.wael@gmail.com',
                subject: "🎉 Build Successful: ${env.JOB_NAME} #${env.BUILD_NUMBER} 🎉",
                body: """
                Hello Ezine Wael! 👋

                🎊 Congratulations! The build for the project **'${env.JOB_NAME}'** has completed successfully! 🎊

                **Details:**
                - **Build Number:** ${env.BUILD_NUMBER}
                - **Build Status:** ✅ SUCCESS
                - **Build Duration:** ${currentBuild.durationString}

                You can view the full console output here: [Console Output](${env.BUILD_URL}console)

                Best regards,
                Jenkins CI/CD 🤖
                """
        }
        failure {
            echo 'Build failed!'
            mail to: 'ezine.wael@gmail.com',
                subject: "❌ Échec du Build: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                Salut Ezzine Wael,

                Le build du projet **'${env.JOB_NAME}'** s'est terminé avec le statut : FAILURE. ❌

                **Détails :**
                - **Numéro du Build :** ${env.BUILD_NUMBER}
                - **Statut du Build :** ❌ FAILURE
                - **Durée du Build :** ${currentBuild.durationString}

                Vous pouvez consulter la sortie complète de la console ici : [Sortie de la console](${env.BUILD_URL}console)

                Cordialement,
                Jenkins CI/CD
                """
        }
    }
}
