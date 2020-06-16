package org.devops

//封装Http请求
def HttpReq(reqType,reqUrl,reqBody){
    sonarServer = "http://sonarqubetest.goschainccap.com/api"
    result = httpRequest authentication: 'sonar-user', 
                         consoleLogResponseBody: true, 
                         contentType: 'APPLICATION_JSON', 
                         httpMode: reqType, 
                         ignoreSslErrors: true, 
                         responseHandle: 'NONE', 
                         url: "${sonarServer}/${reqUrl}", 
                         wrapAsMultipart: false
    return result
}

//获取质量阈状态
def GetProjectStatus(projectName){
    apiUrl = "project_branches/list?project=${projectName}"
    response = HttpReq("GET",apiUrl,"")
    //解析json数据
    response = readJSON text: "${response.content}"
    result = response["branches"][0]["status"]["qualityGateStatus"]
    
    if(result != "OK"){
        error "代码扫描质量阈错误，请仔细检查代码！"
    }
}

//搜索sonar项目，判断项目是否存在
def SearchProject(projectName){
    apiUrl = "projects/search?projects=${projectName}"
    response = HttpReq("GET",apiUrl,"") 
    response = readJSON text: """${response.content}"""
    result = response["paging"]["total"]
    return result
}

//新建项目
def CreateProject(projectName){
    apiUrl = "projects/create?name=${projectName}&project=${projectName}"
    HttpReq("POST",apiUrl,"")
}

//指定项目的质量规则，当不指定时默认是java语言，默认质量规则是系统自带的java质量规则
def ConfigQualityprofiles(projectName,lang="java",qualityprofile="Sonar%20way"){
    apiUrl = "qualityprofiles/add_project?project=${projectName}&language=${lang}&qualityProfile=${qualityprofile}"
    HttpReq("POST",apiUrl,"")
}

//获取质量阈的id
def GetGateId(gateName){
    apiUrl = "qualitygates/show?name=${gateName}"
    response = HttpReq("GET",apiUrl,"").content
    response = readJSON text: "${response}"
    result = response["id"]
    return result
}
//配置项目的质量阈
def ConfigGate(projectName,gateId){
    apiUrl = "qualitygates/select?projectKey=${projectName}&gateId=${gateId}"
    HttpReq("POST",apiUrl,"")
}
