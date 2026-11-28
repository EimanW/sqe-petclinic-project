[CmdletBinding()]
param(
    [string]$ProjectRoot,
    [switch]$VerifyOnly
)

$ErrorActionPreference = 'Stop'
$origin = 'https://github.com/spring-petclinic/spring-petclinic-angular.git'
$revision = 'f497b8fd61ec9cb896aa09447263b8872fb233be'
$expectedNode = 'v24.22.3'
$expectedNpm = '11.19.1'

function Invoke-CheckedGit {
    & git.exe @args
    if ($LASTEXITCODE -ne 0) {
        throw "Git failed (exit $LASTEXITCODE): git $($args -join ' ')"
    }
}

function Assert-CleanCheckout {
    if (Invoke-CheckedGit -C $frontend status --porcelain=v1 --untracked-files=all --ignore-submodules=none) {
        throw "Angular checkout has local changes: $frontend"
    }
}

try {
    if (-not $ProjectRoot) {
        $ProjectRoot = Join-Path $PSScriptRoot '../..'
    }
    $root = (Resolve-Path -LiteralPath $ProjectRoot).ProviderPath
    if (-not (Test-Path -LiteralPath (Join-Path $root 'src/main/resources/openapi.yml') -PathType Leaf) -or
        (Invoke-CheckedGit -C $root rev-parse --show-toplevel).Replace('/', '\') -ne $root) {
        throw "Expected the PetClinic team worktree root: $root"
    }
    $frontend = Join-Path (Split-Path -Parent $root) 'spring-petclinic-angular'
    if ($frontend -eq $root) {
        throw 'The Angular checkout must be a sibling of the team worktree.'
    }
    $exists = Test-Path -LiteralPath $frontend
    if ($exists) {
        $destination = Get-Item -LiteralPath $frontend -Force
        if (-not $destination.PSIsContainer -or ($destination.Attributes -band [IO.FileAttributes]::ReparsePoint) -or
            -not (Test-Path -LiteralPath (Join-Path $frontend '.git')) -or
            (Invoke-CheckedGit -C $frontend rev-parse --show-toplevel).Replace('/', '\') -ne $frontend) {
            throw "Expected a separate Git checkout at $frontend"
        }
        if ((Invoke-CheckedGit -C $frontend remote get-url origin) -cne $origin) {
            throw "Unexpected Angular origin in $frontend"
        }
        Assert-CleanCheckout
    } elseif ($VerifyOnly) {
        throw "Angular checkout is missing: $frontend"
    }

    $node = (Get-Command node.exe -CommandType Application | Select-Object -First 1).Source
    $npm = (Get-Command npm.cmd -CommandType Application | Select-Object -First 1).Source
    $nodeVersion = & $node --version
    if ($LASTEXITCODE -ne 0 -or $nodeVersion -cne $expectedNode -or
        (Split-Path -Parent $node) -ne (Split-Path -Parent $npm)) {
        throw "Activate Node $expectedNode and its bundled npm first. Resolved Node: $node"
    }
    $npmVersion = & $npm --version
    if ($LASTEXITCODE -ne 0 -or $npmVersion -cne $expectedNpm) {
        throw "Expected npm $expectedNpm from $npm, got $npmVersion"
    }

    if (-not $exists) {
        Invoke-CheckedGit clone --no-checkout $origin $frontend
    }
    if (-not $exists -or (Invoke-CheckedGit -C $frontend rev-parse HEAD) -cne $revision) {
        if ($VerifyOnly) {
            throw "Expected Angular HEAD $revision at $frontend"
        }
        & git.exe -C $frontend cat-file -e "${revision}^{commit}"
        if ($LASTEXITCODE -ne 0) {
            Invoke-CheckedGit -C $frontend fetch --no-tags origin $revision
        }
        Invoke-CheckedGit -C $frontend checkout --detach --no-overwrite-ignore $revision
    }
    if ((Invoke-CheckedGit -C $frontend rev-parse HEAD) -cne $revision -or
        (Invoke-CheckedGit -C $frontend remote get-url origin) -cne $origin) {
        throw 'Angular revision or origin verification failed.'
    }
    Assert-CleanCheckout

    $manifest = Join-Path $frontend 'package.json'
    $lockfile = Join-Path $frontend 'package-lock.json'
    $manifestHash = (Get-FileHash -Algorithm SHA256 -LiteralPath $manifest).Hash
    $lockHash = (Get-FileHash -Algorithm SHA256 -LiteralPath $lockfile).Hash
    Write-Output "Angular $revision at $frontend ($origin)"
    Write-Output "Node $nodeVersion at $node; npm $npmVersion at $npm"
    Write-Output "package-lock.json SHA-256 before install: $lockHash"

    if (-not $VerifyOnly) {
        $modules = Get-Item -LiteralPath (Join-Path $frontend 'node_modules') -Force -ErrorAction SilentlyContinue
        if ($modules -and (-not $modules.PSIsContainer -or ($modules.Attributes -band [IO.FileAttributes]::ReparsePoint))) {
            throw 'Refusing to run npm ci with a non-directory or redirected node_modules.'
        }
        Push-Location -LiteralPath $frontend
        try {
            & $npm ci
            $installExit = $LASTEXITCODE
        } finally {
            Pop-Location
        }
        $afterHash = (Get-FileHash -Algorithm SHA256 -LiteralPath $lockfile).Hash
        Write-Output "npm ci exit: $installExit; package-lock.json SHA-256 after install: $afterHash"
        if ($afterHash -cne $lockHash -or (Get-FileHash -Algorithm SHA256 -LiteralPath $manifest).Hash -cne $manifestHash) {
            throw 'npm ci changed the Angular manifest or lockfile.'
        }
        Assert-CleanCheckout
        exit $installExit
    }
    Write-Output 'Pinned checkout and runtime verified.'
    exit 0
} catch {
    Write-Error -ErrorRecord $_ -ErrorAction Continue
    exit 1
}
