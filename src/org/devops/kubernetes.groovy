package org.devops

//封装对k8s的http请求
def HttpReq(reqType,reqUrl,reqBody){
    apiUrl = "https://172.17.138.183:6443/apis/apps/v1"
    withCredentials([string(credentialsId: 'k8s-admin-token', variable: 'k8stoken')]) {
        result = httpRequest consoleLogResponseBody: false, 
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

/*def HttpReq(reqType,reqUrl,reqBody){
    def apiServer = "https://172.17.138.183:6443/apis/apps/v1"
    withCredentials([string(credentialsId: 'k8s-admin-token', variable: 'k8stoken')]) {
      result = httpRequest customHeaders: [[maskValue: true, name: 'Authorization', value: "Bearer ${k8stoken}"],
                                           [maskValue: false, name: 'Content-Type', value: 'application/yaml'], 
                                           [maskValue: false, name: 'Accept', value: 'application/yaml']], 
                httpMode: reqType, 
                consoleLogResponseBody: true,
                ignoreSslErrors: true, 
                requestBody: reqBody,
                url: "${apiServer}/${reqUrl}"
                //quiet: true
    }
    return result
}*/

//获取deployment
def GetDeployment(namespace,deployment){
    apiUrl = "namespaces/${namespace}/deployments/${deployment}"
    response = HttpReq("GET",apiUrl,"").content
    result = readYaml text: """${response}"""
    println(result)
}
