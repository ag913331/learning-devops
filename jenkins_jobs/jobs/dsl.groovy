import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
class DefUtils {
    static void setupDefinition(Job job, String desc) {
        job.with {
            description(desc)
            disabled()
            
        }
    }
}

Job job = pipelineJob('dsl-job-one_withUtils') {
    // environmentVariables {
    //     env('ONE', '1')
    //     env('TWO', '2')
    // }

    definition {
        cps {
            script(readFileFromWorkspace('jenkins_jobs/jobs/workflow.groovy'))
            sandbox()
        }
    }
}

DefUtils.setupDefinition(job, "dsl-job-one implementation from code with utils vv")