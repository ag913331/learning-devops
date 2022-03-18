package jenkins_jobs.src.jobs.pJ1.utils
import jenkins_jobs.src.jobs.pJ1.utils.DefUtils

def job = pipelineJob('dsl-job-one_withUtils') {
    description("dsl-job-one implementation from code with utils")

    disabled()

    environmentVariables {
        env('ONE', '1')
        env('TWO', '2')
    }
}

DefUtils.setupDefinition(job, 'jenkins_jobs/src/jobs/pj1/dsl.groovy')