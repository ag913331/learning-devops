import groovy.json.JsonSlurper

hudson.FilePath workspace = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
def config = new JsonSlurper().parseText(new File("${workspace}/config.json").text)
// def content = readFileFromWorkspace("seedA", "config.json")

config["repos"].eachWithIndex { repo, index -> 
    job("DSL_JOB_${index}") {
        concurrentBuild()
        scm {
            github(config["from"] + repo["name"], repo["branch"])
        }
        steps {
            shell ("echo Checking")
        }
    }
}
