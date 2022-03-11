import jenkins.automation.builders.BaseJobBuilder

new BaseJobBuilder(
    name: "test job builder",
    description: "Testing",
    emails: ["foo@example.com", "bar@example.com"]
).build(this).with {
    steps {
        shell("echo Hello Builder")
    }
}