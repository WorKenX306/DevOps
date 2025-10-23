pipeline {
    // Agent par défaut: Utilisé uniquement pour les étapes de contrôle de base (Début, Checkout)
    agent any 

    environment {
        // Les variables JAVA_HOME et PATH sont gérées par l'image Docker Maven.
        
        // REMPLACEZ 'MySonarQubeServer' par le nom exact de votre configuration SonarQube dans Jenkins
        SONAR_SERVER_ID = 'MySonarQubeServer' 
    }

    stages {

        stage('Début') {
            steps {
                echo '🚀 Lancement du pipeline DevOps.'
            }
        }

        stage('Vérification Java & Maven') {
            // Agent dédié au Build et à l'Analyse (contient Java et Maven)
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-22-jammy'
                    args '-u 0:0 -v /var/lib/jenkins/m2-docker:/root/.m2 --network devops-net'
                }
            }
            steps {
                echo 'Vérification de l’environnement dans le conteneur Maven...'
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Checkout du code') {
            // Pas besoin d'agent Docker, l'agent 'any' est suffisant
            steps {
                git branch: 'tasnim', url: 'https://github.com/WorKenX306/DevOps.git'
                echo ' Code récupéré depuis GitHub (branche tasnim)'
            }
        }

        stage('Build Maven') {
            // Réutilise l'agent Maven pour s'assurer d'avoir le bon environnement
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-22-jammy'
                    args '-u 0:0 -v /var/lib/jenkins/m2-docker:/root/.m2 --network devops-net'
                }
            }
            steps {
                dir('Order/Order') {
                    echo 'Construction du projet Maven...'
                    sh 'mvn -Dmaven.repo.local=/root/.m2/repository clean package' 
                }
            }
        }

        stage('Analyse SonarQube') {
            when {
                expression { currentBuild.currentResult == 'SUCCESS' }
            }
            // Réutilise l'agent Maven
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-22-jammy'
                    args '-u 0:0 -v /var/lib/jenkins/m2-docker:/root/.m2 --network devops-net'
                }
            }
            steps {
                // Cette fonction nécessite l'installation du plugin "SonarQube Scanner for Jenkins"
                script {
                    withSonarQubeEnv(SONAR_SERVER_ID) {
                        dir('Order/Order') {
                            echo "Lancement de l’analyse SonarQube via le serveur: ${SONAR_SERVER_ID}..."
                            sh 'mvn sonar:sonar -Dsonar.projectKey=mon-projet-devops'
                        }
                    }
                }
            }
        }
        
        stage('Déploiement Kubernetes') {
            when {
                expression { currentBuild.currentResult == 'SUCCESS' }
            }
            // Agent spécialisé pour kubectl (résout l'erreur 'kubectl: not found')
            agent {
                docker {
                    image 'bitnami/kubectl' 
                    args '--network devops-net' 
                }
            }
            steps {
                // Assurez-vous que le fichier config Kube existe sur l'hôte Jenkins
                withEnv(["KUBECONFIG=/var/lib/jenkins/.kube/config"]) {
                    echo '🚀 Déploiement des manifests Kubernetes'
                    sh 'kubectl apply -f mysql-deployment.yaml -n devops'
                    sh 'kubectl apply -f mysql-service.yaml -n devops'
                    sh 'kubectl apply -f javafx-deployment.yaml -n devops'
                    sh 'kubectl apply -f javafx-service.yaml -n devops'

                    echo 'Vérification des Pods...'
                    sh 'kubectl get pods -n devops'
                }
            }
        }

        stage('Fin') {
            steps {
                echo ' Pipeline terminé avec succès !'
            }
        }
    }

    post {
        success {
            echo ' ✅ Pipeline réussi. Le code a été construit, analysé et déployé.'
        }
        failure {
            echo ' ❌ Échec du pipeline : vérifiez les logs du Build Maven ou du Déploiement Kubernetes.'
        }
    }
}
