pipelineJob("anotherJob") {
    displayName("Yet Another Job")
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
            scriptFile('test_dsl/pipeline_script.groovy')
         }
     }
}