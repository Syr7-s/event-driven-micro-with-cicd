$baseUrl="https://argocd.fibabanka.local/api/v1/applications"
$headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
$headers.Add("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcmdvY2QiLCJzdWIiOiJhenVyZWNpY2Q6YXBpS2V5IiwibmJmIjoxNjc0NDYxMjM3LCJpYXQiOjE2NzQ0NjEyMzcsImp0aSI6ImEwYmNmMWMxLTE2NzEtNDViOS1iM2ZhLTUxY2M3ZGUwYjc2NSJ9.CI-iRhzMQ_EIK5MbZ0stFdolYvhx0nsXYaxwmss2ihk")

function Get-Applications($baseUrl){
    $datas = Invoke-WebRequest $baseUrl -Method 'GET' -Headers $headers -SkipCertificateCheck
    if ($datas.StatusCode -eq 200){
        return $datas
    }
    Write-Host $datas.StatusCode
    Write-Host $_Exception.Message
    exit 1
}

try{
    $applications = Get-Applications($baseUrl)
    $applications = $applications | ConvertFrom-Json
    for ($i = 0; $i -lt $applications.items.Count; $i++) {
        <# Action that will repeat until the condition is met #>
        $application=$applications.items[$i]
        $appName=$application.metadata.name
        $clusterName="None"
        if ($appName.StartsWith("apigw-") -and $appName.EndsWith("-prod")){
            $clusterName= "fiba-api-gateway-drc"
            Write-Host "$($appName) will run in $($clusterName) in cluster"
            break
        }elseif($application.spec.destination.name.EndsWith("-dmz")){
            $clusterName="fiba-dmz-drc"
        }elseif($appName.EndsWith("-prod")){
            $clusterName="fiba-production-drc"
        }else{
            $clusterName="fiba-nonproduction-drc"
        }
        Write-Host "$($appName) will run in $($clusterName) in cluster"
        
        
    }

}catch{
    Write-Host $_Exception.Message
}