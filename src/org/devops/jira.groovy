package org.devops

//封装HTTP请求
def HttpReq(reqType,reqUrl,reqBody){
    def apiServer = "http://jiratest.goschainccap.com/rest/api/2"
   
   result = httpRequest authentication: 'jira-user',
            httpMode: reqType, 
            contentType: "APPLICATION_JSON",
            consoleLogResponseBody: true,
            ignoreSslErrors: true, 
            requestBody: reqBody,
            url: "${apiServer}/${reqUrl}"
    return result
}

//执行JQL
def RunJql(jqlContent){
    apiUrl = "search?jql=${jqlContent}"
    response = HttpReq("GET",apiUrl,"")
    return response
}
