$clusters = @{"fiba-apigateway"='fiba-drc-api-gateway'; "fiba-production"='fiba-drc-production';"fiba-dmz"='fiba-drc-dmz'} 
foreach ($cluster in $clusters.keys){
    if ($cluster -eq "fiba-dmz"){
        Write-Host $($clusters[$cluster])
    }
}