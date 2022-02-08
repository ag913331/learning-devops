job('seed') {
    scm {
        git('https://github.com/georgievalexandro/learning-devops.git')
    }
    wrappers {
        injectPasswords {
            injectGlobalPasswords()
        }
    }
    steps {
        dsl {
            external('jobs/job1.groovy')
        }
    }
}