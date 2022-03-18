import javaposse.jobdsl.dsl.jobs.PipelineJob
import utils.DefUtils

PipelineJob job = pipelineJob('dsl-job-one_withUtils') {
    description("dsl-job-one implementation from code with utils")

    disabled()

    environmentVariables {
        env('ONE', '1')
        env('TWO', '2')
    }
}

DefUtils.setupDefinition(job, 'jenkins-jobs/src/jobs/pj1/dsl.groovy')