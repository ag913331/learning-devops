// APP_SAY = '''#!/bin/sh
// echo Hello Jenkins'''

job("Dsl_job") {
    description("Testing dsl")

    wrappers {
        configFiles {
            file('CONFIG_PATH') {
                variable('CONFIG_PATH')
                targetLocation('config.json')
            }
        }
    }

    parameters {
        globalVariableParam('GIT_VERSION', null, 'git version')
        globalVariableParam('EXE_DIR', null, null)
        globalVariableParam('DEBINFO_DIR', null, null)
        globalVariableParam('DUPLICATE_BUILD', null, null)
        globalVariableParam('SHOULD_BUILD', null, null)
        globalVariableParam('phonon', null, null)
        globalVariableParam('BUILD_TYPES', ['MAYA', 'SIM', 'SIM_DEBUG'], null)
    }

    steps {
        shell("echo Testing dsl job")
        // shell("printenv")
        shell(APP_SAY)
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // config["repos"].eachWithIndex { repo, index -> 
    //     scm {
    //         github(config["from"] + repo["name"], repo["branch"])
    //     }
    //     steps {
    //         shell ("echo Checking")
    //     }
    // }
}