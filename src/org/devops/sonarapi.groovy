packge org.devops

//封装http请求
def HttpReq(reqType,reqUrl,reqBody){
    sonarServer = "http://sonarqubetest.goschainccap.com/api"
    result = httpRequest authentication: 'sonar-admin', 
             httpMode: reqType,
             contentType: 'APPLICATION_JSON', 
             responseHandle: 'NONE', 
             url: "${sonarServer}/${reqUrl}", 
             wrapAsMultipart: false
    return result
}

//获取sonar质量阈状态

def GetProjectStatus(projectName){
    apiUrl="project_branches/list?project=${projectName}"
    response = HttpReq("GET",apiUrl,'')
    response = readJSON text: """${response.content}"""
    println(response)
    return response
}