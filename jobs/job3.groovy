import groovy.json.JsonSlurper

hudson.FilePath workspace = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
def config = new JsonSlurper().parse(new File("${workspace}/config.json"))

println config.repos[0]
