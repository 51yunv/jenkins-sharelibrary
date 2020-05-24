package org.devops

//扫描
def sonarScan(projectName,projectDesc,projectPath){
    sonarDate = sh returnStdout: true, script: 'date +%F_%T'
    sonarDate = sonarDate - "\n"
    
    withSonarQubeEnv(credentialsId: 'sonar-token') {
        sh """
            sonar-scanner -Dsonar.projectKey=${projectName} \
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