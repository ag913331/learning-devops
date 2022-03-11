APP_SAY = '''#!/bin/sh
echo Hello Jenkins'''

job("Dsl_job") {
    // authenticationToken('secret')
    authorization {                 // Creates permission records. 
        permissionAll('r3d')
    }
    // blockOn('testSeed')             // Block build if certain jobs are running. 
    checkoutRetryCount(3)           // Sets the number of times the SCM checkout is retried on errors. 
    // compressBuildLog()              // Compresses the log file after build completion. 
    concurrentBuild()               // Allows Jenkins to schedule and execute multiple builds concurrently.
    displayName('DSL_JOB')          // Sets the name to display instead of the actual name. 
    label('built-in')                    // Label which specifies which nodes this job can run on. 
    environmentVariables {
        env('ONE', '1')
        env('TWO', '2')
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