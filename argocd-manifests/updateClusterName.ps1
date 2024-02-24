param(
    [string] $clusterName
    [string] $projectType
)
$baseUrl="https://argocd.fibabanka.local/api/v1/applications"
$headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
$headers.Add("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcmdvY2QiLCJzdWIiOiJhenVyZWNpY2Q6YXBpS2V5IiwibmJmIjoxNjc0NDYxMjM3LCJpYXQiOjE2NzQ0NjEyMzcsImp0aSI6ImEwYmNmMWMxLTE2NzEtNDViOS1iM2ZhLTUxY2M3ZGUwYjc2NSJ9.CI-iRhzMQ_EIK5MbZ0stFdolYvhx0nsXYaxwmss2ihk")
        
try{

    $response = Invoke-WebRequest "$($baseUrl)/k8s-test" -Method 'GET' -Headers $headers  -SkipCertificateCheck
    $statusCode = $response.StatusCode
    Write-Host $statusCode
    if($statusCode -eq 200){
        $response = $response | ConvertFrom-Json
        $metaDataName=$response.metadata.name
        $project = $response.spec.project
        $env=$response.spec.source.path.replace("/.","")
        $namespace=$response.spec.destination.namespace
        $applicationName="k8s-test"
        #$datas=Get-Manifest-File($metaDataName, $projectName, $env, $namespace, $clusterName, $applicationName)
$body = @"
{
              `"metadata`": {
                `"name`": "$metaDataName"
              },
              `"spec`": {
                `"destination`": {
                  `"name`": "$clusterName",
                  `"namespace`": "$namespace"
                },
                `"name`": `"specname1`",
                `"project`": "$project",
                `"source`": {
                  `"repoURL`": `"https://fibagithub.fibabanka.com.tr/fibagit/$($applicationName)`",
                  `"path`": `"$env/.`",
                  `"targetRevision`": `"HEAD`",
                  `"directory`": {
                    `"recurse`": true
                  }
                },
                `"syncPolicy`": {
                  `"syncOptions`": [
                    `"RespectIgnoreDifferences=true`"
                  ]
                },
                `"ignoreDifferences`": [
                  {
                    `"group`": `"apps`",
                    `"kind`": `"Deployment`",
                    `"jsonPointers`": [
                      `"/spec/replicas`"
                    ]
                  }
                ]
              }
            }
"@
        Write-Host $body
        $response1 = Invoke-WebRequest "$($baseUrl)?upsert=true" -Method 'POST' -Headers $headers -Body $body -SkipCertificateCheck
        $statusCode = $response1.StatusCode
        Write-Host $statusCode
        if($statusCode -eq 200){
            Write-Host "Updated"
        }
        else{
            Write-Host "Not Updated"
        }
    }
}catch{
    Write-Host $_.Exception.Message
}