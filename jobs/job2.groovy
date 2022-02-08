import groovy.json.JsonSlurper

hudson.FilePath workspace = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
def config = new JsonSlurper().parseText(new File("${workspace}/config.json").text)

pipelineJob('PJ') {
  definition {
    cps {
      script('''
        pipeline {
            agent any
            stages {
                stage('Checkout stage') {
                    ${config["repos"]}.eachWithIndex { repo, index -> 
                        steps {
                            echo '${repo["name"]}'
                        }
                    }
                }
                stage('Build') {
                    steps {
                        echo 'Building'
                    }
                }
                stage('Test') {
                    steps {
                        echo 'Testing'
                    }
                }
            }
        }
      '''.stripIndent())
      sandbox()     
    }
  }
}