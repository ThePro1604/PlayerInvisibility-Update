 = 'E:\NBT-Viewer\src\main\java\win\transgirls\playervisibility\commands\VisibilityCommand.java'  
 = Get-Content  -Raw -Encoding UTF8  
 =  -replace 'VersionedText\.', 'TextHelper.'  
 =  -replace 'SuggestionProvider<Object>', 'SuggestionProvider<FabricClientCommandSource>'  
Set-Content  -Value  -Encoding UTF8  
Write-Host 'Done'  
