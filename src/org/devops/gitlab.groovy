package org.devops

//封装http请求
def HttpReq(reqType,reqUrl,reqBody){
    gitlabServer = "http://gitlabtest.goschainccap.com/api/v4"
    withCredentials([string(credentialsId: 'gitlab-token', variable: 'gitlabToken')]) {
        httpRequest httpMode: "${reqType}",
                             customHeaders: [[maskValue: true, name: 'PRIVATE-TOKEN', value: "${gitlabToken}"]], 
                             //contentType: 'APPLICATION_JSON',
                             url: "${gitlabServer}/${reqUrl}",
                             requestBody: "${reqBody}",
                             wrapAsMultipart: false
    }
    //return result
    
}

//更改提交状态
def ChangeCommitStatus(projectId,commitSha,status){
    commitApi = "projects/${projectId}/statuses/${commitSha}?state=${status}"
    response = HttpReq('POST',commitApi,'')
    println("返回的结果为：" + response)
    //return response
}

//获取项目ID
def GetProjectID(repoName="",projectName){
    projectApi = "projects?search=${projectName}"
    response = HttpReq("GET",projectApi,"").content
    result = readJSON text: "${response}"
    
    for(repo in result){
        if(repo["path"] == "${projectName}"){
            repoId = repo["id"]
        }
    }
    return repoId
}
//创建分支
def CreateBranch(projectId,refBranch,newBranch){
    createBranchApi = "projects/${projectId}/repository/branches?branch=${newBranch}&ref=${refBranch}"
    repsonse = HttpReq("POST",createBranchApi,"").content
    branchInfo = readJSON text: "${response}"
}

//创建合并请求
def CreateMr(projectId,sourceBranch,targetBranch,title,assigneeUser=""){
    try {
        def mrUrl = "projects/${projectId}/merge_requests"
        def reqBody = """{"source_branch":"${sourceBranch}", "target_branch": "${targetBranch}","title":"${title}","assignee_id":"${assigneeUser}"}"""
        response = HttpReq("POST",mrUrl,reqBody).content
        return response
    } catch(e){
        println(e)
    }
}

//删除分支


//创建仓库文件
def createRepoFile(projectId,filePath,fileContent){
    apiUrl = "projects/${projectId}/repository/files/${filePath}"
    reqBody = """{"branch": "master", "content": "${fileContent}", "commit_message": "create a new file"}"""
    response = HttpReq("POST",apiUrl,reqBody)
    println(response)
}