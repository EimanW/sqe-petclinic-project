[CmdletBinding()]
param(
    [Parameter(Mandatory)]
    [ValidateSet('Seed', 'BeforeRestart', 'AfterRestart')]
    [string]$Stage,

    [Parameter(Mandatory)]
    [PSCredential]$AdminCredential,

    [Parameter(Mandatory)]
    [PSCredential]$VetCredential,

    [string]$StatePath
)

$ErrorActionPreference = 'Stop'
$baseUri = 'http://127.0.0.1:9966/petclinic/api'
if ($Stage -ne 'Seed' -and -not $StatePath) {
    throw 'Supply the same -StatePath for both restart stages.'
}
if ($StatePath) { $StatePath = $ExecutionContext.SessionState.Path.GetUnresolvedProviderPathFromPSPath($StatePath) }
if ($Stage -eq 'BeforeRestart' -and (Test-Path -LiteralPath $StatePath)) {
    throw "Refusing to overwrite the existing marker state file $StatePath."
}

Add-Type -AssemblyName System.Net.Http

function Invoke-ClinicRequest {
    param(
        [string]$Path,
        [int]$Expected,
        [PSCredential]$Credential,
        [string]$Method = 'GET',
        [string]$Body
    )

    $role = 'anonymous'
    $message = [System.Net.Http.HttpRequestMessage]::new([System.Net.Http.HttpMethod]::new($Method), "$baseUri$Path")
    try {
        if ($Credential) {
            $role = $Credential.UserName
            $plainText = '{0}:{1}' -f $role, $Credential.GetNetworkCredential().Password
            $encoded = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($plainText))
            $message.Headers.Authorization = [System.Net.Http.Headers.AuthenticationHeaderValue]::new('Basic', $encoded)
        }
        if ($Body) {
            $message.Content = [System.Net.Http.StringContent]::new($Body, [Text.Encoding]::UTF8, 'application/json')
        }
        $response = $client.SendAsync($message).GetAwaiter().GetResult()
        try {
            $status = [int]$response.StatusCode
            Write-Host "$Method $Path [$role] status=$status expected=$Expected"
            $content = $response.Content.ReadAsStringAsync().GetAwaiter().GetResult()
            if ($status -ne $Expected) { throw "Unexpected HTTP status for $Method $Path [$role]." }
            if ($status -eq 201) {
                Write-Host "Location=$($response.Headers.Location)"
                Write-Host "Response=$content"
            }
            if ($content -and $status -lt 300) {
                $data = ConvertFrom-Json -InputObject $content
                foreach ($item in $data) { $item }
            }
        } finally {
            $response.Dispose()
        }
    } finally {
        $message.Dispose()
    }
}

$handler = [System.Net.Http.HttpClientHandler]::new()
$handler.AllowAutoRedirect = $false
$handler.UseCookies = $false
$handler.UseProxy = $false
$client = [System.Net.Http.HttpClient]::new($handler)
$client.Timeout = [TimeSpan]::FromSeconds(10)

try {
    Write-Host "$(Get-Date -Format o) stage=$Stage backend=$baseUri"
    $null = Invoke-ClinicRequest -Path '/owners/1' -Expected 401
    $owner = Invoke-ClinicRequest -Path '/owners/1' -Expected 200 -Credential $AdminCredential
    $vets = @(Invoke-ClinicRequest -Path '/vets' -Expected 200 -Credential $VetCredential)
    $null = Invoke-ClinicRequest -Path '/owners/1' -Expected 403 -Credential $VetCredential
    $pet = Invoke-ClinicRequest -Path '/owners/1/pets/1' -Expected 200 -Credential $AdminCredential
    $types = @(Invoke-ClinicRequest -Path '/pettypes' -Expected 200 -Credential $AdminCredential)
    $cat = $types | Where-Object { $_.id -eq 1 -and $_.name -ceq 'cat' }
    if ($owner.id -ne 1 -or $owner.firstName -cne 'George' -or $owner.lastName -cne 'Franklin' -or
        @($owner.pets).Count -ne 1 -or $owner.pets[0].id -ne $pet.id -or $pet.id -ne 1 -or
        $pet.name -cne 'Leo' -or $pet.ownerId -ne $owner.id -or $pet.type.id -ne 1 -or
        $pet.type.name -cne 'cat' -or -not $cat -or -not $vets.Count) {
        throw 'The secured seed baseline differs from the documented H2 fixtures.'
    }

    $seed = [ordered]@{
        owner = [ordered]@{ id = $owner.id; firstName = $owner.firstName; lastName = $owner.lastName; petIds = @($owner.pets.id) }
        pet = $pet
        petTypes = @($types | Sort-Object id)
        vetIds = @($vets | Sort-Object id | ForEach-Object { $_.id })
    } | ConvertTo-Json -Depth 10 -Compress
    Write-Host "Seed=$seed"

    if ($Stage -eq 'BeforeRestart') {
        $marker = 'Reset-' + [guid]::NewGuid().ToString('N').Substring(0, 12)
        $payload = @{ name = $marker; birthDate = '2020-01-01'; type = @{ id = $cat.id; name = $cat.name } } |
            ConvertTo-Json -Depth 3 -Compress
        Write-Host "PetFields=$payload"
        $created = Invoke-ClinicRequest -Path "/owners/$($owner.id)/pets" -Method POST -Expected 201 -Credential $AdminCredential -Body $payload
        if ($created.id -le 0 -or $created.name -cne $marker -or $created.ownerId -ne $owner.id) {
            throw 'The Pet creation response differs from the requested marker.'
        }
        $markerPath = "/owners/$($owner.id)/pets/$($created.id)"
        @{ marker = $marker; path = $markerPath; seed = $seed } | ConvertTo-Json |
            Out-File -LiteralPath $StatePath -Encoding utf8 -NoClobber

        $stored = Invoke-ClinicRequest -Path $markerPath -Expected 200 -Credential $AdminCredential
        $updated = Invoke-ClinicRequest -Path "/owners/$($owner.id)" -Expected 200 -Credential $AdminCredential
        $association = @($updated.pets | Where-Object { $_.id -eq $created.id -and $_.name -ceq $marker })
        if ($stored.id -ne $created.id -or $stored.name -cne $marker -or $stored.ownerId -ne $owner.id -or
            $stored.birthDate -cne '2020-01-01' -or $stored.type.id -ne $cat.id -or
            $stored.type.name -cne $cat.name -or $association.Count -ne 1) {
            throw 'Independent Pet and Owner readback did not confirm the created marker.'
        }
        Write-Host "Pet readback=$($stored | ConvertTo-Json -Depth 10 -Compress)"
        Write-Host "Owner $($owner.id) lists marker ID $($created.id)."
    }

    if ($Stage -eq 'AfterRestart') {
        $before = Get-Content -LiteralPath $StatePath -Raw | ConvertFrom-Json
        if ($before.path -notmatch '^/owners/1/pets/[1-9][0-9]*$' -or $before.marker -cnotmatch '^Reset-[0-9a-f]{12}$') {
            throw 'The state file does not contain a valid marker path and name.'
        }
        $null = Invoke-ClinicRequest -Path $before.path -Expected 404 -Credential $AdminCredential
        if ($seed -cne $before.seed -or $owner.pets.name -ccontains $before.marker) {
            throw 'The initial synthetic seed baseline was not restored.'
        }
        Write-Host "Seed restored. Marker $($before.marker) is absent."
    }
} finally {
    $client.Dispose()
}
