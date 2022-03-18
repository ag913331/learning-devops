import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
class DefUtils {
    static void setupDefinition(Job job, String workflowPath) {
        job.with {
            // description(desc)
            definition {
                cps {
                    script(readFileFromWorkspace(workflowPath))
                    sandbox()
                }
            }

            disabled()
        }
    }
}

Job job = pipelineJob('dsl-job-one_withUtils') {
    description("dsl-job-one implementation from code with utils")

    

    // environmentVariables {
    //     env('ONE', '1')
    //     env('TWO', '2')
    // }

    
}

DefUtils.setupDefinition(job, 'jenkins_jobs/jobs/workflow.groovy')