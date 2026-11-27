# Member C handover

## Continuation point

| Item | State on 2026-11-28 (Asia/Karachi) |
| --- | --- |
| Phase | Phase 0, checkpoint 0.1 development commit explicitly authorized. Checkpoint 0.2 and Phase 1 remain unstarted. |
| Branch | `feat/area3-24i-3014`. The worktree entered the commit session with the seven untracked 0.1 documents and a clean tracked worktree and index. The configured Git author matches the assigned contributor. |
| Pre-commit HEAD and local origin/master | Both resolved to `9e318fa35ec0917dec5be8009095bae44278b9c0`, merge commit for PR #5. `origin/master` is the local remote-tracking snapshot. The current GitHub ref and hosting-side protection remain independently unverified. Treat `master` as protected under team instructions. |
| Remote | `https://github.com/EimanW/sqe-petclinic-project.git` for fetch and push. No feature branch tracking ref was listed. |
| Baseline | `baseline` resolves to `220f4ba8e92cec9ad1faa03324723a3e9da63d95`, a verified ancestor of HEAD. Backend licence is Apache 2.0 in `LICENSE.txt`. |
| Verified checkpoint work | Codex checked the local repository and all 39 checklist entries. The independent ChatGPT review of the seven documents passed as reported by the student. No assignment deliverable is marked VERIFIED. The student reported manual review through this checkpoint. The specific checks and changes remain pending in the AI-use record. |
| Git authorization | The seven Markdown files below are authorized for one local development commit. Push and pull request authorization are separate and remain pending. Verify current HEAD and status in Git when resuming. |

## Checkpoint 0.1 files

These are the exact paths authorized for the checkpoint 0.1 commit.

- `docs/member-c/PROJECT_CONTRACT.md`
- `docs/member-c/REQUIREMENTS_CHECKLIST.md`
- `docs/member-c/README.md`
- `docs/member-c/PROGRESS.md`
- `docs/member-c/HANDOVER.md`
- `docs/member-c/COMMIT_LOG.md`
- `docs/member-c/AI_USE_RECORD.md`

The three exact Codex prompts are listed in the [AI-use record](AI_USE_RECORD.md). The ignored copies were checked against the original attachments. Keep them out of commits and the submission ZIP. Confirm private backup and the instructor access arrangement for required AI-use evidence.

## Commands and outcomes

### Initial 0.1 inspection

| Check | Outcome |
| --- | --- |
| `git status --short --branch`, `git status --porcelain=v1 -uall`, `git remote -v`, `git branch -a`, `git log --oneline -20`, `git tag -n` and Git author name/email lookup | Safe branch and clean initial tracked/unignored worktree. The expected team remote, baseline and recent history were present. Raw Git author email was kept out of these files. |
| `git rev-parse HEAD origin/master`, `git rev-parse 'baseline^{commit}'`, `git merge-base --is-ancestor baseline HEAD`, `git diff --name-status baseline HEAD` | Hashes above. Baseline ancestry check exited 0. Baseline-to-team diff includes Java tests, config and the exception handler as well as build, CI and documentation changes. |
| `C:/Program Files/Git/mingw64/bin/pdftotext.exe -f 1 -l 4 -layout -enc UTF-8 'SQE_Final_Project Fall 2026.pdf' -` and equivalent `-f 5 -l 7` | Extracted all seven pages. A Python check confirmed seven text segments and the matching footer on every page. Used the locally ignored root PDF after the proposed parent path was absent. Hash in the [checklist](REQUIREMENTS_CHECKLIST.md). An alternate Python PDF dependency was unavailable. |
| `rg --files`, `git ls-files`, and source reads of POM, wrapper properties, workflow, test sources, REST controllers, advice, validation, H2 scripts and OpenAPI | Confirmed the source observations below. Existing documentation is primarily `docs/static-analysis/PetAgeValidator_CFG.drawio` and `docs/mutation/custom-mutants.md`. Shared plan, weekly reports, completion report, six use cases, traceability, risk register and generated API test data were not located in tracked files at this revision. Ask owners for their canonical evidence before adding shared files. |
| `git --version`, `java -version`, `node --version`, `npm.cmd --version`, `python --version`, `docker --version`, `k6 version`, `Get-Command` and `npm.cmd list --global --depth=0` | Windows 10.0.26200.0 kernel, Git 2.51.2.windows.1, Microsoft OpenJDK 21.0.12.1, Node 24.11.0, npm 11.6.1, Python 3.14.0, Docker CLI 29.9.2 and k6 2.2.0. Standalone Maven, Playwright, Lighthouse and ZAP were not found in the checked locations. Docker engine and Chromium availability are pending. Wrapper 3.3.4 is configured with Maven 3.10.0. Wrapper execution was deferred because it can bootstrap a download. |
| `git ls-remote https://github.com/spring-petclinic/spring-petclinic-angular.git refs/heads/master` with interactive auth disabled, public upstream file reads, public team PR/Issue page attempts | Shell Git could not contact GitHub through the local proxy. The upstream README and selected source files were accessible, but an exact matching SHA and the team PR/Issue evidence could not be established from the available pages. |
| `git status --porcelain=v1 --branch -uall`, `git diff --check`, `git diff --cached --check`, and a read-only check of all seven new Markdown files | Expected branch and doc-only untracked paths. No tracked or staged diffs. New files reviewed separately for content, links, checklist columns and whitespace, since regular Git diff omits them. The initialization prompt hash was checked against the attachment and `git check-ignore -v` confirmed its exclusion. |

### 0.1 correction review

| Check | Outcome |
| --- | --- |
| `git branch --show-current`, `git rev-parse HEAD refs/remotes/origin/master`, `git rev-parse 'baseline^{commit}'`, `git merge-base --is-ancestor baseline HEAD` | Authorized feature branch, the same local hashes as above, and ancestry exit 0. |
| Poppler `pdftotext -layout -enc UTF-8`, `Get-FileHash -Algorithm SHA256`, and checklist schema and link checks | All seven pages and the original PDF hash were checked. 39 unique requirements with 28 NOT STARTED, 9 IN PROGRESS, 2 NEEDS HUMAN REVIEW and 0 VERIFIED. |
| `rg -c '^\s*@Test\s*$'` on the team Java tree and read-only checks of POM, app/test properties, H2/HSQLDB seeds and CI YAML | Seven classes contain 72 `@Test` annotations. Profile, seed and gate settings were checked in source. Test runs and measured results remain unverified. |
| `git status --porcelain=v1 -uall`, `git diff --check`, `git diff --cached --check`, direct reads of all seven files, `git check-ignore -v` and prompt hash comparisons | The seven Markdown files remain untracked. No tracked or staged changes. The PDF and both private prompt copies match their recorded sources and remain excluded from Git. |

## Source observations recorded at checkpoint 0.1

- **Team evidence.** The local history includes PR #5, its regression test and an `AccessDeniedException` handler returning 403. Seven original team Java classes contain 72 `@Test` methods counted from source. Actual execution and pass counts remain unverified, including the live HTTP result of the fix. A CFG, custom mutation script/notes and PR CI for `master` are tracked. The POM configures 100% JaCoCo line and branch coverage for the selected scope and a PIT threshold of 90%. The workflow contains a SonarQube Cloud gate. Measured coverage, Sonar results, PIT score and surviving-mutant analysis remain pending. Hosted evidence for blocked PR #6, Issues #8–#10 and branch protection also remains pending.
- **CI coordination with Member A.** The workflow selects Member A's Java team package and has no browser or dedicated API job. The command includes `-Dsurefire.failIfNoSpecifiedTests=false`, which can suppress Surefire's failure for an explicit selector that matches no tests. Ask Member A to confirm the selector and executed count from the genuine Surefire report and agree how the gate will verify nonempty execution and include all required test levels. The command and quality gate remain as configured.
- **Backend.** `pom.xml` declares application 4.0.2 with Spring Boot parent 4.2.0, and CI selects Java 21. The default `h2,spring-data-jpa` profile seeds the in-memory H2 database from `src/main/resources/db/h2/{schema,data}.sql`. The configured base is `http://localhost:9966/petclinic/`. H2 writes use relational persistence within the running process. A fresh process is the proposed reset, pending demonstration and confirmation of whether assignment assessors require durable storage across restarts.
- **Workflow source.** `src/main/resources/openapi.yml` and `OwnerRestControllerV1` map the owner detail and nested pet read/create/update and visit create endpoints. Pet creation associates an existing owner. Visit creation stores the Pet association. `PetAgeValidator` accepts today's date and exactly 50 years ago and rejects future and older dates. OpenAPI declares a required nonblank pet name limited to 30 and nonblank visit description limited to 255, with a required visit date. The H2 visit schema requires a pet foreign key. The Visit model has no scheduled/completed/cancelled lifecycle. Agree on expected duplicate prevention and a source-grounded transition rule in the test basis, then verify actual behavior. Record deviations from approved expectations as potential defects with evidence.
- **Authorization source.** Default security is enabled, using HTTP Basic, JDBC roles and method authorization. The H2 admin account holds OWNER_ADMIN, VET_ADMIN and ADMIN. The H2 vet account holds VET_ADMIN only. The existing Java test configuration selects HSQLDB, whose inspected seed lacks that vet account. A role comparison and the auth/CORS behavior still require isolated runtime verification. The owner-scoped read checks the Pet under the Owner. The nested write code warrants defensive owner-association review with Member B. Keep the approved access expectation, source observation and evidence of any actual deviation as distinct records.
- **Angular.** The tracked backend links to an external Angular frontend. No sibling checkout, tracked frontend lockfile, gitlink, or team Playwright directory was found. Public upstream sources show Angular core 22.2.5, CLI 22.2.6, `.nvmrc` Node 24, npm lockfile, owner/pet/visit route modules, an API base matching the backend, and a pre-existing upstream Chromium suite under `e2e/`. Upstream Angular 22 requires Node 24.15.0 or newer within 24.x. Local 24.11.0 is below that floor. Branch URLs showed slightly different manifest snapshots and cannot establish a pin. Detailed routes, authentication support and runtime behavior remain to be checked against one exact revision. [Upstream repository](https://github.com/spring-petclinic/spring-petclinic-angular) · [Environment source](https://raw.githubusercontent.com/spring-petclinic/spring-petclinic-angular/master/src/environments/environment.ts) · [Angular compatibility table](https://angular.dev/reference/versions)

## Blockers, decisions and next action

1. **Authorized development commit.** After local checks, stage the seven reviewed paths and commit them as `docs(area3): record project contract and assignment checklist`. Verify the resulting commit in Git. At the next authorized documentation checkpoint, record its actual SHA. Record the remaining personal verification details in `AI_USE_RECORD.md` before final submission.
2. **Resolve the 0.1 Git checkpoint.** After the local commit, wait for separate explicit authorization to push to `feat/area3-24i-3014`. Verify the remote feature branch after an authorized push. Opening a pull request requires its own authorization.
3. **Checkpoint 0.2.** Once the 0.1 commit/push checkpoint is resolved as instructed, start a fresh Codex chat unless the student decides otherwise. Antigravity may continue in the same workspace. Use a separate authorized 0.2 prompt and reconstruct state following [README.md](README.md). The planned work is the repository audit and reproducible environment documentation.
4. **Team inputs.** Verify the registration row and announcement date. Ask A for quality artifacts, highest-risk release targets, blocked-PR evidence and branch-policy evidence. Ask B for the approved functional test basis, measurable API coverage criteria, traceability and issue links before adding anything shared. Establish the four-rule set, including the actual state transition and duplicate expectations. Obtain prospective estimates and actual effort from each owner.
5. **Frontend decision.** Once acquisition and Phase 1 are both authorized, use a sibling checkout under `D:/SQE-Project/spring-petclinic-angular`. Obtain the exact upstream SHA from a network-enabled context, verify the commit-specific `.nvmrc`, manifest and lockfile, select an exact compatible Node 24 patch, and test both workflows with security enabled. Record the final SHA and versions, use `npm ci`, and keep any team frontend change reproducible through reviewed patches and setup instructions in the team repository. The prior local Git network failure and available mutable upstream snapshots leave the exact pin pending.
6. **Protected work.** Preserve production routes and rules, database scripts, teammate Java tests, upstream suites, POM/wrapper, CI and quality gates, and GitHub configuration. A shared-file change requires approval of its exact diff. Keep screenshots, installations, application runs, dependency audit, security assessment and test implementation for their authorized phases.
