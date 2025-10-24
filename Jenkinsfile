pipeline{
    agent any
    triggers {
        githubPush()
    }
    environment {
        SONAR_TOKEN = credentials('jenkins-sonar-token')
        EMAIL_RECIPIENTS = "mustapha.451belkah@gmail.com"
        DOCKER_IMAGE = "workenx/order-app"
        DOCKER_TAG = "latest"
        K8S_NAMESPACE = "devops"
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

        stage('Maven Package') {
            steps {
                dir('Order/Order') {
                    sh 'mvn clean package'
                }
            }
        }
        stage('Build & Push Docker Image') {
            steps {
                dir('Order/Order') {
                    sh """
                        docker login -u workenx -p azizzizou123
                        docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                        docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest
                        docker push ${DOCKER_IMAGE}:latest
s                    """
                }
            }
        }

         stage('Deploy MySQL to Kubernetes') {
            steps {
                dir('Order/Order') {
                    script {
                        echo 'Deploying MySQL to Kubernetes...'
                        sh """
                            kubectl create namespace ${K8S_NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -
                            kubectl apply -f mysql-deployment.yaml
                            echo 'Waiting for MySQL to be ready...'
                            kubectl wait --for=condition=ready pod -l app=mysql -n ${K8S_NAMESPACE} --timeout=300s || true
                        """
                    }
                }
            }
        }

         stage('Deploy JavaFX Application to Kubernetes') {
            steps {
                dir('Order/Order') {
                    script {
                        echo 'Deploying JavaFX application to Kubernetes...'
                        sh """
                            kubectl apply -f order-deployment.yaml
                            kubectl apply -f order-app-service.yaml
                            kubectl rollout restart deployment/order-app-deployment -n ${K8S_NAMESPACE}
                            echo 'Waiting for application deployment...'
                            kubectl rollout status deployment/order-app-deployment -n ${K8S_NAMESPACE} --timeout=300s
                        """
                    }
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
