pipelineJob("maya_test_build") {
    description("Build automation")

    parameters {
        booleanParam('FORCE', false, 'Force build to overwrite existing files')
    }

    environmentVariables {
        env('GIT_VERSION': null)
        env('EXE_DIR': null)
        env('DEBINFO_DIR': null)
        env('DUPLICATE_BUILD': false)
        env('SHOULD_BUILD': true)
        env('phonon': null)
        env('repos_dict': null)
    }

    definition {
        cps {
            script(readFileFromWorkspace("jobs/maya_test_build/workflow.groovy"))
            sandbox()
        }
    }
}