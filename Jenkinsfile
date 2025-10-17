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
    }
}
