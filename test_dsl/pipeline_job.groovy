def pipeline_script = readFileFromWorkspace('pipeline_script.groovy')

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
            script(pipeline_script)
         }
     }
}