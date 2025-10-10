pipeline {
    agent {
        docker {
            image 'maven:3.9.6-eclipse-temurin-22-jammy'
            // ✅ Monte le cache Maven de Jenkins et définit le HOME
            args '-v /var/lib/jenkins/.m2:/root/.m2 -e HOME=/root'
        }
    }

    environment {
        JAVA_HOME = '/opt/java/openjdk'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        MAVEN_OPTS = "-Dmaven.repo.local=/root/.m2/repository"
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
                echo '✅ Code récupéré depuis GitHub (branche tasnim)'
            }
        }

        stage('Build Maven') {
            steps {
                dir('Order/Order') {
                    echo '🏗️ Construction du projet Maven...'
                    sh 'mvn -Dmaven.repo.local=/root/.m2/repository clean package'
                }
            }
        }

        stage('Analyse SonarQube') {
            when {
                expression { currentBuild.currentResult == 'SUCCESS' }
            }
            steps {
                dir('Order/Order') {
                    echo '🔍 Lancement de l’analyse SonarQube...'
                    sh '''
                        mvn sonar:sonar \
                            -Dsonar.projectKey=mon-projet-devops \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.token=squ_2cefdc0a738acde8cb4abfed0e3d1f6c3cea2589
                    '''
                }
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
