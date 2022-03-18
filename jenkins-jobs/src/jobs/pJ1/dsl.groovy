pipelineJob('dsl-job-one') {
    description("dsl-job-one implementation from code")

    disabled()

    environmentVariables {
        env('ONE', '1')
        env('TWO', '2')
    }

    definition {
        cps {
            script(readFileFromWorkspace('jenkins-jobs/src/jobs/pJ1/workflow.groovy'))
            sandbox()
        }
    }
}