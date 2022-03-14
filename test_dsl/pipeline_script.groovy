def say() {
    return 'hi'
}
pipeline {
    agent any                    
    stages {
        stage('Another Greeting') {
            steps {
                script {
                    echo "Hello!! ${name}"
                    echo say()
                }
            }
        }

        stage('Show variables') {
            steps {
                // sh 'printenv'
                sh 'echo $GIT_VERSION'
            }
        }
    }
    post {
        success {
            writeFile file: 'status.txt', text: 'status: SUCCESS, timestamp...'
        }
    }
}