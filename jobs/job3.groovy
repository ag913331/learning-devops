pipeline {
    agent any
    stages {
        stage('Checkout stage') {
            steps {
                echo "This is checkout stage"
                
                script {
                    echo "config"
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Building'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing'
            }
        }
    }
}
