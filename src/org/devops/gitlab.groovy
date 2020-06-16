package org.devops

//封装对gitlab的http请求
def HttpReq(reqType,reqUrl,reqBody){
    gitServer = "http://gitlabtest.goschainccap.com/api/v4"
    withCredentials([string(credentialsId: 'gitlab-token', variable: 'gitlabToken')]) {
        result = httpRequest consoleLogResponseBody: true, 
                            contentType: 'APPLICATION_JSON', 
                            customHeaders: [[maskValue: true, name: 'PRIVATE-TOKEN', value: "${gitlabToken}"]], 
                            httpMode: reqType, 
                            ignoreSslErrors: true, 
                            requestBody: reqBody,
                            responseHandle: 'NONE', 
                            url: "${gitServer}/${reqUrl}", 
                            wrapAsMultipart: false
    }
    return result
}

//更改提交状态
def ChangeCommitStatus(projectId,commitSha,status){
    apiUrl = "projects/${projectId}/statuses/${commitSha}?state=${status}"
    HttpReq("POST",apiUrl,"")
}

//通过项目名字获取项projectId，api创建分支时需要projectId
def GetProjectId(projectName){
    projectApi = "projects?search=${projectName}"
    response = HttpReq("GET",projectApi,"").content
    result = readJSON text: """${response}"""
    
    for(project in result){
        if(project["path"] == projectName){
            projectId = project["id"]
        }
    }
    return projectId
}

//通过projectId创建分支，默认从master分支创建创建
def CreateProjectBranch(projectId,branchName,srcBranch="master"){
    try{
        createBranchApi = "projects/${projectId}/repository/branches?branch=${branchName}&ref=${srcBranch}"
        HttpReq("POST",createBranchApi,"")
    }catch(e){
        println(e)
    }
}

//删除分支
def DeleteProjectBranch(projectId,branchName){
    deleteBranchApi = "projects/${projectId}/repository/branches/${branchName}"
    HttpReq("DELETE",deleteBranchApi,"")
}

//创建合并请求
def CreateMR(projectId,sourceBranch,targetBranch,title,assigneeUser=""){
    try {
        def mrUrl = "projects/${projectId}/merge_requests"
        def reqBody = """{"source_branch":"${sourceBranch}", "target_branch": "${targetBranch}","title":"${title}","assignee_id":"${assigneeUser}"}"""
        HttpReq("POST",mrUrl,reqBody).content
    } catch(e){
        println(e)
    }
}

//查找分支是否合并
def SearchProjectBranches(projectId,searchKey){
    def branchUrl =  "projects/${projectId}/repository/branches?search=${searchKey}"
    response = HttpReq("GET",branchUrl,'').content
    def branchInfo = readJSON text: """${response}"""
    
    def branches = [:]
    branches[projectId] = []
    if(branchInfo.size() ==0){
        return branches
    } else {
        for (branch in branchInfo){
            //println(branch)
            branches[projectId] += ["branchName":branch["name"],
                                    "commitMes":branch["commit"]["message"],
                                    "commitId":branch["commit"]["id"],
                                    "merged": branch["merged"],
                                    "createTime": branch["commit"]["created_at"]]
        }
        return branches
    }
}

//允许合并请求
def AcceptMr(projectId,mergeId){
    def apiUrl = "projects/${projectId}/merge_requests/${mergeId}/merge"
    HttpReq('PUT',apiUrl,'')
}

//创建仓库文件
def CreateRepoFile(projectId,filePath,fileContent){
    apiUrl = "projects/${projectId}/repository/files/${filePath}"
    reqBody = """{"branch": "master", "content": "${fileContent}", "commit_message": "create a new file","encoding":"base64"}"""
    HttpReq("POST",apiUrl,reqBody)
}

//获取文件内容
def GetRepoFile(projectId,filePath){
    apiUrl = "projects/${projectId}/repository/files/${filePath}/raw?ref=master"
    response = HttpReq("GET",apiUrl,"").content
    return response
}

//更新文件内容
def UpdateRepoFile(projectId,filePath,fileContent){
    apiUrl = "projects/${projectId}/repository/files/${filePath}"
    reqBody = """{"branch": "master", "content": "some content", "commit_message": "update file"}"""
    response = HttpReq("PUT",apiUrl,reqBody)
}
