pipelineJob("maya_test_build") {
    description("Build automation")

    // parameters {
    //     booleanParam('LOAD_CONFIG', false, 'Load predefined workflow config')
    // }

    definition {
        cps {
            script(readFileFromWorkspace("jobs/maya_test_build/workflow.groovy"))
            sandbox()
        }
    }
}