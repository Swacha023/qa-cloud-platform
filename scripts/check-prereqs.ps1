$ErrorActionPreference = 'Continue'
$tools = @('git','java','mvn','node','npm','docker')
foreach ($tool in $tools) {
  $cmd = Get-Command $tool -ErrorAction SilentlyContinue
  if ($cmd) { Write-Host "[OK] $tool -> $($cmd.Source)" }
  else { Write-Host "[MISSING] $tool" }
}
Write-Host "`nJava version:"; java -version
Write-Host "`nNode version:"; node -v
Write-Host "`nDocker version:"; docker --version
