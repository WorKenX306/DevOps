pipeline {
    agent any

    environment {
        // CORRECTION : Nous changeons le JAVA_HOME pour pointer vers Java 22.
        // C'est indispensable car le pom.xml du prof exige Java 22.
        // !!! VÉRIFIEZ BIEN QUE CE CHEMIN CORRESPOND À VOTRE INSTALLATION JAVA 22 !!!
        JAVA_HOME = '/usr/lib/jvm/java-22-openjdk-amd64' 
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }
    
    stages {
        stage('first project ') {
            steps {
                echo 'Début du pipeline DevOps (Maintenant avec Java 22)'
            }
        }
        
        stage('checkout ') {
            steps {
                // Récupération du code depuis GitHub
                git branch: 'tasnim', url: 'https://github.com/WorKenX306/DevOps.git'
            }
        }
        
        // Les stages 'mail' sont laissés en commentaire, comme vous l'aviez fait.
        /*
        stage('mail') {
            steps {
                mail body: 'Le build est terminé. Vérifiez l\'état dans Jenkins.', 
                     subject: 'Notification de Pipeline Jenkins', 
                     to: 'tasnim.kheder@esprit.tn' 
            }
        }
        */
        
        // 1. MVN CLEAN
        stage('MVN CLEAN') {
            steps {
                dir('Order/Order') {    
                    sh 'mvn clean'
                }
            }
        }
        
        // 2. MVN COMPILE
        // Ce stage va maintenant utiliser Java 22 et devrait réussir
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
                dir('Order/Order') {    
                    sh '''
                        // Nettoyage inutile ici car fait dans 'MVN CLEAN'
                        mvn package sonar:sonar \\
                            -Dsonar.projectKey=mon-projet-devops \\
                            -Dsonar.host.url=http://localhost:9000 \\
                            -Dsonar.token=squ_2cefdc0a738acde8cb4abfed0e3d1f6c3cea2589 \\
                            // CORRECTION : Ces paramètres doivent être 22 pour correspondre au pom.xml et au JAVA_HOME
                            -Dsonar.java.source=22 \\
                            -Dsonar.java.target=22
                    '''
                }
            }
        }
    }
}
