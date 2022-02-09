pipeline {
    agent any
    
    stages {
        stage('Checkout stage') {
            steps {
                echo "This is checkout stage"
                script {
                    def config = readJSON file: '../seedA/config.json'
                    echo config
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
