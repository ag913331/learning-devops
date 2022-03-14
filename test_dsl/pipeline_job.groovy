def config = readFileFromWorkspace('config.yaml')
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
                                    script {
                                        echo "$config"
                                    }
                                }
                            }
                        }
                    }
                '''
            )
        }
    }
}