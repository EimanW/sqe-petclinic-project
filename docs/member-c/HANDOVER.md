# Member C handover

## Continuation point

| Item | Verified or reported state on 2026-11-28 (Asia/Karachi) |
| --- | --- |
| Current boundary | Phase 1.2 met local technical acceptance. The student reported personal review and authorized the four local development commits. The locally committed checkpoint awaits a separate publication decision. Phase 1.3 is unstarted. [Actual results](PHASE_1_2_RESULTS.md) · [Commit log](COMMIT_LOG.md). |
| Workspace and identity | `D:\SQE-Project\sqe-petclinic-project` on `feat/area3-24i-3014`, tracking `origin/feat/area3-24i-3014`. Origin is `https://github.com/EimanW/sqe-petclinic-project.git`. Configured author is Muhammad Huzaima Malik (`i243014@isb.nu.edu.pk`). |
| Published feature history | Phase 0.1–0.3 and Phase 1.1's `e5fd284aa83bf4751cb739703bb807da47216cbe` and `5306226dbe002a842e013c55a7b2eac776f3332c` were published. The student supplied successful push/fetch, matching HEAD and tracking refs, and a clean prior worktree. The local feature tracking ref still points to `5306226dbe002a842e013c55a7b2eac776f3332c`. [Commit log](COMMIT_LOG.md). |
| Student synchronization | Merge `668a371cf4a180b2b6f9c868e14935df477177b2` has first parent `5306226dbe002a842e013c55a7b2eac776f3332c` and second parent `73ccbf1257f927c6b35b22c02c5eb8cd28678883`. It was HEAD at commit-review entry, three commits ahead by expected merge ancestry. The merge preserved all five published Member C commits and tagged baseline `220f4ba8e92cec9ad1faa03324723a3e9da63d95`. |
| Local and hosted master | Local `master` remains `9e318fa35ec0917dec5be8009095bae44278b9c0`. The local `origin/master` snapshot remains `73ccbf1257f927c6b35b22c02c5eb8cd28678883`. Read-only GitHub verification found hosted master at `5c74a9b60f26290e2496ec7b891b1d6d111edbd0`, two commits ahead through `63f0e27` and PR #12. The hosted diff adds `e2e/tests/uc1-uc2.spec.ts` and removes `e2e/tests/w1-w2-pet-visit.spec.ts`. The historical pairing and local integration history are preserved. |
| Angular checkout | `D:\SQE-Project\spring-petclinic-angular`, upstream `https://github.com/spring-petclinic/spring-petclinic-angular.git`, clean detached HEAD at `f497b8fd61ec9cb896aa09447263b8872fb233be`. Angular core and CLI 22.1.0, TypeScript 6.0.3, portable Node 24.22.3/npm 11.19.1. The workspace helper created a disposable proxy overlay and preserved the sibling checkout and both lockfiles. [Runbook](ENVIRONMENT.md). |
| Historical suite | Eiman's `e2e/` is the provisional browser entry point. The reviewed revision ran five tests from a fresh H2 seed with Playwright 1.65.1 and Chromium 1255. The vet smoke passed, W1 failed on a blank Owner field after HTTP 200, and three serial cases did not run. The Phase 1.2 run did not include the later hosted spec. [Inventory and results](PHASE_1_2_RESULTS.md#team-browser-suite). |
| Verified commit scope | Four local commits cover the approved 37 paths below. The original synchronization and teammate source are preserved. The checkpoint listener processes were stopped during runtime execution. The commit review started none. [Evidence register](EVIDENCE_REGISTER.md#phase-12-evidence). |
| Private inputs and originals | The PDF and prompts are Git-ignored. The full Playwright report, traces, screens, video and original logs are under `target/member-c-phase12/`. Restricted hashes were verified. Private backup confirmation remains pending before any Maven clean. [Report locations](PHASE_1_2_RESULTS.md#restricted-originals) · [AI-use record](AI_USE_RECORD.md). |

## Executed checks

- Maven Wrapper 3.3.4 bootstrapped Maven 3.10.0 with Microsoft OpenJDK 21.0.12.1. The backend started on loopback TCP 9966 with secured H2 and the expected 401/200/200/403 direct role statuses. Invalid credentials also returned 401. The H2 seed and live OpenAPI were verified.
- A synthetic Pet marker returned 201, was read via two independent GETs, and returned 404 after the verified backend JVM was replaced and the seed restored.
- Chromium observed direct-origin GET and preflight 401 responses with no allowed-origin header. The existing proxy routed secured same-origin JSON GETs and both POSTs. The independent probe created Pet 15 and Visit 5 and verified the Owner 1 → Pet 15 → Visit 5 chain via direct readback. Owner Search and the real Previous Visits form displayed the records. The Owner detail rendering limitation required opening the Visit form by its Pet-specific URL.
- The npm installation used the integrated E2E lockfile. Playwright installed its matching Chromium. The Angular dependencies were reused. Sandbox attempts and the failed dependency junction are retained with the runtime originals. [Run details](PHASE_1_2_RESULTS.md).

## Next authorized action

The next decision is whether to publish the four local Phase 1.2 commits and the existing student merge to `origin/feat/area3-24i-3014`. Publication and any further synchronization, pull request or Phase 1.3 work need separate authorization. Recheck hosted refs before publishing. The original Phase 1.2 PERT estimate of 8.67 person-hours remains proposed. The student's specific personal checks and attended hours remain unspecified.

The student reports receiving Eiman's combined Area 1 report with UC1/UC2, 12 flows, ten product and four project risks, exit criteria and reported browser, static, mutation and CI results. Eiman's exact Angular revision and the context for the reported defect #9 expected failure need confirmation. Her report's EC1/EC2 and severity proposals differ from earlier input. Eiman and Hamza need to settle those discrepancies and confirm canonical `e2e/` ownership and case assignments. Hamza's finalized requirements, rules and approved test oracles remain pending. The instructor's durability expectation and approval for a separate PostgreSQL demonstration also remain pending. [Manual actions](MANUAL_ACTIONS.md#copy-ready-team-message).

## Commit strategy for future checkpoints

Plan the commit breakdown before implementation. Prefer small, coherent commits for tooling and environment setup, shared infrastructure and reusable fixtures, functional implementation, automated tests, verified defect fixes, genuine execution evidence and analysis, and documentation and reporting. Keep trivial changes together where they serve one purpose. Make each commit understandable on its own and preserve a usable repository state where practical.

Stage explicit paths and verify every staged diff. Avoid `git add .` and `git add -A`. Preserve published history and obtain separate authorization for any rewrite, squash or backdating. Preserve genuine contribution history. Commit and push require separate authorization.

## Proposed staging list

These lists preserve the exact scopes from the approval. The first three actual commit SHAs are in the [commit log](COMMIT_LOG.md). The fourth is the commit containing this handover.

### 1. feat(area3): add secured local runtime setup helper

Makes the direct and proxied pinned Angular startup reproducible. Verified both loopback starts, successful overlay lock check and reuse of installed dependencies. Requires the published Phase 1.1 setup script and the merged team proxy.

```text
scripts/team/start-angular-runtime.ps1
```

### 2. test(area3): add secured runtime and H2 reset probes

Supplies the reusable API/H2 checker and the independent browser diagnostic. Verified direct authentication and reset, both real UI writes, independent readbacks and the separate vet context. Browser reproduction uses commit 1 and the integrated `e2e/` lock. The H2 verifier runs against the existing backend.

```text
scripts/team/probe-secured-browser.cjs
scripts/team/verify-secured-h2.ps1
```

### 3. docs(area3): record secured workflow integration evidence

Records the actual execution, compatibility failure, restricted artifact inventory and durability plan. Verified original status/readback records, report counts and digests, and absence of credential-bearing report artifacts from the proposed paths. Depends on the runtime and probes from commits 1–2.

```text
docs/member-c/PHASE_1_2_RESULTS.md
docs/member-c/evidence/environment/E-ENV-03/2026-11-28-01/api-seed.log
docs/member-c/evidence/environment/E-ENV-03/2026-11-28-01/java.stderr.log
docs/member-c/evidence/environment/E-ENV-03/2026-11-28-01/startup-observed.log
docs/member-c/evidence/environment/E-ENV-03/2026-11-28-01/wrapper.log
docs/member-c/evidence/environment/E-ENV-04/2026-11-28-01/marker-after.log
docs/member-c/evidence/environment/E-ENV-04/2026-11-28-01/marker-before.log
docs/member-c/evidence/environment/E-ENV-04/2026-11-28-01/restart-listener.log
docs/member-c/evidence/environment/E-ENV-05/2026-11-28-01/angular-direct-approved.log
docs/member-c/evidence/environment/E-ENV-05/2026-11-28-01/angular-proxy-copy.log
docs/member-c/evidence/environment/E-ENV-05/2026-11-28-01/browser-direct-approved.log
docs/member-c/evidence/environment/E-ENV-05/2026-11-28-01/browser-workflows.log
docs/member-c/evidence/environment/E-ENV-05/2026-11-28-01/e2e-toolchain.log
docs/member-c/evidence/environment/E-ENV-05/2026-11-28-01/npm-ci-approved.log
docs/member-c/evidence/environment/E-ENV-05/2026-11-28-01/playwright-list.log
docs/member-c/evidence/environment/E-ENV-05/2026-11-28-01/playwright-toolchain.log
docs/member-c/evidence/environment/E-ENV-05/2026-11-28-01/proxy-routing.log
docs/member-c/evidence/environment/E-ENV-05/2026-11-28-01/runtime-originals.log
docs/member-c/evidence/environment/E-ENV-05/2026-11-28-01/team-suite-review.log
```

### 4. docs(area3): update runtime handover and team integration plan

Updates the publication and merge record, reproducible runbook, evidence register, requirements, team dependencies, estimates and AI disclosure. Verified checklist and evidence ID preservation, local references and exact unstaged scope. Depends on commits 1–3.

```text
docs/member-c/AI_USE_RECORD.md
docs/member-c/COMMIT_LOG.md
docs/member-c/DECISIONS.md
docs/member-c/EFFORT_LOG.md
docs/member-c/ENVIRONMENT.md
docs/member-c/EVIDENCE_REGISTER.md
docs/member-c/HANDOVER.md
docs/member-c/IMPLEMENTATION_PLAN.md
docs/member-c/MANUAL_ACTIONS.md
docs/member-c/PROGRESS.md
docs/member-c/PROJECT_CONTRACT.md
docs/member-c/README.md
docs/member-c/REPOSITORY_AUDIT.md
docs/member-c/REQUIREMENTS_CHECKLIST.md
docs/member-c/VIVA_PREPARATION.md
```
