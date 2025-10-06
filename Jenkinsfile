pipeline {
    agent any
    
    stages {
        stage('Checkout & Build') {
            steps {
                echo 'Tentative de récupération de la branche principale (main)...'
                // 🚨 CORRECTION : FORCER L'UTILISATION DE LA BRANCHE 'main' 🚨
                // Si la branche est différente, remplacez 'main' par le nom correct.
                git url: 'https://github.com/WorKenX306/DevOps.git', branch: 'main'

                // Nettoyage et Compilation
                sh "mvn clean compile"
            }

            post {
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
        
        // --- STAGE SONARQUBE ---
        stage('SonarQube Analysis') {
            steps {
                echo 'Lancement de l\'analyse du code avec SonarQube...'
                
                // N'oubliez pas de remplacer l'IP, le TOKEN et la clé de projet
                sh "mvn sonar:sonar \
                   -Dsonar.host.url=http://<Adresse_IP_de_votre_VM>:9000 \
                   -Dsonar.login=<VOTRE_TOKEN_SONARQUBE> \
                   -Dsonar.projectKey=mon-projet-devops" 
            }
        }
    }
}
