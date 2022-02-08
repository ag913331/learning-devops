import groovy.json.JsonSlurper

hudson.FilePath workspace = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
def config = new JsonSlurper().parseText(new File("${workspace}/config.json").text)

pipelineJob('CHECKOUT2') {
  definition {
    cps {
      script('''
        pipeline {
            agent any
            stages {
                stage('Stage 1') {
                    steps {
                        echo 'logic'
                    }
                }
                stage('Stage 2') {
                    steps {
                        echo 'logic'
                    }
                }
            }
        }
      '''.stripIndent())
      sandbox()     
    }
  }
}