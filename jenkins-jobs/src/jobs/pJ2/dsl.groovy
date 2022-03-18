pipelineJob('dsl-job-two') {
    description("dsl-job-two implementation from code")

    disabled()

    environmentVariables {
        env('ONE', '1')
        env('TWO', '2')
    }

    definition {
        cps {
            script(readFileFromWorkspace('jenkins-jobs/src/jobs/pJ2/workflow.groovy'))
            sandbox()
        }
    }
}