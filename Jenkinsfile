def config = readJSON file: 'dir/config.json'

pipeline {
            agent any
            stages {
                stage('Checkout stage') {
                    steps {
                        script {
                            config["repos"].eachWithIndex { repo, index -> 
                                echo '${repo["name"]}'
                            }
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