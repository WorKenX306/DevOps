pipeline {
    agent {
        docker {
            image 'maven:3.9.6-eclipse-temurin-22-jammy'
            // Assurez-vous que cette ligne est correcte et que le réseau 'devops-net' est accessible par le SonarQube
            args '-u 0:0 -v /var/lib/jenkins/m2-docker:/root/.m2 --network devops-net'
        }
    }

    environment {
        JAVA_HOME = '/opt/java/openjdk'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        MAVEN_OPTS = "-Dmaven.repo.local=/root/.m2/repository"

        // L'ID du serveur SonarQube (laissé pour la structure, mais non utilisé car l'étape est commentée)
        SONAR_SERVER_ID = 'MySonarQubeServer'
    }

    stages {

        stage('Début') {
            steps {
                echo '🚀 Lancement du pipeline DevOps avec Java 22 et Maven Docker'
            }
        }

        stage('Vérification Java') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Checkout du code') {
            steps {
                git branch: 'tasnim', url: 'https://github.com/WorKenX306/DevOps.git'
                echo ' Code récupéré depuis GitHub (branche tasnim)'
            }
        }

        stage('Build Maven') {
            steps {
                dir('Order/Order') {
                    echo 'Construction du projet Maven...'
                    sh 'mvn -Dmaven.repo.local=/root/.m2/repository clean package'
                }
            }
        }

        /* ====================================================================
        ÉTAPE SONARQUBE DÉSACTIVÉE TEMPORAIREMENT 
        (En attente de résolution du problème de résolution de nom de réseau 'sonarqube')
        ====================================================================
        
        stage('Analyse SonarQube') {
            when {
                expression { currentBuild.currentResult == 'SUCCESS' }
            }
            steps {
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

        ====================================================================
        */
        
        stage('Déploiement Kubernetes') {
            steps {
                withEnv(["KUBECONFIG=/var/lib/jenkins/.kube/config"]) {
                    echo '🚀 Déploiement des manifests Kubernetes'
                    // Applique les configurations (assurez-vous que kubectl est disponible)
                    sh 'kubectl apply -f mysql-deployment.yaml -n devops'
                    sh 'kubectl apply -f mysql-service.yaml -n devops'
                    sh 'kubectl apply -f javafx-deployment.yaml -n devops'
                    sh 'kubectl apply -f javafx-service.yaml -n devops'

                    // Vérification rapide des Pods
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
            echo ' ✅ Pipeline réussi. Le code a été construit et déployé.'
        }
        failure {
            echo ' ❌ Échec du pipeline : vérifiez les logs du Build Maven ou du Déploiement Kubernetes.'
        }
    }
}
