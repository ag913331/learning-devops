job('example') {
    scm {
        github('https://github.com/georgievalexandro/learning-devops.git', 'master')
    }
    triggers {
        githubPush()
    }
    steps {
        shell("Hello Jenkins")
    }
}