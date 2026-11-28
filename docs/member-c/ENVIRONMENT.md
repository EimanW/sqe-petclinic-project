# Member C local environment runbook

Primary shell is Windows PowerShell 5.1 or newer. The 0.2 inventory is a historical read-only snapshot. Phase 1.1 execution is recorded under [Angular acquisition and compatibility](#angular-acquisition-and-compatibility). Backend startup, authentication, scanning and reset commands are for their named future phases. Expected HTTP responses come from source.

## Toolchain on 2026-11-28

| Tool | Detected | Requirement and compatibility | Verification | Installation and phase |
| --- | --- | --- | --- | --- |
| Git | 2.51.2.windows.1 | Local reads worked. The project requires preserved history and a pin. | `git --version` and `git ls-remote` | Reuse in Phase 0 onward. Hosted access was pending at the 0.2 audit. |
| Java / `JAVA_HOME` | Microsoft OpenJDK 21.0.12.1. `JAVA_HOME` resolves to the installed JDK and `bin/java.exe` exists. | Java 21 matches CI. Maven and application execution are pending. | `java -version` and the JDK check below | Reuse in Phase 1. |
| Maven wrapper | Tracked wrapper JAR and launchers. Wrapper 3.3.4 configured for Maven 3.10.0. Standalone `mvn` was not found on PATH. | Repository pin. Successful wrapper bootstrap and parent/dependency resolution are pending. | `Get-Content .mvn/wrapper/maven-wrapper.properties`, then `.\mvnw.cmd -v` in Phase 1 | Wrapper downloads the pinned Maven distribution if uncached. Reuse wrapper. |
| Node.js / npm | Node 24.11.0 and npm 11.6.1 | The Angular branch manifest specifies 24.x. Angular 22 on the Node 24 line requires 24.15.0 or newer. Local Node is below that floor. Derive any additional floor from the approved checkout’s lockfile. | `node --version`, `npm.cmd --version` and manifest/lockfile inspection | In Phase 1 select an exact supported 24.x release for the approved Angular 22 checkout, then record the npm version used. Reuse npm if it satisfies the locked setup. |
| Python | 3.14.0 | Optional for local validation or team tooling. No new Member C package requirement has been selected. | `python --version` | Reuse as needed. Coordinate mutation tooling with A. |
| Docker / Compose | Docker CLI 29.9.2 and Compose v5.6.1. `docker info` could not reach `dockerDesktopLinuxEngine`. | Repository Compose defines optional SQL services. Engine and images remain unverified. | `docker --version`, `docker compose version`, `docker info --format '{{.ServerVersion}}'` | Reuse installed CLI and Compose. Start the engine only for an approved durable-storage demonstration. |
| Playwright / Chromium | No Playwright command on PATH. The user cache contains Chromium and headless shell build 1234, and `chrome.exe` exists in the Chromium cache. | Assignment requires team Playwright coverage. Choose exact `@playwright/test` and its matched Chromium build for the team suite. Cache presence alone gives no launch result. | From the team suite, `.\node_modules\.bin\playwright.cmd --version`, `.\node_modules\.bin\playwright.cmd install --list` and a local browser smoke | Phase 1. Use the matched cache or download Chromium with the locked CLI. |
| Lighthouse | CLI not found on PATH. A system Chrome executable path exists. | Assignment requires Accessibility, Best Practices and SEO. Choose a locked Lighthouse version and a compatible browser. | In the future tool package, `.\node_modules\.bin\lighthouse.cmd --version` and verify the intended screen in a report | Phase 5. Install locally through a pinned package and lockfile. |
| axe-core | No project-owned package detected. | Assignment requires full axe-core scans. Use pinned `@axe-core/playwright` with the team browser suite. | `npm.cmd ls @axe-core/playwright axe-core --depth=1` from the future suite, then verify a screen scan | Phase 5. Add to the team lockfile. |
| OWASP ZAP | `zap` and `zap.bat` not found on PATH. | Assignment requires passive evidence for both local workflows. Select an official pinned release and a compatible Java runtime. | Invoke the installed `zap.bat -version` by its local path during Phase 6 setup. | Phase 6. Official Windows download and local browser/proxy setup. |
| OWASP Dependency-Check | CLI not found on PATH. | Assignment dependency audit. Pin the official tool or Maven plugin with A and record the advisory data date. | `.\dependency-check.bat --version` from the selected CLI's `bin` directory | Phase 6. Official CLI release or approved pinned Maven plugin. Advisory data download and any service key require local setup. |
| Grafana k6 OSS | `k6.exe` 2.2.0, commit `00a9a1b7f5`, Windows amd64 | Release commit matches Grafana’s official release. The CLI exposes VUs, duration, stages and summary output. Grafana documents constant VUs and metrics for rate, failure and latency. Load execution remains pending. | `k6 version`, `k6 run --help` and `k6 inspect --help`. In Phase 7 use `k6 inspect` on the eventual script and run a local smoke. | Phase 7. Reuse existing k6. |

Official references for version selection and installation. [Maven Wrapper](https://maven.apache.org/tools/wrapper/) · [Angular version matrix](https://angular.dev/reference/versions) · [Node downloads](https://nodejs.org/en/download) · [Playwright browsers](https://playwright.dev/docs/browsers) · [Lighthouse](https://developer.chrome.com/docs/lighthouse/overview) · [axe-core for Playwright](https://github.com/dequelabs/axe-core-npm/tree/develop/packages/playwright) · [ZAP](https://www.zaproxy.org/download/) · [Dependency-Check](https://jeremylong.github.io/DependencyCheck/dependency-check-cli/index.html) · [Grafana k6 2.2.0](https://github.com/grafana/k6/releases/tag/v2.2.0) · [Constant VUs](https://grafana.com/docs/k6/latest/using-k6/scenarios/executors/constant-vus/).

## Backend setup and seed check

**Proposed Phase 1 commands.** Use a dedicated PowerShell window for the backend. Check the repo path, branch and revision against the reviewed handover before proceeding. Record the tested HEAD for every run. Maven startup can download dependencies and compile generated interfaces. Bind to loopback for the local run.

```powershell
Set-Location -LiteralPath 'D:\SQE-Project\sqe-petclinic-project'
git rev-parse --show-toplevel
git remote get-url origin
git status --short --branch
if ((git branch --show-current) -ne 'feat/area3-24i-3014') { throw 'Unexpected branch' }
git rev-parse HEAD 'baseline^{commit}'
git merge-base --is-ancestor baseline HEAD
if ($LASTEXITCODE -ne 0) { throw 'Baseline is missing from HEAD history' }

Get-Command java
java -version
$env:JAVA_HOME
if (-not $env:JAVA_HOME -or -not (Test-Path (Join-Path $env:JAVA_HOME 'bin\java.exe'))) {
    throw 'JAVA_HOME must point to the Java 21 JDK'
}
& (Join-Path $env:JAVA_HOME 'bin\java.exe') -version
Get-Item .\mvnw.cmd, .\.mvn\wrapper\maven-wrapper.jar
Get-Content -LiteralPath .\.mvn\wrapper\maven-wrapper.properties
.\mvnw.cmd -v
if ($LASTEXITCODE -ne 0) { throw 'Maven wrapper bootstrap failed' }
.\mvnw.cmd spring-boot:run '-Dspring-boot.run.profiles=h2,spring-data-jpa' '-Dspring-boot.run.arguments=--server.address=127.0.0.1'
```

In a second PowerShell window, use source-reported synthetic pairs `admin/admin` and `vet/admin` at the prompts. Carry credentials only in the local session or approved local test-secret input. Keep API headers and passwords out of logs, traces, scripts and the repository. The PowerShell cmdlet below works on Windows PowerShell 5.1 with `-UseBasicParsing`. If using curl manually, spell it `curl.exe` to avoid PowerShell's `curl` alias and use `--user admin` or `--user vet` to prompt for a password.

**Proposed Phase 1 commands.** Verify authentication, role decisions and seed data with read-only requests. Expected results are 401 unauthenticated, 200 for admin owner read, 200 for vet clinic-directory read and 403 for vet owner read.

```powershell
$api = 'http://localhost:9966/petclinic/api'
$adminCredential = Get-Credential -UserName admin -Message 'Enter the local synthetic admin password'
$vetCredential = Get-Credential -UserName vet -Message 'Enter the local synthetic vet password'
function New-PetclinicBasicHeader([pscredential] $Credential) {
    $pair = '{0}:{1}' -f $Credential.UserName, $Credential.GetNetworkCredential().Password
    @{ Authorization = 'Basic ' + [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($pair)) }
}
function Get-PetclinicStatus([string] $Uri, [hashtable] $Headers = @{}) {
    try {
        [int](Invoke-WebRequest -UseBasicParsing -Uri $Uri -Headers $Headers -ErrorAction Stop).StatusCode
    } catch {
        if (-not $_.Exception.Response) { throw }
        [int]$_.Exception.Response.StatusCode
    }
}
$adminHeaders = New-PetclinicBasicHeader $adminCredential
$vetHeaders = New-PetclinicBasicHeader $vetCredential
if ((Get-PetclinicStatus "$api/owners/1") -ne 401) { throw 'Expected unauthenticated 401' }
if ((Get-PetclinicStatus "$api/owners/1" $adminHeaders) -ne 200) { throw 'Expected admin owner read 200' }
if ((Get-PetclinicStatus "$api/vets" $vetHeaders) -ne 200) { throw 'Expected vet directory read 200' }
if ((Get-PetclinicStatus "$api/owners/1" $vetHeaders) -ne 403) { throw 'Expected vet owner read 403' }

$seedOwner = Invoke-RestMethod -Uri "$api/owners/1" -Headers $adminHeaders
$seedPet = Invoke-RestMethod -Uri "$api/owners/1/pets/1" -Headers $adminHeaders
$seedTypes = Invoke-RestMethod -Uri "$api/pettypes" -Headers $adminHeaders
if ($seedOwner.id -ne 1 -or $seedOwner.lastName -ne 'Franklin' -or
    $seedPet.name -ne 'Leo' -or $seedPet.ownerId -ne 1 -or
    -not ($seedTypes | Where-Object { $_.id -eq 1 -and $_.name -eq 'cat' })) {
    throw 'H2 seed readback differs from source'
}
Start-Process 'http://localhost:9966/petclinic/swagger-ui.html'
```

When opening Swagger UI, use the local admin pair if the browser prompts. Confirm the UI renders and loads its API definition. The configured security chain requires authentication for all requests. Capture a sanitized status or screenshot. See [root README](../../readme.md), [main application properties](../../src/main/resources/application.properties) and [H2 seed](../../src/main/resources/db/h2/data.sql).

**Proposed Phase 1 commands.** Create an isolated reset marker through the ordinary W1 API after the auth and seed checks. Keep the second window open so the marker can be checked after restart.

```powershell
$marker = 'Reset-' + [guid]::NewGuid().ToString('N').Substring(0, 12)
$payload = @{
    name = $marker
    birthDate = '2020-01-01'
    type = @{ id = 1; name = 'cat' }
} | ConvertTo-Json -Depth 3
$createdResponse = Invoke-WebRequest -UseBasicParsing -Uri "$api/owners/1/pets" -Method Post -Headers $adminHeaders -ContentType 'application/json' -Body $payload
if ($createdResponse.StatusCode -ne 201) { throw 'Expected pet create 201' }
$created = $createdResponse.Content | ConvertFrom-Json
$markerUrl = "$api/owners/1/pets/$($created.id)"
$beforeRestart = Invoke-RestMethod -Uri $markerUrl -Headers $adminHeaders
if ($beforeRestart.name -ne $marker -or $beforeRestart.ownerId -ne 1) {
    throw 'Reset marker did not persist in the original process'
}
```

In the backend window press **Ctrl+C** and confirm the wrapper’s batch prompt if shown. Wait for the foreground process to exit. From the second window, run `Test-NetConnection -ComputerName localhost -Port 9966 -InformationLevel Quiet` and expect `False`. If the port is still open, identify the listener with `Get-NetTCPConnection -LocalPort 9966 -State Listen` and verify ownership before taking action. Restart using the same wrapper startup command in the backend window. Then use the second window to verify the seed and marker.

**Proposed Phase 1 commands after restart.**

```powershell
$ownerAfterRestart = Invoke-RestMethod -Uri "$api/owners/1" -Headers $adminHeaders
$petAfterRestart = Invoke-RestMethod -Uri "$api/owners/1/pets/1" -Headers $adminHeaders
if ($ownerAfterRestart.lastName -ne 'Franklin' -or $petAfterRestart.name -ne 'Leo' -or
    @($ownerAfterRestart.pets | Where-Object { $_.name -eq $marker }).Count -ne 0 -or
    (Get-PetclinicStatus $markerUrl $adminHeaders) -ne 404) {
    throw 'Expected original H2 seed without the reset marker'
}
Remove-Variable adminCredential, vetCredential, adminHeaders, vetHeaders
```

## Database strategy

- **Primary Phase 1 proposal.** Use H2 with `spring-data-jpa` and security enabled for browser, role and workflow checks. `jdbc:h2:mem:petclinic` uses `DB_CLOSE_DELAY=-1` and `DB_CLOSE_ON_EXIT=FALSE`. Connections can close while the database survives in the JVM. Ending that JVM loses the database. Startup always initializes schema and synthetic seed rows from the selected platform. Verify that behavior with the marker check above. The Java test resources select HSQLDB by default. See [H2 properties](../../src/main/resources/application-h2.properties) and [test properties](../../src/test/resources/application.properties).
- **Test isolation.** Start from the seed per suite or equivalent isolated instance. Use unique synthetic records per case, pass the returned W1 Pet ID to W2, and serialize tests sharing a write fixture or give each worker its own instance. After successful writes, use independent reads of owner/pet and visit to compare IDs, values and record counts. For rejected writes, compare before and after snapshots of the relevant records and counts. For each measured k6 run, restart the same build, verify seeds, recreate identical fixtures and run the same preparation before measurement.
- **Recovery.** Coordinate a separate Java integration configuration with Member A. Use an isolated real datasource, drive a representative write under a controlled failure and read committed rows outside the failed transaction with fresh queries. Restore the dependency, retry, then verify exactly one intended row and the correct foreign key. Confirm the selected profile explicitly so test-resource HSQLDB does not silently substitute for the intended database.
- **Durable-storage evidence.** The brief requires persistent storage. H2 demonstrates relational write/readback within a process. Plan a separate PostgreSQL durability demonstration and confirm the eventual strategy with the team and instructor. Use the existing profile and Compose service, loopback-only port exposure and an approved named volume. Preserve security, create a unique Pet and Visit, restart the backend, then stop and restart or recreate the DB container with the same volume. Read back both IDs and associations after each restart and record the volume and profile. Use a fresh isolated volume for a new baseline. Any Compose override or shared change needs approval.
- **Existing SQL alternatives.** Compose defines `postgres:16.3` on port 5432 and `mysql:8.4` on port 3306. The checked-in Compose file declares no named database data volumes. The PostgreSQL and MySQL profiles accept `POSTGRES_URL/USER/PASS` and `MYSQL_URL/USER/PASS` and set SQL initialization to `always`. PostgreSQL seed inserts are conditional, and its schema issues unnamed `CREATE INDEX` statements, so verify repeated initialization and index behavior. For an approved durability run, initialize the empty volume once and consider passing `--spring.sql.init.mode=never` after the first successful initialization to measure persistence without reseeding. MySQL uses `INSERT IGNORE` in its seed. Both seeds and the HSQLDB seed contain admin with `OWNER_ADMIN`, `VET_ADMIN` and `ADMIN`. Those three seeds have no separate vet login. H2 is the primary proposal for two-account evidence. See [Compose](../../docker-compose.yml), [PostgreSQL profile](../../src/main/resources/application-postgres.properties), [PostgreSQL schema](../../src/main/resources/db/postgres/schema.sql), [PostgreSQL seed](../../src/main/resources/db/postgres/data.sql), [MySQL profile](../../src/main/resources/application-mysql.properties), [MySQL seed](../../src/main/resources/db/mysql/data.sql) and [HSQLDB seed](../../src/main/resources/db/hsqldb/data.sql).

## Angular acquisition and compatibility

### Phase 1.1 pinned checkout

On 2026-11-28, Git 2.51.2.windows.1 cloned `https://github.com/spring-petclinic/spring-petclinic-angular.git` into `D:\SQE-Project\spring-petclinic-angular` and checked out `f497b8fd61ec9cb896aa09447263b8872fb233be` detached. The directory was absent on entry and clean after checkout. Commit `up Bootstrap version to 5.3 (#179)` is by Vitaliy Fedoriv, authored and committed 2026-08-06 09:15:39 +03:00, parent `122c227e7abc545427627677c9410b7621983c4e`. Backend source is at `e182f1333f3d1e9cd55d677839816905a17f4db2`. [Upstream commit](https://github.com/spring-petclinic/spring-petclinic-angular/commit/f497b8fd61ec9cb896aa09447263b8872fb233be) · [Acquisition output](evidence/environment/E-ENV-02/2026-11-28-01/acquisition.log).

| Input | Pinned facts |
| --- | --- |
| Framework and compiler | Angular core, CLI, builder and compiler CLI `22.1.0`, TypeScript `6.0.3`, RxJS `7.8.2`, Playwright `1.61.1`. The manifest's application version is `11.2.11`. |
| Runtime and package manager | Manifest `engines.node` is `24.x`, `.nvmrc` is `24`, and the tree has no `.node-version`, `.tool-versions`, `.npmrc`, `packageManager` field or root npm engine. The npm lock is `package-lock.json` v3. Locked Angular core, CLI and build require Node `^22.22.3 \|\| ^24.15.0 \|\| >=26.0.0`. CLI/build declare npm `^6.11.0 \|\| ^7.5.6 \|\| >=8.0.0`. TypeScript `6.0.3` is within Angular's `>=6.0.0 <6.1.0` range. [Angular version matrix](https://angular.dev/reference/versions). |
| Selected tools | System Node `24.11.0` with npm `11.6.1` was at `C:\Program Files\nodejs`. No `nvm`, `fnm`, `volta`, `mise` or `nvs` command was found on PATH. The official portable x64 Node `24.22.3` with bundled npm `11.19.1` met all declared Node/npm engines across 653 lock entries. Session-local PATH selected both binaries from the portable directory. [Official Node archive](https://nodejs.org/en/download/archive/v24.22.3). |
| Manifest and lock | Pre-install SHA-256 of `package.json` was `D5B905B530F96253B88EFCEE2E2BACA9AD8646DFA5F9E1DE75592808C7A35CFB`. Pre-install `package-lock.json` SHA-256 was `37D3D1196E6D9F9C3FC754DFDF69F7C52E610ECEB9C6B80FEB17407441FA6C92`. |
| Licence and notices | `package.json` declares MIT. Tracked source including `src/main.ts` and SVG assets carries Apache License 2.0 copyright headers. No standalone LICENSE, NOTICE or COPYING filename was found in the pinned tree. Preserve the existing declarations and headers. Clarify upstream licensing before repackaging or redistributing frontend files. The backend has its own [Apache 2.0 licence](../../LICENSE.txt). [Pinned frontend tree](https://github.com/spring-petclinic/spring-petclinic-angular/tree/f497b8fd61ec9cb896aa09447263b8872fb233be). |
| npm scripts | `ng`, `start` (`ng serve`), `build` (`ng build`), `test`, `test:ci`, `test:coverage`, `lint` and `e2e` (`playwright test`). No root `preinstall`, `install`, `postinstall` or `prepare`. Lock-marked dependency hooks were checked before `ci`. They cover native loading/build for `@parcel/watcher`, `esbuild`, `lmdb` and `msgpackr-extract`, core-js postinstall and macOS-only `fsevents`. [Lifecycle definitions](evidence/environment/E-ENV-02/2026-11-28-01/acquisition.log). |
| Angular config | `@angular/build:application` uses `src/main.ts` and outputs to `dist`. Default build uses source maps and no optimization. Production enables optimization, output hashing, licence extraction and the production environment replacement. `@angular/build:dev-server` serves the same build target by default, with a production target available and no configured development proxy. |
| Upstream browser suite | `playwright.config.ts` selects Chromium, `127.0.0.1:4200` and `e2e/app.e2e-spec.ts`. That file covers welcome, API-backed list views, navigation, validation and responsive layouts using test data/routes. The suite was inspected only. |

### Locked install and compilation

On 2026-11-28, the Windows x64 evaluation used `%TEMP%\sqe-area3-node-1d3d989034c645879d60f7f17a6dadd6\node-v24.22.3-win-x64\node.exe` and `npm.cmd` from the same directory. `npm.cmd ci` added 522 packages and exited `0` in 217.09 seconds. `npm.cmd ls --depth=0` exited `0` and reported the resolved top-level versions. The `ci` output recorded Angular package deprecations, five install hooks without `allowScripts` coverage, an npm upgrade notice, and 36 reported vulnerabilities (20 moderate, 13 high, 3 critical). The vulnerability count is a Phase 6 input. The pinned dependency set was retained. [Install output](evidence/environment/E-ENV-02/2026-11-28-01/npm-ci.stdout.log) · [Install warnings](evidence/environment/E-ENV-02/2026-11-28-01/npm-ci.stderr.log) · [Installed versions](evidence/environment/E-ENV-02/2026-11-28-01/npm-ls.stdout.log).

`npm.cmd run build` executed `ng build` and exited `0` in 15.17 seconds, with Angular reporting 10.608 seconds for bundle generation and a 3.86 MB initial bundle. Its stderr was empty. `dist/browser/index.html` was present under the sibling checkout. Git status remained clean and detached, and both SHA-256 values matched the pre-install files after installation and after compilation. This run verifies compilation. Backend connectivity and persistence remain Phase 1.2 work. [Angular build output](evidence/environment/E-ENV-02/2026-11-28-01/npm-build.stdout.log) · [Verification metadata](evidence/environment/E-ENV-02/2026-11-28-01/verification.log).

### W1/W2 source compatibility

W1 follows `/owners/:id` → `/owners/:id/pets/add`. W2 starts from **Add Visit** on the displayed Pet and follows `/pets/:id/visits/add`. Pet routes also declare `/pets`, `/pets/add` and `/pets/:id/edit`. The Visit router separately declares `/visits`, `/visits/add` and `/visits/:id/edit`. The selected W2 route supplies the Pet ID. Both successful submit callbacks navigate back to Owner detail, which renders the Pet and Visit lists. The two add templates have field-level validation feedback. The detail view and independent readback are the candidate success checks because the submit callbacks redirect immediately. The routes and callbacks were inspected in `owners/owner-detail`, `pets/pet-list`, `pets/pet-add`, `visits/visit-add` and their services under `src/app`.

API paths are relative to `/petclinic/api`. A source match identifies compatible endpoint and field definitions. Request acceptance, UI outcomes and stored records require runtime verification. [Backend OpenAPI](../../src/main/resources/openapi.yml) · [Pinned frontend app](https://github.com/spring-petclinic/spring-petclinic-angular/tree/f497b8fd61ec9cb896aa09447263b8872fb233be/src/app).

| Operation | Source assessment |
| --- | --- |
| Owner detail | **Source match.** `OwnerService.getOwnerById` reads `GET /owners/{ownerId}`. `OwnerRestControllerV1` returns the owner and the view uses `owner.pets`. |
| Pet types | **Source match.** `PetTypeService.getPetTypes` reads `GET /pettypes`. The form binds the selected type object, which supplies the `id` and `name` used by the API model. Owner and vet admin roles can read the list. |
| Add Pet | **Path and required fields match.** `PetService.addPet` posts to `/owners/{ownerId}/pets`. OpenAPI `PetFields` requires `name`, `birthDate` and a type object. The form formats dates as `YYYY-MM-DD`. The controller sets the Owner using the path and returns `201`. **Potential mismatch.** The client also serializes `id: null` and the full `owner`; confirm acceptance of these extras. The UI requires a leading ASCII letter or digit, while OpenAPI uses a nonblank name and the backend applies a separate birth-date rule. |
| Pet detail readback | **Source match.** `PetService.getPetById` reads `GET /pets/{petId}`. W2 uses `ownerId` and `type` from the response. The backend also offers `GET /owners/{ownerId}/pets/{petId}` for association readback. Verify actual returned values and persistence. |
| Add Visit | **Path and required fields match.** `VisitService.addVisit` posts to `/owners/{ownerId}/pets/{petId}/visits`, using the loaded Pet's `ownerId` and `id`. OpenAPI `VisitFields` requires `date` and `description`. The client formats the date as `YYYY-MM-DD`. The controller assigns the path Pet ID and returns `201`. **Potential mismatch.** The client also sends `id: null` and a nested `pet`; confirm acceptance. The controller uses the Pet path ID to associate the write and has no Owner–Pet association check in this method. The UI's description constraints do not mirror the backend's nonblank pattern. |
| Visit readback | **Source match.** `VisitService.getVisitById` maps to `GET /visits/{visitId}`. Backend `Visit` includes `petId` and Pet responses can include `visits`. The UI returns to Owner detail, whose pet rows render their visit lists. Verify the stored Visit and Pet ID independently. |
| Authentication | **Potential mismatch.** The pinned app registers `HttpClientModule` and uses `HttpClient` without an app login, custom HTTP interceptor, explicit `Authorization` header or `withCredentials` setting. The enabled backend uses HTTP Basic and requires authentication, with `OWNER_ADMIN` on these owner/pet/visit writes and reads. Verify browser credential handling and role outcomes in Phase 1.2. |
| API base URL | **Source match.** `environment.ts` and `environment.prod.ts` both use `http://localhost:9966/petclinic/api/`. The backend declares port `9966`, context `/petclinic/` and that API server in OpenAPI. |
| Browser origin and CORS | **Runtime unverified.** `localhost:4200` and `127.0.0.1:4200` are each cross-origin with the API. MVC controllers expose `errors` and `content-type` through `@CrossOrigin`. The security chain requires authentication and has no explicit CORS integration. Check the actual origin and preflight, especially JSON writes and authenticated requests, before choosing an integration mechanism. [Auth runbook](#authentication-and-cors). |

### Checkpoint 0.2 historical observations

The backend checkout contains no Angular package manifest, lockfile, submodule or team browser suite. The proposed frontend checkout is the sibling `D:\SQE-Project\spring-petclinic-angular`, which was absent. The REST project names the Angular project as its client. The Angular README documented the API base and `npm ci`. A compatible revision for this team fork was still to be evaluated. [Upstream REST documentation](../../readme.md) · [Angular README](https://github.com/spring-petclinic/spring-petclinic-angular/blob/master/README.md).

| Evidence | Finding |
| --- | --- |
| Hosted references | Public upstream pages show `master`. Shell `git ls-remote` failed through the local proxy and the public API response was unavailable. The latest live ref and complete tag list remain pending. |
| Immutable candidate | `f497b8fd61ec9cb896aa09447263b8872fb233be` is a real commit shown in package history, with Angular 22.1.0 entries visible in its manifest diff and changes to both package files, plus owner, pet-add and visit-add components. Whole pinned files were unavailable through public reads. Treat it as an evaluation candidate. Compatibility remains unverified. [Candidate commit and diff](https://github.com/spring-petclinic/spring-petclinic-angular/commit/f497b8fd61ec9cb896aa09447263b8872fb233be) |
| Mutable branch snapshots | The checked public manifest showed Angular core 22.2.5, CLI 22.2.6 and Node `24.x`. The repository page lists a lockfile, upstream Playwright and Angular dev server. The published branch files can change independently of this evidence. [Manifest](https://raw.githubusercontent.com/spring-petclinic/spring-petclinic-angular/master/package.json) · [Repository](https://github.com/spring-petclinic/spring-petclinic-angular) |
| Browser routing and API | Branch source imports owner, pet and visit route groups. The development environment points to `http://localhost:9966/petclinic/api/`. OwnerService uses the `owners` resource. PetService posts to `owners/{ownerId}/pets` using the owner ID on its pet object. VisitService posts to `owners/{ownerId}/pets/{petId}/visits`. Recheck exact UI paths, request bodies, response mapping and validation in the same pinned checkout. [Routes](https://raw.githubusercontent.com/spring-petclinic/spring-petclinic-angular/master/src/app/app.routes.ts) · [Environment](https://raw.githubusercontent.com/spring-petclinic/spring-petclinic-angular/master/src/environments/environment.ts) · [Owner service](https://raw.githubusercontent.com/spring-petclinic/spring-petclinic-angular/master/src/app/owners/owner.service.ts) · [Pet service](https://github.com/spring-petclinic/spring-petclinic-angular/blob/master/src/app/pets/pet.service.ts) · [Visit service](https://raw.githubusercontent.com/spring-petclinic/spring-petclinic-angular/master/src/app/visits/visit.service.ts) |
| Authentication evidence | The sampled service methods use Angular HttpClient and show no explicit Basic header on the inspected calls. Global interceptors, a supplied login flow and a working browser credential mechanism remain unverified at an exact revision. |

### Reproduce the Phase 1.1 setup on Windows x64

From the team root, download and activate the verified portable runtime in the current PowerShell session. The ZIP uses a new temporary directory and requires no administrative privileges. Reopen PowerShell to return to its usual PATH or prepend the same extracted directory in another session. Keep the ZIP and extracted runtime outside the team repository. [Official Node archive and checksums](https://nodejs.org/en/download/archive/v24.22.3).

```powershell
$toolRoot = Join-Path ([IO.Path]::GetTempPath()) ('sqe-area3-node-' + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $toolRoot | Out-Null
$zip = Join-Path $toolRoot 'node-v24.22.3-win-x64.zip'
curl.exe --fail --silent --show-error --output $zip 'https://nodejs.org/dist/v24.22.3/node-v24.22.3-win-x64.zip'
if ($LASTEXITCODE -ne 0) { throw 'Node ZIP download failed' }
if ((Get-FileHash -Algorithm SHA256 -LiteralPath $zip).Hash -ne 'E612AF99F626AB415EA225AF6BCE5C971FEB448CB5CAE250BB1C3FA50C5F7D9F') {
    throw 'Node archive SHA-256 mismatch'
}
Expand-Archive -LiteralPath $zip -DestinationPath $toolRoot
$nodeHome = Join-Path $toolRoot 'node-v24.22.3-win-x64'
$env:Path = "$nodeHome;$env:Path"
node.exe --version
npm.cmd --version

powershell.exe -NoProfile -File .\scripts\team\setup-angular.ps1
if ($LASTEXITCODE -ne 0) { throw 'Angular setup failed' }
Push-Location -LiteralPath ..\spring-petclinic-angular
try {
    npm.cmd run build
    if ($LASTEXITCODE -ne 0) { throw 'Angular build failed' }
    Get-FileHash -Algorithm SHA256 package.json,package-lock.json
    git status --porcelain=v1 --branch --untracked-files=all
} finally {
    Pop-Location
}
```

The [setup script](../../scripts/team/setup-angular.ps1) defaults to the team root from its own location and accepts `-ProjectRoot` for another checkout. It checks the sibling directory and origin, pins the commit, verifies the exact portable pair, runs `npm.cmd ci`, and checks manifests and checkout integrity. `-VerifyOnly` checks an acquired checkout without reinstalling. Windows PowerShell validated the script and exercised `-VerifyOnly` successfully on this checkout. Disposable test directories exercised wrong root, nonrepo, wrong origin and dirty-checkout refusals. The acquisition and locked install were executed directly for E-ENV-02. A fresh-machine run of the script remains pending. [Script verification record](evidence/environment/E-ENV-02/2026-11-28-01/setup-script.log).

The download commands ran in an approved network session after the default shell's team lookup failed. If the student's PowerShell can reach GitHub, Node's distribution and the npm registry, it can run the same setup. Preserve the original command output for any failure and resume at the failed step.

## Authentication and CORS

For separately authorized Phase 1.2, run `npm.cmd start -- --host localhost --port 4200` from the sibling checkout with the pinned shell runtime. Use `http://localhost:4200/` for that run and record the actual browser origin.

1. Run the direct backend checks above with security enabled. Verify the three role decisions and unauthenticated status before troubleshooting the frontend.
2. At the pinned SHA, inspect API base/environment, routes, HttpClient registration, interceptors, credential options and W1/W2 request bodies. Open an Owner and inspect the browser’s requests, preserving only sanitized evidence.
3. Observe the browser’s actual cross-origin request behavior. The two dev ports make the default endpoints different origins. Check `OPTIONS` when issued, allowed origin/method/headers and the follow-up response. Spring Security requires authentication and the inspected chain contains no explicit CORS setup. Controller CORS annotations alone leave preflight behavior unverified.
4. Use the observed request sequence to distinguish 401 authentication, owner-role 403 after authenticated direct checks, a browser-blocked preflight, a routing 404, and bad payload/validation responses. Confirm which layer issued each status.
5. Evaluate Playwright browser-context `httpCredentials` scoped to the intended backend origin, one context per role, and verify whether the actual Angular requests carry the intended credentials. Browser behavior and the API request context can differ. If cross-origin setup fails, consider an approved loopback development proxy limited to the Petclinic API path and a relative API base. Verify its exact routing and per-role authentication. Use sanitized evidence and keep credentials away from unrelated requests.
6. Coordinate any application-level adjustment with Member A. Keep the assessment backend security-enabled. Any explicitly approved UI-only security-disabled profile needs separate evidence and must be excluded from security assessment results. [Playwright browser context options](https://playwright.dev/docs/api/class-browser#browser-new-context) · [Backend security chain](../../src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java).

## Reproducible submission and installation order

Record the backend SHA, frontend URL and accepted full SHA, manifest/lock checksum, exact Node/npm versions, and reviewed acquisition/start/reset instructions in the team repository. Propose a team-owned acquisition script to recreate the sibling checkout and apply any reviewed team patches in a documented order. Preserve upstream licence and notices. The mutable Angular manifest declares MIT while inspected source files carry Apache 2.0 headers. Verify the precise pin’s declarations and preserve both kinds of notice as applicable. Clearly identify team changes. Prove setup from a separate clean extraction of the team ZIP including `.git` and rerun both workflows. Agree on an archive strategy with the team if offline reproduction is required. Submodules, nested repositories, vendoring and new external branches require approval. Keep prompts and local secrets outside the archive as described in the [AI-use record](AI_USE_RECORD.md).

| Phase | Proposed order |
| --- | --- |
| 1 | Verify the frontend pin and lock inputs. Obtain a compatible exact Node release if needed. Bootstrap the Maven wrapper from its configured distribution, start and verify the backend, run `npm.cmd ci` in the pinned frontend, then verify secured UI integration. Agree on the team Playwright package, create its locked installation, and run `.\node_modules\.bin\playwright.cmd install chromium` only if the required build is missing. Review download/network access for Maven, npm and browsers. |
| 5 | Add pinned Lighthouse and `@axe-core/playwright` in the approved team package, reuse a compatible browser and verify both tools reach the intended authenticated state. |
| 6 | Obtain the official pinned ZAP installer or package and Dependency-Check release or approved Maven plugin. Confirm Java requirements, passive local capture configuration and vulnerability data access. Document external download, manual install prompts and any optional feed credentials kept locally. |
| 7 | Reuse detected Grafana k6, inspect the future script and perform the local smoke before measured runs. |
| 8 | Agree with Member A on the dedicated Java integration profile and dependencies. Reuse existing test dependencies where they meet the recovery design. |
| Durable demonstration, if approved | Bring up the local Docker engine and obtain the pinned database image only when needed. Confirm loopback-only binding and volume ownership before startup. |

## Blockers and dependencies

| Classification | Dependency and owner |
| --- | --- |
| Blocks Phase 1 completion · team coordination | The exact frontend and portable pair were selected for evaluation. The team approves adoption after secured runtime proof. A and C verify Basic authentication, role decisions, browser CORS and route/payload compatibility. Backend startup and clean H2 seed/reset remain open. |
| Conditional access dependency · Phase 1 | Phase 1.1 acquired the frontend and portable runtime through approved network access. Verify Maven and browser download access only in their authorized checkpoints. |
| Requires instructor confirmation · team coordination | Confirm what persistent storage evidence the assessor expects. Agree with A on the optional PostgreSQL demonstration, image/volume handling and evidence scope. H2 can support initial reproducible testing. |
| Blocks later implementation · team coordination | All approve at least eight functional requirements with at least three per workflow and four source-grounded rules. B leads duplicate and association expectations with A/C. Confirm a defensible state transition and compound decision. Seek instructor clarification if the transition’s eligibility is uncertain. |
| Blocks later implementation · team coordination | C coordinates the shared use-case split of two per member before flow design, and confirms the canonical shared documents. A/B/C provide optimistic, most likely and pessimistic estimates before their respective implementations and begin recording actual effort as work occurs. |
| Blocks later CI and reporting · team coordination | A supplies genuine Surefire, JaCoCo, PIT, survivor and Sonar results. A/B/C coordinate nonempty selection, explicit recovery and all-test-level CI. The team supplies hosted failed-fix progression, blocked PR #6, Issues #8–#10 and protection evidence. |
| Before separate publication authorization | The Phase 1.1 read-only hosted lookup matched the feature and master tracking refs. Repeat the comparison before a separately authorized push and inspect any newly observed integration commits. |

At checkpoint 0.2, the shell query for the team origin failed with `fatal: unable to access 'https://github.com/EimanW/sqe-petclinic-project.git/': Failed to connect to github.com port 443 via 127.0.0.1 after 2095 ms: Could not connect to server`. The upstream Angular query failed with the same proxy connection error after 2050 ms. Public team branch, commit and PR lookups returned cache misses. The approval-session shell retry likewise failed via `127.0.0.1` after 2119 ms. These are Codex session failures. The student's earlier successful 0.1 push demonstrates PowerShell access at that time. The 0.2 approval supplied no current lookup output. Run this read-only lookup in the student's PowerShell from the team worktree before a push. It does not update local references.

```powershell
git ls-remote origin refs/heads/master refs/heads/feat/area3-24i-3014
```

For frontend reference investigation when authorized, use `git ls-remote https://github.com/spring-petclinic/spring-petclinic-angular.git HEAD refs/heads/master 'refs/tags/*'`. Treat its output as reference evidence. Validate manifests, lockfile and workflow behavior before accepting a pin.
