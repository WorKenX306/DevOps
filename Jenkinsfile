pipeline {
    agent any

    environment {
        // ✅ Correction : ton JDK est Java 21 (et pas 22)
        JAVA_HOME = '/usr/lib/jvm/java-21-openjdk-amd64'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('first project') {
            steps {
                echo 'Début du pipeline DevOps (Maintenant avec Java 21)'
            }
        }

        stage('Vérification Java') {
            steps {
                echo 'Vérification de la version Java utilisée par Maven...'
                sh 'echo "JAVA_HOME défini à : ${JAVA_HOME}"'
                sh 'java -version' // Ceci doit afficher "openjdk version 21..."
            }
        }

        stage('checkout') {
            steps {
                // ✅ Récupération du code depuis GitHub
                git branch: 'tasnim', url: 'https://github.com/WorKenX306/DevOps.git'
            }
        }

        /*
        // (Optionnel) Notification mail
        stage('mail') {
            steps {
                mail body: 'Le build est terminé. Vérifiez l\'état dans Jenkins.',
                     subject: 'Notification de Pipeline Jenkins',
                     to: 'tasnim.kheder@esprit.tn'
            }
        }
        */

        // ✅ 1. MVN CLEAN
        stage('MVN CLEAN') {
            steps {
                dir('Order/Order') {
                    sh 'mvn clean'
                }
            }
        }

        // ✅ 2. MVN COMPILE
        stage('MVN COMPILE') {
            steps {
                dir('Order/Order') {
                    sh 'mvn compile'
                }
            }
        }

        // ✅ 3. BUILD & SONAR ANALYSIS
        stage('Build & Sonar Analysis') {
            steps {
                dir('Order/Order') {
                    sh '''
                        mvn package sonar:sonar \
                            -Dsonar.projectKey=mon-projet-devops \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.token=squ_2cefdc0a738acde8cb4abfed0e3d1f6c3cea2589 \
                            # Les paramètres Sonar sont adaptés à Java 21
                            -Dsonar.java.source=21 \
                            -Dsonar.java.target=21
                    '''
                }
            }
        }
    }
}
