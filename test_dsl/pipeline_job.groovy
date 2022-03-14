@Grab('org.yaml:snakeyaml:1.17')
import org.yaml.snakeyaml.Yaml

def config = readFileFromWorkspace('config.yaml')

Yaml parser = new Yaml()
def example = parser.load(config)
println example["files"]

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
                                    echo "config"
                                }
                            }
                        }
                    }
                '''
            )
        }
    }
}