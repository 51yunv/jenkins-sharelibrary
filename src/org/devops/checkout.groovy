package org.devops

def CheckOut(credentialsId,srcUrl,branchName){
    checkout(
        [$class: 'GitSCM', branches: [[name: "*/${branchName}"]],
        doGenerateSubmoduleConfigurations: false, 
        extensions: [], 
        submoduleCfg: [], 
        userRemoteConfigs: [[credentialsId: "${credentialsId}", url: "${srcUrl}"]]])
}
