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

//晋级制品
def ArtifactUpdate(updateType,artifactUrl){
    //定义晋级策略
    if("${updateType}" == "snapshot -> release"){
        println("snapshot -> release")
        //下载原始制品
        sh """
            [ ! -d updates ] && mkdir updates 
            rm -f updates/*  && cd updates && wget ${artifactUrl} && ls -l
        """
        
        //获取artifactID
        artifactUrl = artifactUrl - "http://nexustest.goschainccap.com/repository/maven-hosted/"
        artifactUrl = artifactUrl.split("/").toList()
        env.jarName = artifactUrl[-1]
        env.pomVersion = artifactUrl[-2]
        env.pomArtifact = artifactUrl[-3]
        env.pomPackaging = artifactUrl[-1].replace(".","-").split("-")[-1]
        env.pomGroupId = artifactUrl[0..2].join(".")
        println("${pomGroupId}##${pomArtifact}##${pomVersion}##${pomPackaging}")
        //上传制品
        //NexusUpload()
        
    }
}