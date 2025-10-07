pipeline {
    agent any

    environment {
        
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64' 
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }
    
    stages {
        stage('first project ') {
            steps {
                echo 'first project devops'
            }
        }
        
        stage('checkout ') {
            steps {
                
                git branch: 'tasnim', url: 'https://github.com/WorKenX306/DevOps.git'
            }
        }
        
        stage('mail') {
            steps {
                // REMPLACEZ VOTRE ADRESSE E-MAIL ICI
                mail body: 'Ce mail est envoyé depuis Jenkins via Gmail App Password', subject: 'Test Email from Pipeline', to: 'tasnim.kheder@esprit.tn' 
            }
        }
        
        // 1. MVN CLEAN (Exécuté dans le bon sous-dossier)
        stage('MVN CLEAN') {
            steps {
                dir('Order/Order') {   
                    sh 'mvn clean'
                }
            }
        }
        
        // 2. MVN COMPILE (Exécuté dans le bon sous-dossier)
        stage('MVN COMPILE') {
            steps {
                dir('Order/Order') {   
                    sh 'mvn compile'
                }
            }
        }
        
        // 3. BUILD & SONAR ANALYSIS
        stage('Build & Sonar Analysis') {
            steps {
                // Compilation et analyse du code en une seule commande, dans le bon dossier
                dir('Order/Order') {   
                    sh '''
                        // Suppression du MAVEN_OPTS car Java 17 ne nécessite pas '--enable-preview'
                        
                        mvn clean package sonar:sonar \
                            -Dsonar.projectKey=mon-projet-devops \
                            -Dsonar.host.url=http://<ADRESSE_IP_DE_VOTRE_VM>:9000 \
                            -Dsonar.token=<VOTRE_TOKEN_SONARQUBE> \
                            -Dsonar.java.source=17 \
                            -Dsonar.java.target=17
                    '''
                }
            }
        }
    }
}
