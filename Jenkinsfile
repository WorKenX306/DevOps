pipeline {
    agent {
        docker {
            image 'maven:3.9.6-eclipse-temurin-22-jammy'
            args '-u 0:0 -v /var/lib/jenkins/m2-docker:/root/.m2 --network devops-net'
        }
    }

    environment {
        JAVA_HOME = '/opt/java/openjdk'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        MAVEN_OPTS = "-Dmaven.repo.local=/root/.m2/repository"
        SONAR_URL = 'http://sonarqube:9000'
        SONAR_TOKEN = 'squ_2cefdc0a738acde8cb4abfed0e3d1f6c3cea2589'
        KUBECONFIG = '/var/lib/jenkins/.kube/config'
    }

    stages {
        stage('Début') {
            steps {
                echo '🚀 Pipeline DevOps lancé avec Maven et Java Docker'
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
                echo 'Code récupéré depuis GitHub (branche tasnim)'
            }
        }

        stage('Build Maven') {
            steps {
                dir('Order/Order') {
                    echo 'Construction du projet Maven...'
                    sh 'mvn clean package'
                }
            }
        }

        stage('Analyse SonarQube') {
            when {
                expression { currentBuild.currentResult == 'SUCCESS' }
            }
            steps {
                dir('Order/Order') {
                    echo 'Lancement de l’analyse SonarQube...'
                    sh """
                        mvn sonar:sonar \
                            -Dsonar.projectKey=mon-projet-devops \
                            -Dsonar.host.url=${SONAR_URL} \
                            -Dsonar.login=${SONAR_TOKEN}
                    """
                }
            }
        }

        stage('Déploiement Kubernetes') {
            steps {
                echo '🚀 Déploiement des manifests Kubernetes'
                sh 'kubectl apply -f mysql-deployment.yaml -n devops'
                sh 'kubectl apply -f mysql-service.yaml -n devops'
                sh 'kubectl apply -f javafx-deployment.yaml -n devops'
                sh 'kubectl apply -f javafx-service.yaml -n devops'
                sh 'kubectl get pods -n devops'
            }
        }

        stage('Fin') {
            steps {
                echo '✅ Pipeline terminé avec succès !'
            }
        }
    }

    post {
        success {
            echo '🎉 Build réussi et analyse SonarQube effectuée.'
        }
        failure {
            echo '❌ Échec du pipeline : vérifiez les logs Maven ou Docker.'
        }
    }
}
