pipeline {
    environment {
        def config = readJSON file: '../seedA/config.json'
        from = "${config.from}"
    }

    agent any
    stages {
        stage('Checkout stage') {
            steps {
                echo from
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
