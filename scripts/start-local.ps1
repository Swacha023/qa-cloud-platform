$ErrorActionPreference = 'Stop'
Set-Location (Join-Path $PSScriptRoot '..')
if (-not (Test-Path '.env')) { Copy-Item '.env.example' '.env' }
if (-not (Test-Path 'backend/.env')) { Copy-Item 'backend/.env.example' 'backend/.env' }
if (-not (Test-Path 'frontend/.env')) { Copy-Item 'frontend/.env.example' 'frontend/.env' }
docker compose up --build
