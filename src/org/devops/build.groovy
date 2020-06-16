package org.devops

def Build(buildType,buildShell){
    buildTools = ["mvn":"m2","ant":"ant","gradle":"gradle","node":"nodejs","npm":"nodejs"]
    //println("当前选择的构建类型为${buildType}")
    buildHome = tool buildTools[buildType]
    sh "${buildHome}/bin/${buildType} ${buildShell}"
}
