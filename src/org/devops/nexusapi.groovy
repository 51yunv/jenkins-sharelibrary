package org.devops


//封装http请求
def HttpRequest(reqType,reqUrl,reqBody){
    nexusServer = "http://nexustest.goschainccap.com/service/rest"
    result = httpRequest authentication: 'nexus-admin',
                consoleLogResponseBody: true, 
                contentType: 'APPLICATION_JSON', 
                httpMode: "${reqType}",
                requestBody: "${reqBody}", 
                responseHandle: 'NONE', 
                url: "${nexusServer}/${reqUrl}", 
                wrapAsMultipart: false
    return result
}

//获取仓库中所有组件
def GetRepoComponents(repoName){
    apiUrl = "v1/components?repository=${repoName}"
    response = HttpRequest("GET",apiUrl,"")
    response = readJSON text: "${response.content}"
    println(response["items"].size())   
}