def job = job('test_builder')
job.with {
    description 'A simple job'
    sh 'Hello'
}