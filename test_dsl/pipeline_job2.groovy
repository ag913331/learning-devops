def BUILD_TYPES = ['MAYA', 'SIM', 'SIM_DEBUG']

pipelineJob("anotherJob") {
    displayName("Yet Another Job")
    description("Testing another job")
    
    parameters {
        stringParam('name', "Ani", 'name of the person')
        globalVariableParam('GIT_VERSION', null, 'git version')
        globalVariableParam('EXE_DIR', null, null)
        globalVariableParam('DEBINFO_DIR', null, null)
        globalVariableParam('DUPLICATE_BUILD', null, null)
        globalVariableParam('SHOULD_BUILD', null, null)
        globalVariableParam('phonon', null, null)
        globalVariableParam('BUILD_TYPES', '${BUILD_TYPES}', null)
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
    }

    environmentVariables {
        env('nickname', 'Alex')
        env('age', 25)
    }

    triggers {
        upstream { // Build after other projects are built
            upstreamProjects("greetingJob")
            threshold("SUCCESS")
        }

        cron("H/15 * * * *") // Build periodically

        gitHubPushTrigger() // GitHub hook trigger for GITScm polling

        pollSCM { // Poll SCM
            scmpoll_spec("H/12 * * * *")
            ignorePostCommitHooks(true)
        } 
    }

    disabled() // Disable this project
    quietPeriod(5) // Quiet period

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

                        stage('Show variables') {
                            steps {
                                echo printenv
                            }
                        }
                    }
                    post {
                        success {
                            writeFile file: 'status.txt', text: 'status: SUCCESS, timestamp...'
                        }
                    }
                }'''
            )
         }
     }
}