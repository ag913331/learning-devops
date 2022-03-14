pipelineJob('maya_build_dsl') {
    authorization {
        blocksInheritance()
    }
    properties {
        disableConcurrentBuilds()
        durabilityHint { hint("PERFORMANCE_OPTIMIZED") }
        pipelineTriggers { triggers { pollSCM { scmpoll_spec('*/1 * * * *') } } } }
    parameters {
        booleanParam('FORCE', false, 'Force build to overwrite existing files')
    }
    disabled()

    definition {
        cps {
            script(readFileFromWorkspace('maya/maya_workflow.groovy'))
            sandbox()
         }
     }
}