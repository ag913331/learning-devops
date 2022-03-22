def WOKFLOW_PATH = 'test_dsl/workflow.groovy'

pipelineJob("anotherJob") {
    displayName("Yet Another Job")
    description("Testing variables and wokflow path")

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
            script(readFileFromWorkspace(WOKFLOW_PATH))
            sandbox()
        }
    }

    // environmentVariables {
    //     env('GIT_VERSION', null)
    //     env('EXE_DIR', null)
    //     env('DEBINFO_DIR', null)
    //     env('DUPLICATE_BUILD', false)
    //     env('SHOULD_BUILD', true)
    //     env('phonon', null)
    // }
}