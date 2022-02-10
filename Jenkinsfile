pipelineJob('checkout_dev_ops') {
  definition {
    cpsScm {
      scm {
        git {
          remote {
            url('https://github.com/georgievalexandro/learning-devops.git')
          }
          branch('*/master')
        }
      }
      lightweight()
    }
  }
}
