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
    response = readJSON text: """${response.content}"""
    result = response["paging"]["total"]
    
    if(result.toString() == "0"){
        return "false"
    }else{
        return "true"
    }
}

//当项目不存在时，创建项目
def CreateProject(projectName){
    apiUrl = "projects/create?name=${projectName}&project=${projectName}"
    response = HttpReq("POST",apiUrl,'')
    println(response)
}

//配置项目质量规则
def ConfigQualityProfiles(lang,projectName,qpname){
    apiUrl = "qualityprofiles/add_project?language=${lang}&project=${projectName}&qualityProfile=${qpname}"
    response = HttpReq("POST",apiUrl,'')
    println(response)
}
//获取质量阈的id；因为通过api配置项目的质量阈时，需要知道某个质量阈的id
def GetQualityGateId(qualityGateName){
    apiUrl = "qualitygates/show?name=${qualityGateName}"
    response = HttpReq("GET",apiUrl,'')
    response = readJSON text: """${response.content}"""
    result = response["id"]
    return result
}
//配置项目质量阈
def ConfigQualityGates(projectName,qualityGateName){
    gateId = GetQualityGateId(qualityGateName)
    apiUrl = "qualitygates/select?projectKey=${projectName}&gateId=${gateId}"
    response = HttpReq("POST",apiUrl,'')
    println(response)
}