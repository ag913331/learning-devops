job("Job1"){
    description("First job")
    label('dynamic')
    scm {
        github('https://github.com/georgievalexandro/learning-devops.git', 'master')
    }
    triggers {
        gitHubPushTrigger()   
    }
    steps {
        shell ("printf Hello DSL")
    }
}buildPipelineView('project-A') {
    title('Project A CI Pipeline')
    displayedBuilds(5)
    selectedJob('Job1')
    showPipelineParameters()
    refreshFrequency(60)
}