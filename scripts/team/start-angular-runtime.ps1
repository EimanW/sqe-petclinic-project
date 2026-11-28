[CmdletBinding()]
param(
    [Parameter(Mandatory)]
    [ValidateSet('Direct', 'Proxy')]
    [string]$Mode
)

$ErrorActionPreference = 'Stop'
$root = (Resolve-Path -LiteralPath (Join-Path $PSScriptRoot '../..')).ProviderPath
$frontend = Join-Path (Split-Path -Parent $root) 'spring-petclinic-angular'

# The setup verifier exits explicitly, so run it in a child shell.
& powershell.exe -NoProfile -File (Join-Path $PSScriptRoot 'setup-angular.ps1') -VerifyOnly
if ($LASTEXITCODE -ne 0) { throw 'The pinned Angular checkout or toolchain failed verification.' }

$revision = & git.exe -C $frontend rev-parse HEAD
if ($LASTEXITCODE -ne 0) { throw 'Could not read the verified checkout revision.' }
$lockHash = (Get-FileHash -LiteralPath (Join-Path $frontend 'package-lock.json')).Hash
if ($lockHash -cne '37D3D1196E6D9F9C3FC754DFDF69F7C52E610ECEB9C6B80FEB17407441FA6C92') {
    throw 'Unexpected pinned Angular lockfile bytes.'
}

$modules = Join-Path $frontend 'node_modules'
$cli = 'node_modules/@angular/cli/bin/ng.js'
foreach ($package in @{ '@angular/core' = '22.1.0'; '@angular/cli' = '22.1.0'; 'typescript' = '6.0.3' }.GetEnumerator()) {
    $metadata = Get-Content -LiteralPath (Join-Path $modules "$($package.Key)/package.json") -Raw | ConvertFrom-Json
    if ($metadata.version -cne $package.Value) { throw "Unexpected installed $($package.Key) version." }
    Write-Output "$($package.Key) $($metadata.version)"
}

if (netstat.exe -ano -p TCP | Select-String '^\s*TCP\s+\S+:4200\s+\S+\s+LISTENING') {
    throw 'Port 4200 is already in use.'
}

$workspace = $frontend
$serveArgs = @('serve', '--host', '127.0.0.1', '--port', '4200', '--watch=false')
if ($Mode -eq 'Proxy') {
    $workspace = Join-Path ([IO.Path]::GetTempPath()) "sqe-area3-angular-$([guid]::NewGuid().ToString('N'))"
    New-Item -ItemType Directory -Path $workspace | Out-Null
    $archive = Join-Path $workspace 'pinned-source.tar'
    & git.exe -C $frontend archive -o $archive $revision
    if ($LASTEXITCODE -ne 0) { throw 'Could not archive the pinned Angular tree.' }
    & tar.exe -xf $archive -C $workspace
    if ($LASTEXITCODE -ne 0) { throw 'Could not extract the pinned Angular tree.' }
    Remove-Item -LiteralPath $archive
    if ((Get-FileHash -LiteralPath (Join-Path $workspace 'package-lock.json')).Hash -cne $lockHash) {
        throw 'The evaluation tree lockfile differs from the verified checkout.'
    }

    $environment = Join-Path $workspace 'src/environments/environment.ts'
    $source = [IO.File]::ReadAllText($environment)
    $absoluteUrl = 'http://localhost:9966/petclinic/api/'
    if ([regex]::Matches($source, [regex]::Escape($absoluteUrl)).Count -ne 1) {
        throw 'Expected exactly one pinned development API URL.'
    }
    [IO.File]::WriteAllText($environment, $source.Replace($absoluteUrl, '/petclinic/api/'))
    # Angular's Windows CSS bundler resolves real paths. Copy the installed tree into
    # the disposable workspace so dependencies and source resolve under the same root.
    & robocopy.exe $modules (Join-Path $workspace 'node_modules') /E /XJ /MT:8 /R:1 /W:1 /NFL /NDL /NJH /NJS /NP
    if ($LASTEXITCODE -ge 8) { throw "Copying the installed Angular dependencies failed (robocopy $LASTEXITCODE)." }
    Write-Output "Reused installed dependencies (robocopy exit $LASTEXITCODE)."
    $serveArgs += @('--proxy-config', (Join-Path $root 'e2e/ui-setup/proxy.conf.json'))
}

Write-Output "Angular mode=$Mode workspace=$workspace browserOrigin=http://localhost:4200"
Push-Location -LiteralPath $workspace
$previousCI = $env:CI
try {
    # Angular's default local cache is disabled in CI. Keep writes out of the sibling checkout.
    $env:CI = 'true'
    & node.exe $cli @serveArgs
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
    throw 'Angular returned before the foreground server was stopped.'
} finally {
    $env:CI = $previousCI
    Pop-Location
}
