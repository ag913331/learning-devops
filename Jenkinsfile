import groovy.json.JsonSlurper

hudson.FilePath workspace = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
def config = new JsonSlurper().parseText(new File("${workspace}/config.json").text)

pipeline {
            agent any
            stages {
                stage('Checkout stage') {
                    steps {
                        config["repos"].eachWithIndex { repo, index -> 
                            echo '${repo["name"]}'
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