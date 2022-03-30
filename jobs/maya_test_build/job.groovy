pipelineJob("maya_test_build") {
    description("Build automation")

    definition {
        cps {
            script(readFileFromWorkspace("jobs/maya_test_build/workflow.groovy"))
            sandbox()
        }
    }
}