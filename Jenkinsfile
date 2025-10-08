pipeline {
    agent any

    environment {
        // Chemin vers Java 17, essentiel pour la compilation
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64' 
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }
    
    stages {
        stage('first project ') {
            steps {
                echo 'Début du pipeline DevOps'
            }
        }
        
        stage('checkout ') {
            steps {
                // Récupération du code depuis GitHub
                git branch: 'tasnim', url: 'https://github.com/WorKenX306/DevOps.git'
            }
        }
        
        // =========================================================
        // STAGE MAIL : MIS EN COMMENTAIRE POUR IGNORER LES ERREURS SMTP
        // Si vous avez réussi à configurer Outlook, enlevez les //
        // =========================================================
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
        // Nous nous déplaçons dans le dossier 'Order/Order' où se trouve le pom.xml
        stage('MVN CLEAN') {
            steps {
                dir('Order/Order') {    
                    sh 'mvn clean'
                }
            }
        }
        
        // 2. MVN COMPILE
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
                // Cette étape combine le package Maven et l'analyse SonarQube
                dir('Order/Order') {    
                    sh '''
                        // Le MAVEN_OPTS a été retiré, c'est propre pour Java 17
                        
                        mvn clean package sonar:sonar \\
                            -Dsonar.projectKey=mon-projet-devops \\
                            -Dsonar.host.url=http://localhost:9000 \\
                            // REMPLACEZ CE JETON PAR LE VÔTRE SI VOUS LE CHANGEZ
                            -Dsonar.token=squ_2cefdc0a738acde8cb4abfed0e3d1f6c3cea2589 \\
                            -Dsonar.java.source=17 \\
                            -Dsonar.java.target=17
                    '''
                }
            }
        }
    }
}
