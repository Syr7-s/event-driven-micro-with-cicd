[CmdletBinding()]
param(
    [Parameter(Mandatory=$true)]
    [string] $appName
)

$baseUrl="https://argocd.fibabanka.local/api/v1/applications"
$headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
$headers.Add("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcmdvY2QiLCJzdWIiOiJhenVyZWNpY2Q6YXBpS2V5IiwibmJmIjoxNjc0NDYxMjM3LCJpYXQiOjE2NzQ0NjEyMzcsImp0aSI6ImEwYmNmMWMxLTE2NzEtNDViOS1iM2ZhLTUxY2M3ZGUwYjc2NSJ9.CI-iRhzMQ_EIK5MbZ0stFdolYvhx0nsXYaxwmss2ihk")
function Get-Applications($appName){
    $datas = Invoke-WebRequest "$($baseUrl)/$($appName)" -Method 'GET' -Headers $headers -SkipCertificateCheck
    if ($datas.StatusCode -eq 200){
        Write-Host "test"
        return $datas
    }
    Write-Host $datas.StatusCode
    Write-Host $_.Exception.Message
    exit 1
}
function Get-TimeStamp {
  return "{0:MM/dd/yy} {0:HH:mm:ss}" -f (Get-Date)
}
$clusters = @{"fiba-api-gateway"='fiba-drc-api-gateway'; "fiba-production"='fiba-drc-production';"fiba-dmz"='fiba-drc-dmz'} 

try{
    $applications = Get-Applications($appName)
    $application = $applications | ConvertFrom-Json
    $projectName=$application.spec.project
    
    $env=$application.spec.source.path.replace("/.","")
    $namespace=$application.spec.destination.namespace
    $destinationName=$application.spec.destination.name
    $clusterName="None"
    if ($appName.EndsWith("-prod")){
      $repoName=$appName.replace("-prod","")
      try{
        foreach ($cluster in $clusters.keys){
          if ($cluster -eq $destinationName){
              $clusterName=$clusters[$destinationName]
          }
        }
        $body = @"
{
          `"metadata`": {
            `"name`": "$appName"
          },
          `"spec`": {
            `"destination`": {
              `"name`": "$clusterName",
              `"namespace`": "$namespace"
            },
            `"name`": `"specname1`",
            `"project`": "$projectName",
            `"source`": {
              `"repoURL`": `"https://fibagithub.fibabanka.com.tr/fibagit/k8s-$($repoName)`",
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
        #Write-Host $body
        #$response = Invoke-WebRequest "$($baseUrl)?upsert=true" -Method 'POST' -Headers $headers -Body $body -SkipCertificateCheck
        #if($response.StatusCode -eq 200){
        #    Write-Host "$(Get-TimeStamp) $($destinationName) named cluster was replaced with $($clusterName) for $($appName)" -ForegroundColor DarkGreen
        #}
        #else{
        #    Write-Host "$(Get-TimeStamp) Not Updated for $($appName)" -ForegroundColor Red
        #}
      }catch{
        Write-Host "$(Get-TimeStamp) Unexpected error inner try-catch block. Not updated cluster from $($destinationName) to $($clusterName) for $($appName)" -ForegroundColor Yellow
        #continue
      }
  }
}
catch{
    Write-Host "$(Get-TimeStamp) $($_.Exception.Message)"   -ForegroundColor Red
}