pipelineJob("maya_test_build") {
    description("Build automation")

    parameters {
        booleanParam('FORCE', false, 'Force build to overwrite existing files')
    }

    definition {
        cps {
            script(readFileFromWorkspace("jobs/maya_test_build/workflow.groovy"))
            sandbox()
        }
    }
}