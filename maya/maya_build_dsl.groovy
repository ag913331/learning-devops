job('maya_build_dsl') {
    // def test_run_script() {
    //     return shell()
    // }
    description("Testing maya_build via dsl")
    steps {
        shell(readFileFromWorkspace('scripts/say_hello.py'))
    }
}