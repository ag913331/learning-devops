job('maya_build_dsl') {
    description("Testing maya_build via dsl")
    // publishers {
    //     publishBuild {
    //         discardOldBuilds(1, 2)
    //     }
    // }

    parameters {
        stringParam ('Planet', ' earth', 'Some description')
        booleanParam ('FLAG', true)
        choiceParam('OPTION', ['option 1 (default)', 'option 2', 'option 3'])
    }

    steps {
        shell("echo Testing")
    }
}