def checkout_repo(Map args = [:], String repo_url, String local_dir) {
    // def merged_args = [
    //     branch: 'master',
    //     poll: true
    // ] << args
    // return retry(5) {
    //     checkout(scm: [
    //         $class: 'GitSCM',
    //         branches: [[name: "*/${merged_args.branch}"]],
    //         extensions: [
    //             [$class: 'RelativeTargetDirectory', relativeTargetDir: local_dir],
    //             [$class: 'CloneOption', timeout: 60]
    //         ],
    //         submoduleCfg: [],
    //         // userRemoteConfigs: [[credentialsId: 'veskok', url: repo_url]],
    //     ], 
    //     poll: merged_args.poll,
    //     changelog: merged_args.poll)
    // }
    echo "REPO: ${repo_url} LOCAL_DIR ${local_dir}"
}

def get_build_changes(Map args = [:], build) {
    def merged_args = [ upstream: false ] << args
    changes = build.changeSets.collect{changeSet ->
        changeSet.items.collect{entry -> [repo: changeSet.browser?.repoUrl, author: entry.getAuthorName().toString(), timestamp: entry.getTimestamp(), commit: entry.getCommitId(), msg: entry.getMsg()]}
    }.flatten()
    if (merged_args.upstream) {
        changes += build.upstreamBuilds.collect{ get_build_changes(it, upstream: true)}.flatten()
    }
    return changes
}

return this