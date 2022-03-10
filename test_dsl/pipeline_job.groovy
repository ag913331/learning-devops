pipelineJob("greetingJob") {
    description("Testing")
    parameters {
        stringParam('name', "Tom", 'name of the person')
    }

    definition {
        cpsScm {
            scm {
                git {
                    branch('master')
                    remote {
                        credentials('c7451d39-c220-4088-b7c0-4331ab39a2ba')
                        url('https://github.com/ag913331/learning-devops')
                    }
                }

                scriptPath('test_dsl/pipeline_script.groovy')
            }
         }
     }
}

pipelineJob("anotherJob") {
    description("Testing another job")
    parameters {
        stringParam('name', "Ani", 'name of the person')
    }

    definition {
        cps {
            script('echo Another job')
         }
     }
}