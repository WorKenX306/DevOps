pipeline {
    agent any
    
    stages {
        stage('git') {
            steps {
                git branch: 'Anas', url: 'https://github.com/WorKenX306/DevOps.git'
                echo 'git check'
            }
        }
        
        stage('Build') {
            steps {
                dir('Order/Order') {
                    sh 'mvn clean -Dmaven.compiler.source=17 -Dmaven.compiler.target=17'
                }
            }
        }
    }
}
