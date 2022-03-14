import groovy.yaml.YamlSlurper
def config = readFileFromWorkspace('config.yaml')
List example = new YamlSlurper().parseText(config)

println example

pipelineJob("sss") {
    description("Testing")
    parameters {
        stringParam('name', "Tom", 'name of the person')
    }

    definition {
        cps {
            script('''
                    pipeline {
                        agent any                    
                        stages {
                            stage('show config') {
                                steps {
                                    echo config
                                }
                            }
                        }
                    }
                '''
            )
        }
    }
}