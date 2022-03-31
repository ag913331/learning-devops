// Config
def PHONON_PATH = '/data/jenkins_repos/phonon'
// def BASE_EXE_DIR = "/media/nas/Exe/maya"
// def BUILD_TYPES = ['MAYA', 'SIM', 'SIM_DEBUG']

// Global variables set during build
def GIT_VERSION = null
def EXE_DIR = null
def DEBINFO_DIR = null
def DUPLICATE_BUILD = false
def SHOULD_BUILD = true

def phonon = null
def repos_dict = null

def get_git_version() {
//     return sh (script: """
// (source /opt/rh/rh-git218/enable;
// python3.6 white_core_shared/scripts/exe_version.py --config_path white_main/maya/cmake/maya_version.yaml)""", returnStdout: true)
    return "3"
}

def build_exe(WHITE_BUILD_TYPE, INSTALL_DIR) {
//     def MAYA_SHARED_LIBS = "OFF"
//     def CMAKE_BUILD_TYPE = "RelWithDebInfo"
//     if (WHITE_BUILD_TYPE == "SIM") {
//         MAYA_SHARED_LIBS = "ON"
//     }
//     if (WHITE_BUILD_TYPE == "SIM_DEBUG") {
//         MAYA_SHARED_LIBS = "ON"
//         CMAKE_BUILD_TYPE = "Debug"
//     }
//     return sh (script: """
// source /opt/rh/rh-git218/enable
// source /opt/rh/devtoolset-7/enable
// set -e
// mkdir -p ${WORKSPACE}/.ccache_white/${WHITE_BUILD_TYPE}
// export CCACHE_DIR="${WORKSPACE}/.ccache_white/${WHITE_BUILD_TYPE}"
// export CCACHE_MAXSIZE=10G
// cd ${WORKSPACE}/white_main/maya
// mkdir -p build/${WHITE_BUILD_TYPE}
// rm -f build/${WHITE_BUILD_TYPE}/CMakeCache.txt
// cd build/${WHITE_BUILD_TYPE}
// cmake ../../ -DCMAKE_BUILD_TYPE=${CMAKE_BUILD_TYPE} -DWHITE_BUILD_TYPE=${WHITE_BUILD_TYPE} -DMAYA_SHARED_LIBS=${MAYA_SHARED_LIBS} -DCMAKE_INSTALL_PREFIX:PATH=${INSTALL_DIR}/${WHITE_BUILD_TYPE} -DMAYA_RELEASE=ON
// make -j 8
// make install
// """)
    echo "build ${WHITE_BUILD_TYPE} in ${INSTALL_DIR}"
    return sh(script: """ls""")
}

def build_stages(GIT_VERSION, EXE_DIR, BUILD_TYPES, PHONON_PATH) {
    stages = [ : ]
    BUILD_TYPES.eachWithIndex { build_type, index ->
        def paddedIndex = index.toString().padLeft(2, '0')
        stages[paddedIndex] = {
            stage("${paddedIndex}") {
                def EXE_PATH = null
                stage("Build(${build_type})") {
                    build_exe(build_type, EXE_DIR)
                    // echo "build ${build_type} in ${EXE_DIR}"
                }
            }
        }
    }
    return stages
}

def checkout(config, phonon) {
    stages = [ : ]
    config.repositories.eachWithIndex { repo, index -> 
        def paddedIndex = index.toString().padLeft(2, '0')
        stages["${repo.branch}"] = {
            stage("${repo.branch}") {
                phonon.checkout_repo("${repo.path}", "${repo.branch}") 
            }
        }
    }

    return stages
}

pipeline {
    agent any
    stages {
        stage('Phonon') { steps { script { phonon = load("/var/jenkins_home/workspace/generator/phonon.groovy")} } }
        stage('Config') {
            steps { script { repos_dict = readYaml file: '/var/jenkins_home/workspace/generator/jobs/maya_test_build/config.yaml' } } 
        }
        stage('Checkout') {
            steps { script {
                parallel checkout(repos_dict, phonon)
            }}
        }
        stage('Version') {
            steps { script {
                GIT_VERSION = get_git_version().trim()
                EXE_DIR = "${repos_dict.base_exe_dir}/${GIT_VERSION}"
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
                // writeJSON file: 'changes.json', json: phonon.get_build_changes(currentBuild, upstream: true)
                parallel build_stages(GIT_VERSION, EXE_DIR, repos_dict.build_types, PHONON_PATH)
            }}
        }
    }
    // post {
    //     success {
    //         script {
    //             def duplicate = DUPLICATE_BUILD ? "--duplicate" : ""
    //             def skipped = !SHOULD_BUILD ? "--skipped" : ""
    //             sh """python3.6 ${PHONON_PATH}/maya/finish_build.py --build="maya-${GIT_VERSION}" ${duplicate} ${skipped} --build_number="${currentBuild.number}" --changes="${WORKSPACE}/changes.json" --status="OK" """
    //             if (SHOULD_BUILD) {
    //                 build job: 'maya_deploy', wait: false
    //             }
    //         }
    //     }
    //     failure {
    //         build job: 'mailer', parameters: [
    //             string(name: 'SUBJECT', value: "Build failed - ${currentBuild.displayName}"),
    //             string(name: 'BODY', value: "Link to failed build - ${currentBuild.absoluteUrl}")
    //         ], wait: false

    //         script {
    //             def duplicate = DUPLICATE_BUILD ? "--duplicate" : ""
    //             def skipped = !SHOULD_BUILD ? "--skipped" : ""
    //             sh """python3.6 ${PHONON_PATH}/maya/finish_build.py --build="maya-${GIT_VERSION}" ${duplicate} ${skipped} --build_number="${currentBuild.number}" --changes="${WORKSPACE}/changes.json" --status="ERR" """
    //         }
    //     }
    // }
}