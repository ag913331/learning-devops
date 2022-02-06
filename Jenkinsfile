jobs:
  - script: >
      job('testJob1') {
          triggers {
              scm('H/15 * * * *')
          }
          steps {
              shell('echo Hello World!')
          }
      }

  - script: >
      job('testJob2') {
          triggers {
              scm('H/15 * * * *')
          }
          steps {
              shell('echo Hello Jenkins World!')
          }
      }