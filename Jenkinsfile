pipeline {
    environment {
        def config = readJSON file: '../seedA/config.json'
        jo = "${config}"
    }

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
