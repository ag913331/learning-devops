pipeline {
    agent any
    stages {
        stage('Checkout stage') {
            steps {
                echo "This is checkout stage"
                
                script {
                    jo.repos.eachWithIndex { repo, index -> 
                        echo '${repo.name}'
                    }
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
