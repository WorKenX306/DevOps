pipeline {
    agent {
        docker {
            // Image Maven + JDK 22
            image 'maven:3.9.6-eclipse-temurin-22-jammy'
            // Permet de garder le cache Maven entre les builds
            args '-v /var/jenkins_home/.m2:/root/.m2'
        }
    }

    stages {
        stage('first project') {
            steps {
                echo 'Début du pipeline DevOps (Maintenant avec Java 22 dans Docker)'
            }
        }

        stage('Vérification Java') {
            steps {
                echo 'Vérification de la version Java utilisée par Maven...'
                sh 'java -version'  // Affiche Java 22
                sh 'mvn -version'   // Vérifie que Maven est opérationnel
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'tasnim', url: 'https://github.com/WorKenX306/DevOps.git'
            }
        }

        // MVN CLEAN
        stage('MVN CLEAN') {
            steps {
                dir('Order/Order') {
                    sh 'mvn clean'
                }
            }
        }

        // MVN COMPILE
        stage('MVN COMPILE') {
            steps {
                dir('Order/Order') {
                    sh 'mvn compile'
                }
            }
        }

        // BUILD & SONAR ANALYSIS
        stage('Build & Sonar Analysis') {
            steps {
                dir('Order/Order') {
                    sh '''
                        mvn package sonar:sonar \
                            -Dsonar.projectKey=mon-projet-devops \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.token=squ_2cefdc0a738acde8cb4abfed0e3d1f6c3cea2589 \
                            -Dsonar.java.source=22 \
                            -Dsonar.java.target=22
                    '''
                }
            }
        }
    }
}
