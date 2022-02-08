// def repos = ["https://github.com/georgievalexandro/learning-devops.git", "https://github.com/georgievalexandro/nda.git"]
// def repos = readFileFromWorkspace("")
for(i in 0..2) {
    job("Repo${i}") {
        // scm {
        //     git(repos[i])
        // }
        steps {
            shell("cat /jenkins_repos/github.txt")
        }
    }
}