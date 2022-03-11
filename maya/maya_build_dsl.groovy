job('maya_build_dsl') {
    description("Testing maya_build via dsl")
    wrappers {
        configFiles {
            file('PHONON_PATH') {
                variable('PHONON_PATH')
                targetLocation('/data/jenkins_repos/phonon')
            }
            file('BASE_EXE_DIR') {
                variable('BASE_EXE_DIR')
                targetLocation('/media/nas/Exe/maya')
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

    definition {
        cps {
            script('''
                pipeline {
                    triggers { pollSCM('*/1 * * * *') }
                    agent {
                        docker { 
                            label 'master'
                            image 'white_jenkins_cam:latest'
                            args '-e TZ=Asia/Shanghai -v /data/:/data -v /media/:/media --network host'
                            reuseNode true
                        }
                    }
                    parameters {
                        booleanParam(name: 'FORCE', defaultValue: false, description: 'Force build to overwrite existing files')
                    }
                    stages {
                        stage('Phonon') { steps { script { phonon = load("${PHONON_PATH}/jenkins/phonon.groovy") } } }
                        stage('Checkout') {
                            parallel {
                                stage("white_accounts") { steps { script { phonon.checkout_repo('/data/jenkins_repos/white_accounts', 'white_accounts') } } }
                                stage("white_main") { steps { script { phonon.checkout_repo('/data/jenkins_repos/white_main', 'white_main') } } }
                                stage("white_core") { steps { script { phonon.checkout_repo('/data/jenkins_repos/white_core', 'white_core') } } }
                                stage("white_core_shared") { steps { script { phonon.checkout_repo('/data/jenkins_repos/white_core_shared', 'white_core_shared') } } }
                                stage("white_monitor") { steps { script { phonon.checkout_repo('/data/jenkins_repos/white_monitor', 'white_monitor') } } }
                                stage("white_monitor_shared") { steps { script { phonon.checkout_repo('/data/jenkins_repos/white_monitor_shared', 'white_monitor_shared') } } }
                                stage("white_logger") { steps { script { phonon.checkout_repo('/data/jenkins_repos/white_logger', 'white_logger') } } }
                                stage("white_logger_shared") { steps { script { phonon.checkout_repo('/data/jenkins_repos/white_logger_shared', 'white_logger_shared') } } }
                                stage("maya_strategy_driver_shared") { steps { script { phonon.checkout_repo('/data/jenkins_repos/maya_strategy_driver_shared', 'maya_strategy_driver_shared') } } }
                                stage("maya_strategy_driver") { steps { script { phonon.checkout_repo('/data/jenkins_repos/maya_strategy_driver', 'maya_strategy_driver') } } }
                                stage("maya_algo") { steps { script { phonon.checkout_repo('/data/jenkins_repos/maya_algo', 'maya_algo') } } }
                                stage("maya_sta_prod") { steps { script { phonon.checkout_repo('/data/jenkins_repos/maya_sta_prod', 'maya_sta_prod') } } }
                                stage("maya_mta_prod") { steps { script { phonon.checkout_repo('/data/jenkins_repos/maya_mta_prod', 'maya_mta_prod') } } }
                            }
                        }
                        stage('Version') {
                            steps { script {
                                GIT_VERSION = get_git_version().trim()
                                EXE_DIR = "${BASE_EXE_DIR}/${GIT_VERSION}"
                                echo "GIT_VERSION: ${GIT_VERSION}"
                                currentBuild.displayName = "${GIT_VERSION}"
                            }}
                        }
                        stage('Check build') {
                            steps { script {
                                DUPLICATE_BUILD = currentBuild.previousBuild?.result != 'FAILURE' && currentBuild.displayName == currentBuild.previousBuild?.displayName
                                SHOULD_BUILD = params.FORCE || !DUPLICATE_BUILD
                                echo "Current version: ${currentBuild.displayName}"
                                echo "Previous version: ${currentBuild.previousBuild?.displayName}"
                                echo "Previous build status: ${currentBuild.previousBuild?.result}"
                                echo "DUPLICATE_BUILD: ${DUPLICATE_BUILD}"
                                echo "SHOULD_BUILD: ${SHOULD_BUILD}"
                            }}
                        }
                        stage('Build main') {
                            when { expression { return SHOULD_BUILD } }
                            steps { script {
                                writeJSON file: 'changes.json', json: phonon.get_build_changes(currentBuild, upstream: true)
                                parallel build_stages(GIT_VERSION, EXE_DIR, BUILD_TYPES, PHONON_PATH)
                            }}
                        }
                    }
                    post {
                        success {
                            script {
                                def duplicate = DUPLICATE_BUILD ? "--duplicate" : ""
                                def skipped = !SHOULD_BUILD ? "--skipped" : ""
                                sh """python3.6 ${PHONON_PATH}/maya/finish_build.py --build="maya-${GIT_VERSION}" ${duplicate} ${skipped} --build_number="${currentBuild.number}" --changes="${WORKSPACE}/changes.json" --status="OK" """
                                if (SHOULD_BUILD) {
                                    build job: 'maya_deploy', wait: false
                                }
                            }
                        }
                        failure {
                            build job: 'mailer', parameters: [
                                string(name: 'SUBJECT', value: "Build failed - ${currentBuild.displayName}"),
                                string(name: 'BODY', value: "Link to failed build - ${currentBuild.absoluteUrl}")
                            ], wait: false

                            script {
                                def duplicate = DUPLICATE_BUILD ? "--duplicate" : ""
                                def skipped = !SHOULD_BUILD ? "--skipped" : ""
                                sh """python3.6 ${PHONON_PATH}/maya/finish_build.py --build="maya-${GIT_VERSION}" ${duplicate} ${skipped} --build_number="${currentBuild.number}" --changes="${WORKSPACE}/changes.json" --status="ERR" """
                            }
                        }
                    }
                }'''.stripIndent()
            )
         }
     }
}