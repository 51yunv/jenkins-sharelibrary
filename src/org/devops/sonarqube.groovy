package org.devops

//扫描
def sonarScan(projectName,projectDesc,projectPath){
    //定义sonar-scanner命令环境变量
    sonar_home = "/usr/local/scanner"
    sonarDate = sh returnStdout: true, script: 'date +%F_%T'
    sonarDate = sonarDate - "\n"
    
    def qg = waitForQualityGate()
    
    withSonarQubeEnv(credentialsId: 'sonar-token') {
        sh """
            ${sonar_home}/bin/sonar-scanner -Dsonar.projectKey=${projectName} \
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
        if (qg.status != 'OK') {
          error "Pipeline aborted due to quality gate failure: ${qg.status}"
        }
    }
}