freeStyleJob('Loader') {
    steps {
        jobDsl {
            lookupStrategy('SEED_JOB')
            ignoreMissingFiles()
            targets('''
                test_dsl/pipeline_job2.groovy
                test_dsl/pipeline_job.groovy
            ''')
        }
    }
}