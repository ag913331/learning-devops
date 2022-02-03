job('DSL-Test') {
    scm {
        git('https://github.com/georgievalexandro/learning-devops.git')
    }
    triggers {
        scm('H/15 * * * *')
    }
    steps {
        batchFile('echo Check for changes!')
    }
}