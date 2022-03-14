@Grab('org.yaml:snakeyaml:1.17')
import org.yaml.snakeyaml.Yaml

Yaml parser = new Yaml()
List example = parser.load(("config.yaml" as File).text)
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