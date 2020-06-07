package org.devops

//封装http请求
def HttpReq(reqType,reqUrl,reqBody){
    jenkinsServer = "http://jenkinstest.goschainccap.com/jenkins"
    result = httpRequest authentication: 'jenkins-gitlab-token', 
                         consoleLogResponseBody: true, 
                         httpMode: reqType, 
                         requestBody: reqBody, 
                         ignoreSslErrors: true, 
                         responseHandle: 'NONE', 
                         url: "${jenkinsServer}/${reqUrl}", 
                         wrapAsMultipart: false
    return result
}

//项目操作
def Project(projectName,option){
    options = ["DisableProject":"disable",
               "EnableProject":"enable",
               "DeleteProject":"doDelete",
               "BuildProject":"build"]
    
    result = HttpReq("POST","job/${projectName}/${options[option]}","")
}
