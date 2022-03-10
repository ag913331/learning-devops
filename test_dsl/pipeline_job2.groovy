pipelineJob("anotherJob") {
    description("Testing another job")
    parameters {
        stringParam('name', "Ani", 'name of the person')
    }
    properties {
        buildDiscarder { strategy { logRotator { 
            daysToKeepStr('1')
            numToKeepStr('3')
        } } }
        disableConcurrentBuilds()
    }

    definition {
        cps {
            script('''
                pipeline {
                    agent any                    
                    stages {
                        stage('Another Greeting') {
                            steps {
                                echo "Hello!! ${name}"
                            }
                        }
                    }
                }'''.stripIndent()
            )
         }
     }
}