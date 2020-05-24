packge org.devops

//封装http请求
def HttpReq(reqType,reqUrl,reqBody){
    sonarServer = "http://sonarqubetest.goschainccap.com/api"
    result = httpRequest authentication: 'sonar-admin', 
             httpMode: reqType,
             contentType: 'APPLICATION_JSON', 
             consoleLogResponseBody: true,
             //responseHandle: 'NONE', 
             url: "${sonarServer}/${reqUrl}", 
             wrapAsMultipart: false
    return result
}

//获取sonar质量阈状态

def GetProjectStatus(projectName){
    apiUrl="project_branches/list?project=${projectName}"
    response = HttpReq("GET",apiUrl,'')
    //response返回的是字符串，需要用readJSON转换为json类型
    response = readJSON text: """${response.content}"""
    //获取json中某个key的value
    result = response["branches"][0]["status"]["qualityGateStatus"]
    return result
}

//通过api搜索sonar项目，判断其是否存在
def SearchProject(projectName){
    apiUrl = "projects/search?projects=${projectName}"
    response = HttpReq("GET",apiUrl,'')
    response = readJson text: """${response.content}"""
    result = response["paging"]["total"]
    
    if(result.toString() == "0"){
        return "false"
    }else{
        return "true"
    }
}
