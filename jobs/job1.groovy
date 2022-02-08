import groovy.json.JsonSlurper

hudson.FilePath workspace = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
def config = new JsonSlurper().parseText(new File("${workspace}/github.json").text)
// def content = readFileFromWorkspace("seedA", "github.json")

for(i in 0..2) {
    job("Repo${i}") {
        // scm {
        //     git(repos[i])
        // }
        steps {
            shell("printf ${config}")
        }
    }
}
