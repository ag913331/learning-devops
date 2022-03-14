pipeline {
    agent any                    
    stages {
        stage('Workflow2') {
            steps {
                script {
                    echo "Hello!! workflow2"
                }
            }
        }
    }
}