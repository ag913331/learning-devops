for(i in 0..10) {
    job("DSL-Test-${i}") {
        steps {
            batchFile("echo test -Dtest.suite=${i}")
        }
    }
}