pipeline {
    agent any
    stages {
        stage('Checkout stage') {
            steps {
                echo "This is checkout stage"
                
                sh '''git clone -b master https://github.com/georgievalexandro/learning-devops.git'''
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
