pipeline {
  agent any 

  environment {
    GITHUB_REPO_CREDS = credentials('Jenkins')
  }

  triggers {
    pollSCM('H/15 * * * *')
  }

  stages {
    stage('checkout') {
      steps {
        echo "I am about to checkout this repo"
        checkout scm: [$class: 'GitSCM',
          userRemoteConfigs: [[url: 'https://github.com/georgievalexandro/learning-devops.git',
                              credentialsId: 'GITHUB_REPO_CREDS']],
                              branches: [[name: 'refs/heads/master']]
        ], poll: true
      }
    }
  }
}