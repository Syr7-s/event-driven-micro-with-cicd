$headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
$headers.Add("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcmdvY2QiLCJzdWIiOiJhenVyZWNpY2Q6YXBpS2V5IiwibmJmIjoxNjc0NDYxMjM3LCJpYXQiOjE2NzQ0NjEyMzcsImp0aSI6ImEwYmNmMWMxLTE2NzEtNDViOS1iM2ZhLTUxY2M3ZGUwYjc2NSJ9.CI-iRhzMQ_EIK5MbZ0stFdolYvhx0nsXYaxwmss2ihk")



function Trigger-ArgoCD-Apply($repositoryName) {
    try{
        Write-Host "Repository Name : "  $repositoryName 
        $url='https://argocd.fibabanka.local/api/v1/applications/'+$repositoryName.Trim()+"/sync"
        Write-Host "Url : " $url
        $response = Invoke-WebRequest $url -Method 'POST' -Headers $headers -SkipCertificateCheck
        $statusCode = $response.StatusCode
        Write-Host "Status Code : " $statusCode
        if ($statusCode -eq 200){
            Write-Host $repositoryName " named app was triggered"
        }else{
            Write-Host $response.StatusCode
            Write-Host $response.StatusDescription
        }
    }
    catch {
        Write-Host "An error occurred"
        Write-Host $_
        exit 1
    }
}

function check-pod-running($repositoryName) {
    try{

       $url='https://argocd.fibabanka.local/api/v1/applications/'+$repositoryName.Trim()+"/resource-tree"

        $response = Invoke-WebRequest $url -Method 'GET' -Headers $headers -SkipCertificateCheck
        $statusCode = $response.StatusCode
        if ($statusCode -eq 200){
            $response = $response | ConvertFrom-Json
            $datas = $response.nodes
            $find = $false
            foreach ($data in $datas) {

                if ($data.kind -eq "Pod"){
                    if ($data.info[0].value -eq "Running") {
                        $find = $true
                        return $find 
                    }
                }
            }
            if (-not $find){
                Write-Host "pod is not running, check your container logs"
                exit 1
            }
        }else{
            Write-Host $response.StatusCode
            Write-Host $response.StatusDescription
            exit 1
        }
    }
    catch {
        Write-Host "An error occurred"
        Write-Host $_
        exit 1
    }
}

try {
    $repositoryName=$args[0]
    Write-Host "You can check app this url : https://argocd.fibabanka.local/applications/argocd/$($repositoryName)?view=tree&resource="
    Trigger-ArgoCD-Apply($repositoryName)
    Start-Sleep -Seconds 7
    $isSync=$false
    $url='https://argocd.fibabanka.local/api/v1/applications/'+$repositoryName.Trim()
    Write-Host "Url : " $url
    $counter = 0
    $countOfOutSync = 0
    $time = 40
    while (-not $isSync) {
        Start-Sleep -Second $time 
        $response = Invoke-WebRequest $url -Method 'GET' -Headers $headers -SkipCertificateCheck
        $statusCode = $response.StatusCode
        Write-Host "Status Code : " $statusCode
        if ($statusCode -eq 200){
            $response = $response | ConvertFrom-Json
            Write-Host "Sync Status : " $response.status.sync.status
            Write-Host "Health Status : " $response.status.health.status
            Write-Host "Operation Status : " $response.status.operationState.phase
            if ($response.status.operationState.phase -ne "Succeeded"){
                Write-Host $response.status.operationState.message
                Write-Host "You can check app this url : https://argocd.fibabanka.local/applications/argocd/$($repositoryName)?view=tree&resource="
                Write-Host "Please check on argocd, here is the document : https://atlas.fibabanka.local/confluence/pages/viewpage.action?pageId=67211903&preview=/67211903/67213773/argocd-demo.mp4"
                exit 1
            }
            if ($response.status.health.status -eq 'Healthy'){
                $isSync = $true
                if ($counter -le 2){
                    $isSync = $false
                    $counter ++
                    Write-Host "Counter " $counter
                    Start-Sleep -Seconds 2
                    if($response.status.sync.status -eq 'OutOfSync'){
                        if ($countOfOutSync -eq 2){
                            check-pod-running($repositoryName)
                            return
                        }
                        $countOfOutSync++
                    }
                    $time = 30
                    continue
                }
                check-pod-running($repositoryName)
                return
            }elseif($response.status.health.status -eq 'Degraded'){
                $isSync = $true
                Write-Host "Health Status : " $response.status.health.status
                Write-Host "App is not working.Check your container log"
                Write-Host "Click pods and logs."
                Write-Host "You can check app this url : https://argocd.fibabanka.local/applications/argocd/$($repositoryName)?view=tree&resource="
                exit 1
            }
            $counter = 0
        }else{
            Write-Host "Sync Status : " $response.status.sync.status
            Write-Host $response.StatusCode
            Write-Host $response.StatusDescription
            Write-Host "You can check app this url : https://argocd.fibabanka.local/applications/argocd/$($repositoryName)?view=tree&resource="
            exit 1
        }
        
    }
}
catch {
    Write-Host "An error occurred"
    Write-Host $_
    exit 1
}