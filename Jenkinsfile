pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('app') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Test') {
            steps {
                dir('app') {
                    bat 'mvn test'
                }
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'app/target/*.jar'
            }
        }
    }
}