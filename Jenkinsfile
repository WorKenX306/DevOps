pipeline{
    agent any
    
    triggers {
        githubPush()
    }
    environment {
        SONAR_TOKEN = credentials('jenkins-sonar-token')
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
        stage("Send Analysis Email"){
            steps{
                emailext(
                    to: 'workenxaimelespatat@gmail.com',
                    subject: "Jenkins Pipeline Test",
                    body: "This is a test email from your pipeline."
                )
            }
        }
    }
    post {
        failure {
            emailext(
                to: 'workenxaimelespatat@gmail.com',
                subject: "Jenkins Build FAILED for Devops",
                body: "The Jenkins pipeline for Devops has failed. Please check the console output for details.",
                mimeType: 'text/plain'
            )
        }
    }
}
