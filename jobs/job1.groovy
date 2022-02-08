def repos = ["https://github.com/georgievalexandro/learning-devops.git", "https://github.com/georgievalexandro/nda.git"]

for(i in 0..2) {
    job("Scan repo-${i}") {
        scm {
            git(repos[i])
        }
        steps {
            shell("Hello Jenkins ${i}")
        }
    }
}