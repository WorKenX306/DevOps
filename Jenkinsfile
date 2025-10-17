pipeline{
    agent any
    
    triggers {
        githubPush()
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
            environment {
                SONAR_TOKEN = credentials('jenkins-sonar-token') // the secret text you added
            }
            steps {
                dir('Order/Order') {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=Order \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.login=$SONAR_TOKEN
                    """
                }
            }
        }
    }
}
