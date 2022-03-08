pipeline {
    agent any
    stages {
        stage('Checkout stage') {
            steps {
                echo "This is checkout stage"
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

    post {
        always {
            echo 'Doing some cleanup'
        }

        success {
            echo 'Great success'
        }

        failure {
            echo 'Something went wrong'
        }
    }
}
