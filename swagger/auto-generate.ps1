param (
	[Parameter(Mandatory=$true)][string]$codegen
)

$PSScriptRoot = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition
$JavaAutogenerationPath = $PSScriptRoot + "\auto-generated-java"
$TypeScriptAutogenerationPath = $PSScriptRoot + "\auto-generated-typescript"
$PathToAutogeneratedCodeInProject = $PSScriptRoot + "\..\zookeeper-ui-web\src\app\autogenerated"

Write-Host "Generating Spring Server Code..."
If (Test-Path $JavaAutogenerationPath){
	Remove-Item $JavaAutogenerationPath -Force -Recurse 
}

java -jar $codegen generate -i $PSScriptRoot\zookeeper-res-api.swagger.yml -l spring -o $JavaAutogenerationPath -c $PSScriptRoot\swagger-codegen-config.json

If (Test-Path $PSScriptRoot\..\zookeeper-rest-api\src\main\java\com\rfranco\zookeeperrestapi\autogenerated){
	Remove-Item $PSScriptRoot\..\zookeeper-rest-api\src\main\java\com\rfranco\zookeeperrestapi\autogenerated -Force -Recurse -verbose
}

Copy-Item $JavaAutogenerationPath\src\main\java\com\rfranco\zookeeperrestapi\autogenerated $PSScriptRoot\..\zookeeper-rest-api\src\main\java\com\rfranco\zookeeperrestapi\autogenerated -Recurse -verbose

Write-Host "Generating TypeScript Client Code..."
If (Test-Path $TypeScriptAutogenerationPath){
	Remove-Item $TypeScriptAutogenerationPath -Force -Recurse 
}

java -jar $codegen generate -i $PSScriptRoot\zookeeper-res-api.swagger.yml -l typescript-angular2 -o $TypeScriptAutogenerationPath 

If (Test-Path $PathToAutogeneratedCodeInProject\api){
	Remove-Item $PathToAutogeneratedCodeInProject\api -Force -Recurse -verbose
}
If (Test-Path $PathToAutogeneratedCodeInProject\model){
	Remove-Item $PathToAutogeneratedCodeInProject\model -Force -Recurse -verbose
}
Copy-Item $TypeScriptAutogenerationPath\api $PathToAutogeneratedCodeInProject\ -Recurse -verbose
Copy-Item $TypeScriptAutogenerationPath\model $PathToAutogeneratedCodeInProject\ -Recurse -verbose
Copy-Item $TypeScriptAutogenerationPath\configuration.ts $PathToAutogeneratedCodeInProject\ -verbose
Copy-Item $TypeScriptAutogenerationPath\index.ts $PathToAutogeneratedCodeInProject\ -verbose
