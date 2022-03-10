pipelineJob("greetingJob") {
    description("Testing")
    parameters {
        stringParam('name', "Tom", 'name of the person')
    }

    definition {
        cps {
            // script('''
            //         pipeline {
            //             agent any                    
            //             stages {
            //                 stage('Greet') {
            //                     steps {
            //                         echo "Hello!! ${name}"
            //                     }
            //                 }
            //             }
            //         }
            // '''.stripIndent())
            script(readFileFromWorkspace('pipeline_script.groovy'))
         }
     }
}