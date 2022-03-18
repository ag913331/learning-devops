import javaposse.jobdsl.dsl.PipelineJob
class DefUtils {
    static void setupDefinition(def job, String desc) {
        job.with {
            description(desc)
            
        }
    }
}

PipelineJob job = pipelineJob('dsl-job-one_withUtils') {
    // description("dsl-job-one implementation from code with utils")

    disabled()

    environmentVariables {
        env('ONE', '1')
        env('TWO', '2')
    }

    definition {
        cps {
            script(readFileFromWorkspace('jenkins_jobs/jobs/dsl.groovy'))
            sandbox()
        }
    }
}

DefUtils.setupDefinition(job, "dsl-job-one implementation from code with utils")