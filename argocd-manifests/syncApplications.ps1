[CmdletBinding()]
param(
    [Parameter(Mandatory=$true)]
    [string] $appName
)
$baseUrl="https://argocd.fibabanka.local/api/v1/applications"
$headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
$headers.Add("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcmdvY2QiLCJzdWIiOiJhenVyZWNpY2Q6YXBpS2V5IiwibmJmIjoxNjc0NDYxMjM3LCJpYXQiOjE2NzQ0NjEyMzcsImp0aSI6ImEwYmNmMWMxLTE2NzEtNDViOS1iM2ZhLTUxY2M3ZGUwYjc2NSJ9.CI-iRhzMQ_EIK5MbZ0stFdolYvhx0nsXYaxwmss2ihk")
function Get-TimeStamp {
    
    return "{0:MM/dd/yy} {0:HH:mm:ss}" -f (Get-Date)
    
}
try{
    $body = @"
{
    `"dryRun`": true,
    `"prune`": true
}
"@
    $response = Invoke-WebRequest "$($baseUrl)/$($appName)/sync" -Method 'POST' -Headers $headers -Body $body -SkipCertificateCheck
    if ($response.StatusCode -eq 200){
        Write-Host  "$(Get-TimeStamp) $($appName) named application was triggered" -ForegroundColor DarkGreen
        
    }else{
        Write-Host $response.StatusCode
        Write-Host $response.StatusDescription
    }
}catch{
    Write-Host "$(Get-TimeStamp) $($_.Exception.Message)"   -ForegroundColor Red
}