package utils.DefUtils

// import javaposse.jobdsl.dsl.Job

class DefUtils {
    static void setupDefinition(def job, String workflowPath) {
        job.with {
            definition {
                cps {
                    script(readFileFromWorkspace(workflowPath))
                    sandbox()
                }
            }
        }
    }
}