package util

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