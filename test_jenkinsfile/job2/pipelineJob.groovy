pipelineJob("pipeline_j2") {
    description("Try creating another pipeline with jobdsl and Jenkisnfile")

    definition {
        cps {
            script(readFileFromWorkspace('test_jenkinsfile/job2/Jenkinsfile'))
            sandbox()
        }
    }
}