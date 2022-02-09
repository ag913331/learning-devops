pipeline {
    environment {
        def config = readJSON file: '../seedA/config.json'
        jo = "${config}"
    }

    agent any
    stages {
        stage('Checkout stage') {
            steps {
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
