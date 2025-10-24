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
        SONAR_URL = 'http://sonarqube:9000'
        SONAR_TOKEN = 'squ_b8f6d7256c186234fa5024231bfcc731339844e8'
        KUBECONFIG = '/root/.kube/config'
        DOCKER_REGISTRY = 'localhost:5000'
        IMAGE_NAME = 'javafx-order-app'
        K8S_NAMESPACE = 'devops'
    }
    stages {
        stage('Debut') {
            steps {
                echo '🚀 Pipeline DevOps lance avec Maven et Java Docker'
            }
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
                echo 'Code recupere depuis GitHub (branche tasnim)'
            }
        }
        
        stage('Build Maven') {
            steps {
                dir('Order/Order') {
                    echo 'Construction du projet Maven...'
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
                    echo 'Lancement de analyse SonarQube...'
                    sh '''
                        mvn sonar:sonar \
                            -Dsonar.projectKey=mon-projet-devops \
                            -Dsonar.host.url=http://sonarqube:9000 \
                            -Dsonar.login=squ_b8f6d7256c186234fa5024231bfcc731339844e8
                    '''
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                dir('Order/Order') {
                    script {
                        echo '🐳 Building Docker image...'
                        sh """
                            docker build -t ${DOCKER_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} .
                            docker tag ${DOCKER_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} ${DOCKER_REGISTRY}/${IMAGE_NAME}:latest
                        """
                    }
                }
            }
        }
        
        stage('Push to Registry') {
            steps {
                    script {
                        echo '📤 Pushing image to local registry...'
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
                        echo '🗄️ Deploying MySQL to Kubernetes...'
                        sh """
                            # Create namespace if not exists
                            kubectl create namespace ${K8S_NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -
                            
                            # Apply MySQL resources
                            kubectl apply -f mysql-deployment.yaml
                            kubectl apply -f mysql-service.yaml
                            
                            # Wait for MySQL to be ready
                            echo 'Waiting for MySQL to be ready...'
                            kubectl wait --for=condition=ready pod -l app=mysql -n ${K8S_NAMESPACE} --timeout=300s || true
                        """
                    }
                
            }
        }
        
        stage('Deploy Application to Kubernetes') {
            steps {
                    script {
                        echo '🚀 Deploying JavaFX application to Kubernetes...'
                        sh """
                            # Apply application resources
                            kubectl apply -f javafx-deployment.yaml
                            kubectl apply -f javafx-service.yaml
                            
                            # Force rollout to pick up new image
                            kubectl rollout restart deployment/javafx-order-app -n ${K8S_NAMESPACE}
                            
                            # Wait for rollout to complete
                            echo 'Waiting for application deployment...'
                            kubectl rollout status deployment/javafx-order-app -n ${K8S_NAMESPACE} --timeout=300s
                        """
                    }
                
            }
        }
        
        stage('Verify Deployment') {
            steps {
                script {
                    echo '✅ Verifying Kubernetes deployment...'
                    sh """
                        echo '=== Pods Status ==='
                        kubectl get pods -n ${K8S_NAMESPACE}
                        
                        echo ''
                        echo '=== Services ==='
                        kubectl get svc -n ${K8S_NAMESPACE}
                        
                        echo ''
                        echo '=== Application Logs (last 20 lines) ==='
                        kubectl logs -l app=javafx-order-app -n ${K8S_NAMESPACE} --tail=20 || true
                        
                        echo ''
                        echo '=== Service Endpoint ==='
                        NODE_IP=\$(kubectl get nodes -o jsonpath='{.items[0].status.addresses[?(@.type=="InternalIP")].address}')
                        echo "Application URL: http://\${NODE_IP}:30080"
                    """
                }
            }
        }
        
        stage('Fin') {
            steps {
                echo '✅ Pipeline termine avec succes !'
            }
        }
    }
    
    post {
        success {
            echo '🎉 Build reussi, analyse SonarQube effectuee et application deployee sur Kubernetes.'
        }
        failure {
            echo '❌ Echec du pipeline : verifiez les logs Maven, Docker ou Kubernetes.'
        }
    }
}
