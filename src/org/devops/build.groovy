package org.devops

def Build(buildType){
  println("当前选择的构建类型为${buildType}")
  sh "$buildType -v"
}