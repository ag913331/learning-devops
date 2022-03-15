pipelineJob("anotherJob") {
    displayName("Yet Another Job")
    description("Testing variables")

    authorization {
        blocksInheritance()
    }
    
    parameters {
        booleanParam('FORCE', false, 'Force build to overwrite existing files')
    }
    properties {
        disableConcurrentBuilds()
        durabilityHint { hint("PERFORMANCE_OPTIMIZED") }
        // pipelineTriggers { triggers { pollSCM { scmpoll_spec('*/1 * * * *') } } } }
    }
    disabled()

    definition {
        cps {
            script(readFileFromWorkspace('test_dsl/workflow.groovy'))
            sandbox()
        }
    }

    environmentVariables {
        env('GIT_VERSION', null)
        env('EXE_DIR', null)
        env('DEBINFO_DIR', null)
        env('DUPLICATE_BUILD', false)
        env('SHOULD_BUILD', true)
        env('phonon', null)
    }
}