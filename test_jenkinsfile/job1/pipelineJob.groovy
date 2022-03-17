pipelineJob("pipeline_j1") {
    description("Try creating pipeline with jobdsl and Jenkisnfile")

    definition {
        cps {
            script(readFileFromWorkspace('test_jenkinsfile/job1/Jenkinsfile'))
            sandbox()
        }
    }
}