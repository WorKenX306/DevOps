pipeline {
    // Agent par défaut: Utilisé uniquement pour les étapes de contrôle de base (Début, Checkout)
    agent any 

    environment {
        JAVA_HOME = '/opt/java/openjdk'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        MAVEN_OPTS = "-Dmaven.repo.local=/root/.m2/repository"

        // *** À MODIFIER : L'ID du serveur SonarQube configuré dans Jenkins ***
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
                    // Assurez-vous que cette ligne est correcte pour votre réseau Docker
                    args '-u 0:0 -v /var/lib/jenkins/m2-docker:/root/.m2 --network devops-net'
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
                echo ' Code récupéré depuis GitHub (branche tasnim)'
            }
        }

        stage('Build Maven') {
            // L'agent Maven est réutilisé automatiquement pour cette étape
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
            steps {
                // Le wrapper withSonarQubeEnv injecte l'URL et le Token de manière sécurisée.
                script {
                    withSonarQubeEnv(SONAR_SERVER_ID) {
                        dir('Order/Order') {
                            echo "Lancement de l’analyse SonarQube via le serveur: ${SONAR_SERVER_ID}..."
                            // L'URL et le Token sont injectés par Jenkins, on ne les passe plus en ligne de commande.
                            sh 'mvn sonar:sonar -Dsonar.projectKey=mon-projet-devops'
                        }
                    }
                }
            }
        }
        
        stage('Déploiement Kubernetes') {
            // *** NOUVEL AGENT : Conteneur spécialisé qui contient la commande kubectl ***
            agent {
                docker {
                    image 'bitnami/kubectl' 
                    // Important: assurez-vous que ce conteneur peut accéder au cluster K8s
                    args '--network devops-net' 
                }
            }
            steps {
                // Assurez-vous que le fichier config Kube existe à cet emplacement sur l'hôte Jenkins
                withEnv(["KUBECONFIG=/var/lib/jenkins/.kube/config"]) {
                    echo '🚀 Déploiement des manifests Kubernetes'
                    // Les commandes kubectl s'exécutent maintenant correctement
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
            echo ' ✅ Pipeline réussi. Le code a été construit, analysé et déployé.'
        }
        failure {
            echo ' ❌ Échec du pipeline : vérifiez les logs du Build Maven ou du Déploiement Kubernetes.'
        }
    }
}
