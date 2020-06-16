package org.devops

//扫描代码质量，通过命令行的方式
/*def SonarScan(projectName,projectDesc,projectPath){
    sonarServer = "http://sonarqubetest.goschainccap.com"
    sonar_home = "/data/sonarqube/sonar-scanner"
    sonarDate = sh returnStdout: true, script: 'date +%F_%T'
    //删除sh返回值中的换行符，否则执行扫描命令时会报错误
    sonarDate = sonarDate - "\n"
    sh """
        ${sonar_home}/bin/sonar-scanner -Dsonar.host.url=${sonarServer} \
        -Dsonar.projectKey=${projectName} \
        -Dsonar.projectName=${projectName} \
        -Dsonar.projectVersion=${sonarDate} \
        -Dsonar.login=admin \
        -Dsonar.password=admin \
        -Dsonar.ws.timeout=30 \
        -Dsonar.projectDescription=${projectDesc} \
        -Dsonar.links.homepage=http://www.baidu.com \
        -Dsonar.sources=${projectPath} \
        -Dsonar.sourceEncoding=UTF-8 \
        -Dsonar.java.binaries=target/classes \
        -Dsonar.java.test.binaries=target/test-classes \
        -Dsonar.java.surefire.report=target/surefire-reports
    """
}*/

//扫描代码质量，通过jenkins中的sonar插件
def SonarScan(projectName,projectDesc,projectPath){
    sonar_home = "/data/sonarqube/sonar-scanner"
    sonarDate = sh returnStdout: true, script: 'date +%F_%T'
    //删除sh返回值中的换行符，否则执行扫描命令时会报错误
    sonarDate = sonarDate - "\n"
    
    //可以指定配置的sonarserver名字，也可以使用credentialsId，两种方法都可以进行认证
    withSonarQubeEnv("sonarserver-prod"){
    //withSonarQubeEnv(credentialsId: 'sonar-token') {
        sh """
            ${sonar_home}/bin/sonar-scanner \
            -Dsonar.projectKey=${projectName} \
            -Dsonar.projectName=${projectName} \
            -Dsonar.projectVersion=${sonarDate} \
            -Dsonar.ws.timeout=30 \
            -Dsonar.projectDescription=${projectDesc} \
            -Dsonar.links.homepage=http://www.baidu.com \
            -Dsonar.sources=${projectPath} \
            -Dsonar.sourceEncoding=UTF-8 \
            -Dsonar.java.binaries=target/classes \
            -Dsonar.java.test.binaries=target/test-classes \
            -Dsonar.java.surefire.report=target/surefire-reports
        """
    }
}
//获取扫描结果
def SonarScanResult(){
    timeout(time: 1, unit: 'HOURS') {
        def qg = waitForQualityGate()
        if (qg.status != 'OK') {
          error "Pipeline aborted due to quality gate failure: ${qg.status}"
        }
    }
}
