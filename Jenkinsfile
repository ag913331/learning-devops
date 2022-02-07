pipeline {
	agent any 

	triggers {
		pollSCM('H/15 * * * *')
	}

	stages {
		stage('checkout') {
			steps {
				echo "I am about to checkout this repo twice"
				checkout scm: [
					$class: 'GitSCM',
					userRemoteConfigs: [
						[url: 'https://github.com/georgievalexandro/learning-devops.git', credentialsId: '928429e8-5c06-4b6e-9f83-7a02081edc5e'],
						[url: 'https://github.com/georgievalexandro/learning-devops.git', credentialsId: '1ce5941d-f076-4867-940b-f67f41ecc79c']
					],
		  			branches: [[name: 'refs/heads/master']]
		  		], poll: true
	  		}
		}
	}
}