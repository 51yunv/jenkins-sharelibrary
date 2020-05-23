package org.devops

//扫描
def sonarScan(projectName,projectDesc,projectPath){
    //定义sonarServer服务器
    sonarServer = "http://172.17.138.183:9000"
    //执行sh命令，并获取返回值；返回值中有一个换行符\n，需要删除
    sonarDate = sh returnStdout: true, script: 'date +%F-%H-%M-%S'
    //删除换行符，其余的值作为projectVersion的版本号
    sonarDate = sonarDate - "\n"
    sh """
        sonar-scanner -Dsonar.host.url= \
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
}