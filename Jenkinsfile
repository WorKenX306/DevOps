pipeline {
    agent none

    environment {
        JAVA_HOME = '/opt/java/openjdk'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        MAVEN_OPTS = "-Dmaven.repo.local=/root/.m2/repository"
    }

    stages {
        stage('Début') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-22-jammy'
                    args '-u 0:0 -v /var/lib/jenkins/m2-docker:/root/.m2 --network devops-net'
                }
            }
            steps {
                echo '🚀 Lancement du pipeline DevOps avec Java 22 et Maven Docker'
            }
        }

        stage('Vérification Java & Maven') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-22-jammy'
                    args '-u 0:0 -v /var/lib/jenkins/m2-docker:/root/.m2 --network devops-net'
                }
            }
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Checkout du code') {
            agent any
            steps {
                git branch: 'tasnim', url: 'https://github.com/WorKenX306/DevOps.git'
                echo '✅ Code récupéré depuis GitHub (branche tasnim)'
            }
        }

        stage('Build Maven') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-22-jammy'
                    args '-u 0:0 -v /var/lib/jenkins/m2-docker:/root/.m2 --network devops-net'
                }
            }
            steps {
                dir('Order/Order') {
                    echo '⚙️ Construction du projet Maven...'
                    sh 'mvn clean package -Dmaven.repo.local=/root/.m2/repository'
                }
            }
        }

        stage('Analyse SonarQube') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-22-jammy'
                    args '-u 0:0 -v /var/lib/jenkins/m2-docker:/root/.m2 --network devops-net'
                }
            }
            when {
                expression { currentBuild.currentResult == 'SUCCESS' }
            }
            steps {
                dir('Order/Order') {
                    echo '🔍 Lancement de l’analyse SonarQube...'
                    withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')]) {
                        sh """
                            mvn sonar:sonar \
                                -Dsonar.projectKey=mon-projet-devops \
                                -Dsonar.host.url=http://sonarqube:9000 \
                                -Dsonar.login=${SONAR_TOKEN}
                        """
                    }
                }
            }
        }

        stage('Déploiement Kubernetes') {
            agent {
                docker {
                    image 'bitnami/kubectl:latest'
                    args '--network devops-net'
                }
            }
            steps {
                withEnv(["KUBECONFIG=/var/lib/jenkins/.kube/config"]) {
                    echo '🚀 Déploiement des manifests Kubernetes'
                    sh 'kubectl apply -f mysql-deployment.yaml -n devops'
                    sh 'kubectl apply -f mysql-service.yaml -n devops'
                    sh 'kubectl apply -f javafx-deployment.yaml -n devops'
                    sh 'kubectl apply -f javafx-service.yaml -n devops'
                    sh 'kubectl get pods -n devops'
                }
            }
        }
    }

    post {
        success {
            echo '🎉 Pipeline terminé avec succès !'
        }
        failure {
            echo '❌ Échec du pipeline : vérifiez les logs Maven ou Docker.'
        }
    }
}
