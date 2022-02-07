pipeline {
  agent any 

  triggers {
    pollSCM('H/15 * * * *')
  }

  stages {
    stage('checkout') {
      steps {
        echo "I am about to checkout this repo"
        checkout scm: [$class: 'GitSCM',
          userRemoteConfigs: [[url: 'https://github.com/georgievalexandro/learning-devops.git',
                              credentialsId: '928429e8-5c06-4b6e-9f83-7a02081edc5e']],
                              branches: [[name: 'refs/heads/master']]
        ], poll: true
      }
    }
  }
}