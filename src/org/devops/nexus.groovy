package org.devops

//获取POM中的坐标
def GetGav(){
    jarName = sh returnStdout: true,script:"cd target;ls *.jar"
    env.jarName = jarName - "\n"
    
    pom = readMavenPom file: 'pom.xml'
    //定义成全局变量
    env.pomVersion = "${pom.version}"
    env.pomArtifact = "${pom.artifactId}"
    env.pomPackaging = "${pom.packaging}"
    env.pomGroupId = "${pom.groupId}"
    //println("${pomGroupId}-${pomArtifact}-${pomVersion}-${pomPackaging}")
    return ["${pom.groupId}","${pom.artifactId}","${pom.version}","${pom.packaging}"]
}

//使用Maven命令行上传
def MavenUpload(){
    mvnHome = tool "m2"
    sh """
        cd target/
        ${mvnHome}/bin/mvn deploy:deploy-file \
        -Dmaven.test.skip=true \
        -Dfile=${jarName} \
        -DgroupId=${pomGroupId} \
        -DartifactId=${pomArtifact} \
        -Dversion=${pomVersion} \
        -Dpackaging=${pomPackaging} \
        -DrepositoryId=maven-snapshots \
        -Durl=http://nexustest.goschainccap.com/repository/maven-snapshots
    """
}

//使用Nexus插件上传
def NexusUpload(){
    repoName = "maven-hosted"
    filePath = "target/${jarName}"
    nexusArtifactUploader artifacts: [[artifactId: "${pomArtifact}", classifier: '', file: "${filePath}", type: "${pomPackaging}"]], 
                          credentialsId: 'nexus-admin', 
                          groupId: "${pomGroupId}", 
                          nexusUrl: 'nexustest.goschainccap.com', 
                          nexusVersion: 'nexus3', 
                          protocol: 'http', 
                          repository: "${repoName}", 
                          version: "${pomVersion}"
}


//使用一个主函数调用前边2个函数
def main(uploadType){
    GetGav()
    if("${uploadType}" == "maven"){
        MavenUpload()
    }else if("${uploadType}" == "nexus"){
        NexusUpload()
    }
}