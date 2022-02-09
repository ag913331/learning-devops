import groovy.json.JsonSlurper

hudson.FilePath workspace = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
def config = new JsonSlurper().parse(new File("${workspace}/config.json"))

pipeline {
    agent any
    stages {
        stage('Checkout stage') {
            steps {
                echo "This is checkout stage"
                
                script {
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
