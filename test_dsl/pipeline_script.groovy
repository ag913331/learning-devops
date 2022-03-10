pipeline {
    agent any                    
    stages {
        stage('Greet') {
            steps {
                echo "Hello!! ${name}"
            }
        }
    }
}