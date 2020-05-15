@Library("jenkinslib") _
def tools = new org.devops.tools()

pipeline {
   agent any
   options {
       timestamps()
       skipDefaultCheckout()
   }
   stages {
        stage('获取代码') {
            steps {
                script{println("获取代码")}
            }
        }
        stage("应用打包"){
            steps{
                script{println("应用打包")}
            }
        }
        stage("代码扫描"){
            steps{
                script{
                    println("代码扫描")
                    tools.PrintMes("this is my first ShareLibrary!","green")
                }
            }
        }
    }
}

