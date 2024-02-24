[CmdletBinding()]
param(
    [Parameter(Mandatory=$true)]
    [string] $clusterName,
    [Parameter(Mandatory=$true)]
    [string] $projectType
)
$baseUrl="https://argocd.fibabanka.local/api/v1/applications"
$headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
$headers.Add("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcmdvY2QiLCJzdWIiOiJhenVyZWNpY2Q6YXBpS2V5IiwibmJmIjoxNjc0NDYxMjM3LCJpYXQiOjE2NzQ0NjEyMzcsImp0aSI6ImEwYmNmMWMxLTE2NzEtNDViOS1iM2ZhLTUxY2M3ZGUwYjc2NSJ9.CI-iRhzMQ_EIK5MbZ0stFdolYvhx0nsXYaxwmss2ihk")
# non-production
# api-gateway prod
# dmz
# production   
# tek bir paketin tüm applications'larında cluster güncellenmesi yapılabilmesi
function get-Applications($projectType){
    $datas = Invoke-WebRequest $baseUrl -Method 'GET' -Headers $headers -SkipCertificateCheck
    if ($datas.StatusCode -eq 200){
        return $datas
    }
    Write-Host $datas.StatusCode
    Write-Host $_Exception.Message
    exit 1
}

try {
        #$applications = [System.Collections.ArrayList]::new()
        $applications= New-Object System.Collections.Generic.List[System.Object]
        switch ($projectType) {
            "apigw" { 
                $datas = get-Applications($projectType)
                $datas = $datas | ConvertFrom-Json
                for ($i = 0; $i -lt $datas.items.Count; $i++) {
                    <# Action that will repeat until the condition is met #>
                    $appName=$datas.items[$i]
                    $appName=$appName.metadata.name
                    if ($appName.StartsWith("apigw-") -and $appName.EndsWith("-prod")){
                        $applications.Add($appName)
                    } 
                }
                break
             }
            "dmz" {
                $datas = get-Applications($projectType)
                $datas = $datas | ConvertFrom-Json
                for ($i = 0; $i -lt $datas.items.Count; $i++) {
                    <# Action that will repeat until the condition is met #>
                    $appName=$datas.items[$i]
                    $destinationName=$appName.spec.destination.name
                    if ($destinationName.EndsWith("-dmz")){
                        $applications.Add($appName.metadata.name)
                    } 
                }
                break
            }
            "prod" {
                $datas = get-Applications($projectType)
                $datas = $datas | ConvertFrom-Json
                for ($i = 0; $i -lt $datas.items.Count; $i++) {
                    <# Action that will repeat until the condition is met #>
                    $appName=$datas.items[$i]
                    if (-not $appName.spec.destination.name.EndsWith("-dmz")){
                        $appName=$appName.metadata.name
                        if (-not $appName.StartsWith("apigw-") -and $appName.EndsWith("-prod")){
                            $applications.Add($appName)
                        } 
                    }
                }
                break
            }
            "non-prod" {
                $datas = get-Applications($projectType)
                $datas = $datas | ConvertFrom-Json
                for ($i = 0; $i -lt $datas.items.Count; $i++) {
                    <# Action that will repeat until the condition is met #>
                    $appName=$datas.items[$i]
                    $appName=$appName.metadata.name
                    if (-not $appName.EndsWith("-prod")){
                        $applications.Add($appName)
                    } 
                }
                break
            }
            Default {
                Write-Host "Not used reguest"
                exit 1
            }
        }
        foreach($appName in $applications){
           Write-Host $appName
        }
    
}
catch {
    Write-Host $_Exception.Message
}