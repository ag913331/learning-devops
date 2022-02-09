pipelineJob('PJ') {
  definition {
    cps {
      script(readFileFromWorkspace('Jenkinsfile'))
      sandbox()     
    }
  }
}