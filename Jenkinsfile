pipeline {
    agent {
        docker {
            image 'maven:3.9.6-eclipse-temurin-22-jammy' // Maven + Java 22
            args '-u 0:0 -v /var/lib/jenkins/.m2:/root/.m2 -v /var/lib/jenkins/workspace:/workspace --network devops-net'
        }
    }

    environment {
        JAVA_HOME = '/usr/local/openjdk-22'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        KUBECONFIG = '/workspace/.kube/config'
    }

    stages {
        stage('Début') {
            steps {
                echo '🚀 Pipeline DevOps lancé'
            }
        }

        stage('Vérification Java & Maven') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Checkout du code') {
            steps {
                git branch: 'tasnim', url: 'https://github.com/WorKenX306/DevOps.git'
            }
        }

        stage('Build Maven') {
            steps {
                dir('Order/Order') {
                    sh 'mvn clean package -Dmaven.repo.local=/root/.m2/repository'
                }
            }
        }

        stage('Analyse SonarQube') {
            when {
                expression { currentBuild.currentResult == 'SUCCESS' }
            }
            steps {
                dir('Order/Order') {
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
            steps {
                sh '''
                    curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
                    chmod +x kubectl
                    mv kubectl /usr/local/bin/
                    kubectl apply -f mysql-deployment.yaml -n devops
                    kubectl apply -f mysql-service.yaml -n devops
                    kubectl apply -f javafx-deployment.yaml -n devops
                    kubectl apply -f javafx-service.yaml -n devops
                    kubectl get pods -n devops
                '''
            }
        }
    }

    post {
        success {
            echo '🎉 Pipeline terminé avec succès !'
        }
        failure {
            echo '❌ Échec du pipeline : vérifiez Maven ou Docker.'
        }
    }
}
