pipeline{
    agent any
    
    triggers {
        githubPush()
    }
    environment {
        SONAR_TOKEN = credentials('jenkins-sonar-token')
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
        stage("Send Analysis Email"){
            steps{
                script {
                    def response = sh(script: """
                        curl -s -u $SONAR_TOKEN: \
                        "http://localhost:9000/api/measures/component?component=Devops&metricKeys=security_rating,reliability_rating,maintainability_rating,coverage,duplicated_lines_density"
                    """, returnStdout: true).trim()

                    def json = readJSON text: response
                    def measures = json.component.measures.collectEntries { [(it.metric): it.value] }

                    emailext(
                        to: 'mustapha.belkahdi@gmail.com',
                        subject: "SonarQube Analysis Resul",
                        body: """
                        Hello Mustapha,<br><br>

                        The SonarQube analysis for project <b>Devops</b> is complete.<br><br>

                        <b>Security:</b> ${measures.security_rating}<br>
                        <b>Reliability:</b> ${measures.reliability_rating}<br>
                        <b>Maintainability:</b> ${measures.maintainability_rating}<br>
                        <b>Coverage:</b> ${measures.coverage}%<br>
                        <b>Duplications:</b> ${measures.duplicated_lines_density}%<br><br>

                        Please check the SonarQube dashboard for full details:<br>
                        <a href="http://localhost:9000">SonarQube Dashboard</a>
                        """,
                        mimeType: 'text/html'
                    )
                }
            }
        }
    }
    post {
        failure {
            emailext(
                to: 'mustapha.belkahdi@gmail.com',
                subject: "Jenkins Build FAILED for Devops",
                body: "The Jenkins pipeline for Devops has failed. Please check the console output for details.",
                mimeType: 'text/plain'
            )
        }
    }
}
