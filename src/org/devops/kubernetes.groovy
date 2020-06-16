package org.devops

//封装对k8s的http请求，将Accept和Content-Type都设置为application/yaml，因为k8s返回的格式为yaml的
def HttpReq(reqType,reqUrl,reqBody){
    apiUrl = "https://172.17.138.183:6443/apis/apps/v1"
    withCredentials([string(credentialsId: 'k8s-admin-token', variable: 'k8stoken')]) {
        result = httpRequest consoleLogResponseBody: true, 
                             contentType: 'TEXT_HTML', 
                             customHeaders: [[maskValue: true, name: 'Authorization', value: "Bearer $k8stoken"],[maskValue: false, name: 'Content-Type', value: 'application/yaml'],[maskValue: false, name: 'Accept', value: 'application/yaml']], 
                             httpMode: "${reqType}", 
                             requestBody: "${reqBody}",
                             responseHandle: 'NONE', 
                             url: "${apiUrl}/${reqUrl}", 
                             wrapAsMultipart: false,
                             ignoreSslErrors: true
    }
    return result
}

//获取deployment
def GetDeployment(namespace,deployment){
    apiUrl = "namespaces/${namespace}/deployments/${deployment}"
    response = HttpReq("GET",apiUrl,"").content
    return response
}

//更新deployment
def UpdateDeployment(namespace,deployment,deployBody){
    apiUrl = "namespaces/{namespace}/deployments/{deployment}"
    response = HttpReq("PUT",apiUrl,deployBody)
    println(response)
}
