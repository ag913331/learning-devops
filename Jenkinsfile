jobs:
  - script: >
      job('testJob1') {
          agent any
          triggers { pollSCM }
          stages {
            stage('Source checkout') {
                steps {
                    checkout(
                    [
                        $class: 'GitSCM',
                        branches: [],
                        browser: [],
                        doGenerateSubmoduleConfigurations: false,
                        extensions: [],
                        submoduleCfg: [],
                        userRemoteConfigs: [
                            [
                                url: 'https://github.com/georgievalexandro/learning-devops'
                            ]
                        ]
                    ]
                )
                stash 'source'
            }
        }
        stage('OS-specific binaries') {
            steps {
                shell('echo Hello Jenkins World!')
            }
        }

  - script: >
      job('testJob2') {
          steps {
              shell('echo Hello Jenkins World!')
          }
      }