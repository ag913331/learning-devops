pipelineJob("anotherJob") {
    description("Testing another job")
    parameters {
        stringParam('name', "Ani", 'name of the person')
    }
    properties {
        buildDiscarder { strategy { logRotator(1, 3, 1, 1) } }
        disableConcurrentBuilds()
        disableResume()
        githubProjectProperty {
            projectUrlStr("https://github.com/ag913331/learning-devops") 
            displayName("learning-devops")
        }
        durabilityHint { hint("PERFORMANCE_OPTIMIZED") }
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