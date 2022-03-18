import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
class DefUtils {
    static void setupDefinition(Job job) {
        job.with {
            // description(desc)
            definition {
                cps {
                    script(readFileFromWorkspace('jenkins_jobs/jobs/workflow.groovy'))
                    sandbox()
                }
            }
        }
    }
}

Job job = pipelineJob('dsl-job-one_withUtils') {
    description("dsl-job-one implementation from code with utils vv")

    disabled()

    // environmentVariables {
    //     env('ONE', '1')
    //     env('TWO', '2')
    // }

    
}

DefUtils.setupDefinition(job)