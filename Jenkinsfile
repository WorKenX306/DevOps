pipeline{
    agent any
    
    triggers {
        githubPush()
    }
    environment {
        SONAR_TOKEN = credentials('jenkins-sonar-token')
        EMAIL_RECIPIENTS = "workenxaimelespatat@gmail.com"

    }

    stages{
        stage('Github'){
            steps{
                echo "Cloning the repo from github"
                git branch: 'Mustapha', url: 'https://github.com/WorKenX306/DevOps'
            }
        }
        stage('Maven Clean'){
            steps{
                dir('Order/Order') {
                    sh 'mvn clean'
                }
            }
        }
        stage('Maven Compile') {
            steps {
                dir('Order/Order') {
                    sh 'mvn compile'
                }
            }
        }
         stage('SonarQube Analysis') {
            steps {
                dir('Order/Order') {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=Devops \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.login=$SONAR_TOKEN
                    """
                }
            }
        }
    }
    post {
        success {
            emailext(
                subject: "Jenkins Build SUCCESS for Devops",
                body: "<p>The Jenkins pipeline for Devops succeeded!</p>",
                to: EMAIL_RECIPIENTS,
                mimeType: 'text/html'
            )
        }

        failure {
            emailext(
                subject: "Jenkins Build FAILED for Devops",
                body: "<p>The Jenkins pipeline for Devops failed. Please check the console output for details.</p>",
                to: EMAIL_RECIPIENTS,
                mimeType: 'text/html'
            )
        }
    }
}
