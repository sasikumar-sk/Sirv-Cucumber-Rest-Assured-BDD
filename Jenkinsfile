pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME' // Use the configured Maven installer name from Jenkins (Global Tool Configuration)
        jdk 'JDK_HOME'     // Use the configured JDK installer name from Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                // Get code from GitHub
                git 'https://github.com/sasikumar-sk/Sirv-Cucumber-Rest-Assured-BDD.git'
            }
        }

        stage('Build') {
            steps {
                // Build the application (skip tests, just packaging)
                sh 'mvn clean compile -DskipTests'
            }
        }

        stage('Test') {
            steps {
                // Run all tests, including Cucumber with Rest-Assured
                sh 'mvn test'
            }
            post {
                always {
                    // Publish test results (adjust path if using Surefire or Cucumber)
                    junit 'target/surefire-reports/*.xml'
                    cucumber 'target/cucumber-reports/*.json' // If using Cucumber reporting plugin
                }
            }
        }
    }

    post {
        always {
            // Archive reports, logs or artifacts here if needed
            archiveArtifacts artifacts: 'target/**/*.html', allowEmptyArchive: true
        }
    }
}
