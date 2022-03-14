pipelineJob('maya_build_dsl') {
    // authorization {
    //     permission('hudson.model.Item.Workspace:authenticated')
    //     blocksInheritance()
    // }
    disableConcurrentBuilds()
    durabilityHint { hint("PERFORMANCE_OPTIMIZED") }
    parameters {
        booleanParam(name: 'FORCE', defaultValue: false, description: 'Force build to overwrite existing files')
    }
    // parameters {
    //     globalVariableParam('GIT_VERSION', null, 'git version')
    //     globalVariableParam('EXE_DIR', null, null)
    //     globalVariableParam('DEBINFO_DIR', null, null)
    //     globalVariableParam('DUPLICATE_BUILD', null, null)
    //     globalVariableParam('SHOULD_BUILD', null, null)
    //     globalVariableParam('phonon', null, null)
    //     globalVariableParam('BUILD_TYPES', ['MAYA', 'SIM', 'SIM_DEBUG'], null)
    // }
    disabled()

    definition {
        cps {
            script(readFileFromWorkspace('maya/maya_workflow.groovy'))
         }
     }
}