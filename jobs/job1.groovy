// def repos = ["https://github.com/georgievalexandro/learning-devops.git", "https://github.com/georgievalexandro/nda.git"]
def repos = readFileFromWorkspace('../jenkins_repos/github.txt')
for(i in 0..2) {
    job("Scan repo-${i}") {
        // scm {
        //     git(repos[i])
        // }
        steps {
            shell("cat repos")
        }
    }
}