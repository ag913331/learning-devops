job("Dsl_job") {
    // authenticationToken('secret')
    authorization {                 // Creates permission records. 
        permissionAll('r3d')
    }
    blockOn('testSeed')             // Block build if certain jobs are running. 
    checkoutRetryCount(3)           // Sets the number of times the SCM checkout is retried on errors. 
    compressBuildLog()              // Compresses the log file after build completion. 
    concurrentBuild()               // Allows Jenkins to schedule and execute multiple builds concurrently.
    displayName('DSL_JOB')          // Sets the name to display instead of the actual name. 
    steps {
        shell("echo Testing dsl job")
        jobDsl {
            scriptText('''
                pipeline {
                    agent any
                    stages {
                        stage('Checkout stage') {
                            steps {
                                echo "This is checkout stage"
                                
                                script {
                                    echo "config"
                                }
                            }
                        }
                        stage('Build') {
                            steps {
                                echo 'Building'
                            }
                        }
                        stage('Test') {
                            steps {
                                echo 'Testing'
                            }
                        }
                    }
                }
            '''    
            )
        }
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