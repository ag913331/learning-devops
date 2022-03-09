job('maya_build_dsl') {
    // def test_run_script() {
    //     return shell()
    // }

    steps {
        shell(readFileFromWorkspace('scripts/say_hello.py'))
    }
}