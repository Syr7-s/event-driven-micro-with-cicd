param(
    [Parameter(Position = 0)]
    [string[]]$applicationNames
    )
if($applicationNames.Count -gt 0){
    foreach($appName in $applicationNames.Split(",")){
        Write-Host $appName
    }
}else{
    if (Test-Path applicationLists.txt){
        $applicationList=Get-Content applicationLists.txt
        foreach($app in $applicationList){
            Write-Host $app
        }
    }else{
        Write-Host "All prod application update"
    }
}