package org.devops

//封装http请求
def HttpReq(reqType,reqUrl,reqBody){
    gitlabServer = "http://gitlabtest.goschainccap.com/api/v4"
    withCredentials([string(credentialsId: 'gitlab-token', variable: 'gitlabToken')]) {
        result = httpRequest customHeaders: [[maskValue: true, name: 'PRIVATE-TOKEN', value: "${gitlabToken}"]], 
                             httpMode: "${reqType}", 
                             contentType: 'APPLICATION_JSON'
                             responseHandle: 'NONE', 
                             url: "${gitlabServer}/${reqUrl}",
                             wrapAsMultipart: false
    }
    return result
    
}

//更改提交状态
def ChangeCommitStatus(projectId,commitSha,status){
    commitApi = "projects/${projectId}/statuses/${commitSha?state=${status}}"
    response = HttpReq('POST','commitApi','')
    println(response)
    return response
}