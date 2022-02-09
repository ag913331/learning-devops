pipeline {
    agent any
    stages {
        stage('CheckoutModule1') {
            steps {
                sh 'mkdir -p Module1'
                dir("Module1")
                {
                    git branch: "master",
                    credentialsId: '928429e8-5c06-4b6e-9f83-7a02081edc5e',
                    url: 'https://github.com/georgievalexandro/learning-devops.git'
                }
            }
        }

        stage('CheckoutModule2') {
            steps {
                sh 'mkdir -p Module2'
                dir("Module2")
                {
                    git branch: "master",
                    credentialsId: '1ce5941d-f076-4867-940b-f67f41ecc79c',
                    url: 'https://github.com/georgievalexandro/nda.git'
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Testing'
            }
        }
    }
}
