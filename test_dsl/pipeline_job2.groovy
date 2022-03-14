pipelineJob("anotherJob") {
    displayName("Yet Another Job")
    description("Testing another job")

    authorization {
        permission('hudson.model.Item.Workspace:authenticated')
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
}