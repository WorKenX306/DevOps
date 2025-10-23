pipeline {
    agent any

    stages {
        stage('Début') {
            steps {
                echo '🚀 Pipeline DevOps lancé avec Maven, Java et Kubectl Docker'
            }
        }

        stage('Vérification Java & Maven') {
            agent {
                docker {
                    image 'mon-maven-kubectl:latest'  // 🔹 Utilise ton image locale
                    args '-v /var/run/docker.sock:/var/run/docker.sock --network devops-net'
                }
            }
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
            steps {
                dir('Order/Order') {
                    echo 'Lancement de l’analyse SonarQube...'
                    sh '''
                    mvn sonar:sonar \
                      -Dsonar.projectKey=mon-projet-devops \
                      -Dsonar.host.url=http://sonarqube:9000 \
                      -Dsonar.login=squ_b8f6d7256c186234fa5024231bfcc731339844e8
                    '''
                }
            }
        }

        stage('Déploiement Kubernetes') {
            steps {
                echo '🚀 Déploiement des manifests Kubernetes'
                sh 'kubectl apply -f mysql-deployment.yaml -n devops'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline exécuté avec succès !'
        }
        failure {
            echo '❌ Échec du pipeline : vérifiez les logs Maven ou Docker.'
        }
    }
}
