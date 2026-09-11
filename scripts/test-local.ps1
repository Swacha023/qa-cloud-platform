$ErrorActionPreference = 'Stop'
Set-Location (Join-Path $PSScriptRoot '..')
Set-Location backend
mvn -B test
Set-Location ../frontend
npm install
npm run build
Set-Location ../test-runner
npm install
npx playwright install chromium
npm test
