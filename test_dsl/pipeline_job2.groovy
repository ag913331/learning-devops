pipelineJob("anotherJob") {
    description("Testing another job")
    parameters {
        stringParam('name', "Ani", 'name of the person')
    }
    properties {
        buildDiscarder { strategy { logRotator(1, 3, 1, 1) } } // Discard old builds
        disableConcurrentBuilds { abortPrevious(true) } // Do not allow concurrent builds
        disableResume() // Do not allow the pipeline to resume if the controller restarts
        githubProjectProperty {  // GitHub project
            projectUrlStr("https://github.com/ag913331/learning-devops") 
            displayName("learning-devops")
        }
        durabilityHint { hint("PERFORMANCE_OPTIMIZED") } // Pipeline speed/durability override
        preserveStashes { buildCount(1) } // Preserve stashes from completed builds
        upstream { // Build after other projects are built
            upstreamProjects("learning-devops")
            threshold("SUCCESS")
         }  
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