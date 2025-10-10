pipeline {
    agent {
        docker {
            image 'maven:3.9.6-eclipse-temurin-22-jammy'
            args '-v /var/lib/jenkins/.m2:/root/.m2' // ✅ Monte le cache Maven de Jenkins
        }
    }

    environment {
        JAVA_HOME = '/opt/java/openjdk'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
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
            }
        }

        stage('Build Maven') {
            steps {
                dir('Order/Order') {
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
                    sh '''
                        mvn sonar:sonar \
                            -Dsonar.projectKey=mon-projet-devops \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.token=squ_2cefdc0a738acde8cb4abfed0e3d1f6c3cea2589
                    '''
                }
            }
        }
    }
}
