import groovy.json.JsonSlurper

hudson.FilePath workspace = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
def config = new JsonSlurper().parseText(new File("${workspace}/github.json").text)
// def content = readFileFromWorkspace("seedA", "github.json")

config["repos"].eachWithIndex { repo, index -> 
    job("DSL_JOB_${index}") {
        concurrentBuild()
        scm {
            github(config["from"] + repo, 'master')
        }
        steps {
            shell ("Checking ---> $index")
        }
    }
}
