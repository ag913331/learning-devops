import static jenkins_jobs.jobs.util.DefUtils

def job = pipelineJob('dsl-job-one_withUtils') {
    description("dsl-job-one implementation from code with utils")

    disabled()

    environmentVariables {
        env('ONE', '1')
        env('TWO', '2')
    }
}

DefUtils.setupDefinition(job, 'jenkins_jobs/jobs/dsl.groovy')