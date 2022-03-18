pipeline {
    agent any

    stages {
        stage('Stage one') {
            steps {
                echo "Doing some stuff"
            }
        }

        stage('Stage two') {
            steps {
                echo 'Doing some tests'
            }
        }
    }
}