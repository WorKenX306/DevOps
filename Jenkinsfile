pipeline {
    agent {
        docker {
            image 'maven:3.9.6-eclipse-temurin-22-jammy'
            args '-u 0:0 -v /var/lib/jenkins/m2-docker:/root/.m2 -v /var/run/docker.sock:/var/run/docker.sock -v /usr/bin/docker:/usr/bin/docker -v /usr/bin/kubectl:/usr/bin/kubectl -v /var/lib/jenkins/.kube:/root/.kube --network devops-net'
        }
    }

    environment {
        JAVA_HOME = '/opt/java/openjdk'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        MAVEN_OPTS = "-Dmaven.repo.local=/root/.m2/repository"
        KUBECONFIG = '/root/.kube/config'
        DOCKER_REGISTRY = 'localhost:5000'
        IMAGE_NAME = 'javafx-order-app'
        K8S_NAMESPACE = 'devops'
    }

    stages {

        stage('Debut') {
            steps { echo '🚀 Pipeline DevOps lance avec Maven et Docker' }
        }

        stage('Verification Java & Maven') {
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
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo '🐳 Building Docker image headless...'
                    sh """
                        docker build --build-arg HEADLESS=true -t ${DOCKER_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} .
                        docker tag ${DOCKER_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} ${DOCKER_REGISTRY}/${IMAGE_NAME}:latest
                    """
                }
            }
        }

        stage('Push to Registry') {
            steps {
                script {
                    sh """
                        docker push ${DOCKER_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}
                        docker push ${DOCKER_REGISTRY}/${IMAGE_NAME}:latest
                    """
                }
            }
        }

        stage('Deploy MySQL to Kubernetes') {
            steps {
                script {
                    sh """
                        kubectl create namespace ${K8S_NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -
                        kubectl apply -f mysql-deployment.yaml
                        kubectl apply -f mysql-service.yaml
                        kubectl wait --for=condition=ready pod -l app=mysql -n ${K8S_NAMESPACE} --timeout=300s || true
                    """
                }
            }
        }

        stage('Deploy Application to Kubernetes') {
            steps {
                script {
                    echo '🚀 Deploying JavaFX (headless) to Kubernetes...'
                    sh """
                        kubectl apply -f javafx-deployment.yaml
                        kubectl apply -f javafx-service.yaml

                        # Rollout avec timeout
                        kubectl rollout restart deployment/javafx-deployment -n ${K8S_NAMESPACE}
                        kubectl rollout status deployment/javafx-deployment -n ${K8S_NAMESPACE} --timeout=180s || echo '⚠️ Deployment did not finish within 3 minutes, check pods manually'

                        # Check pods
                        kubectl get pods -n ${K8S_NAMESPACE}
                    """
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    echo '✅ Verifying Kubernetes deployment...'
                    sh """
                        kubectl get pods -n ${K8S_NAMESPACE}
                        kubectl logs -l app=javafx-order-app -n ${K8S_NAMESPACE} --tail=20 || true
                        NODE_IP=\$(kubectl get nodes -o jsonpath='{.items[0].status.addresses[?(@.type=="InternalIP")].address}')
                        echo "Application URL: http://\${NODE_IP}:30080"
                    """
                }
            }
        }
    }

    post {
        success { echo '✅ Pipeline termine avec succes !' }
        failure { echo '❌ Echec du pipeline : verifiez les logs Maven, Docker ou Kubernetes.' }
    }
}
