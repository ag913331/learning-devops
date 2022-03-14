pipelineJob("anotherJob") {
    displayName("Yet Another Job")
    description("Testing another job")
    
    properties {
        disableConcurrentBuilds()
        durabilityHint { hint("PERFORMANCE_OPTIMIZED") }
        // pipelineTriggers { triggers { pollSCM { scmpoll_spec('*/1 * * * *') } } } }
        parameters {
            booleanParam('FORCE', false, 'Force build to overwrite existing files')
        }
        disabled()

        definition {
            cps {
                script(readFileFromWorkspace('test_dsl/workflow.groovy'))
                sandbox()
            }
        }
    }
}