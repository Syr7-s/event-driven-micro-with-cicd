import requests,json

baseUrl =  'https://argocd.fibabanka.local/api/v1/applications'
headers = {
  'Authorization': 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcmdvY2QiLCJzdWIiOiJhenVyZWNpY2Q6YXBpS2V5IiwibmJmIjoxNjY5NzMyNzk2LCJpYXQiOjE2Njk3MzI3OTYsImp0aSI6ImMyYmQ3MzM5LTY2NzAtNDQzYS1iMzM1LWEzMDM0ZTA0MGQ2MSJ9.cjvE1nTxa5A45JHpLDupJyycvT25clPdz9zTYQhpI5Q'
}

testClusterName = "fiba-production-drc"
def get_manifest_info(metaDataName,projectName,env,namespace,clusterName,applicationName):
    return {
          "metadata": {
            "name": metaDataName
          },
          "spec": {
            "destination": {
              "name": clusterName,
              "namespace": namespace
            },
            "name": "specname1",
            "project": projectName,
            "source": {
              "repoURL": "https://fibagithub.fibabanka.com.tr/fibagit/{}".format(applicationName),
              "path": "{}/.".format(env),
              "targetRevision": "HEAD",
              "directory": {
                "recurse": True
              }
            },
            "syncPolicy": {
              "syncOptions": [
                "RespectIgnoreDifferences=true"
              ]
            },
            "ignoreDifferences": [
              {
                "group": "apps",
                "kind": "Deployment",
                "jsonPointers": [
                  "/spec/replicas"
                ]
              }
            ]
          }
        }

def get_manifest_info_from_argocd():
    url = baseUrl+"/k8s-test"

    payload = {}

    response = requests.request("GET", url, headers=headers, data=payload,verify=False)
    datas = response.json()
    metaDataName = datas['metadata']['name']
    specData = datas['spec']
    project = specData['project']
    env = str(specData['source']['path']).replace("/.","")
    namespace = specData['destination']['namespace']
    clusterName = testClusterName
    print(f'Project: {project} - Env: {env} - Namespace: {namespace} - ClusterName: {clusterName}')

    #with open('manifest.json', 'r') as file :
    #    devOps = file.read()
    #devOps = datas
    #devOps = devOps.replace("${applicationName}","k8s-test")
    #devOps = devOps.replace("${metaDataName}",metaDataName)
    #devOps = devOps.replace("${projectName}",project)
    #devOps = devOps.replace("${env}",env)
    #devOps = devOps.replace("${namespace}",namespace)
    #devOps = devOps.replace("${clusterName}",clusterName)
    datas = get_manifest_info(metaDataName,project,env,namespace,clusterName,"k8s-test")
    payload = json.dumps(datas)
    print(payload)
    url = "https://argocd.fibabanka.local/api/v1/applications?upsert=true"
    response = requests.request("POST",url=url,headers=headers,data=payload,verify=False)
    print(response.status_code)

#get_manifest_info_from_argocd()


def getAllApplications():
    payload = {}
    response = requests.request("GET", baseUrl, headers=headers, data=payload,verify=False)
    datas = json.loads(response.text)["items"]
    for i in range(len(datas)):
        appName = datas[i]["metadata"]["name"]
        if not appName.endswith("-prod"):
            print(appName + " - "+datas[i]["spec"]["destination"]["name"])

getAllApplications()