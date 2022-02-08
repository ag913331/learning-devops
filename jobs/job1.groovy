// def repos = ["https://github.com/georgievalexandro/learning-devops.git", "https://github.com/georgievalexandro/nda.git"]
def content = readFileFromWorkspace("seedA", "github.txt")

for(i in 0..2) {
    job("Repo${i}") {
        // scm {
        //     git(repos[i])
        // }
        steps {
            shell("printf ${content}")
        }
    }
}
