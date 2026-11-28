# Member C handover

## Continuation point

| Item | Verified or reported state on 2026-11-28 (Asia/Karachi) |
| --- | --- |
| Current boundary | Phase 1.1 met local technical acceptance. The student reported review and authorized the two local commits. Publication requires separate approval. Phase 1.2 remains unstarted. |
| Team branch and origin | `D:\SQE-Project\sqe-petclinic-project` on `feat/area3-24i-3014`, tracking `origin/feat/area3-24i-3014`. Origin is `https://github.com/EimanW/sqe-petclinic-project.git`. The local feature series starts at setup commit `e5fd284aa83bf4751cb739703bb807da47216cbe` and ends at the documentation/evidence commit containing this handover. The tracking ref and live pre-commit feature tip match the Phase 0.3 parent `e182f1333f3d1e9cd55d677839816905a17f4db2`. |
| Phase 0 | The local history confirms checkpoints 0.1 `8cefbb958b491b29a46ba9ecddaa3b021b0a3c75`, 0.2 `48450e0619d6915664e7a5e405f4de1ff7eea272`, and 0.3 `e182f1333f3d1e9cd55d677839816905a17f4db2`. The student supplied successful manual push/fetch evidence for 0.3. Parent, subject and the 16 committed paths were checked. [Commit log](COMMIT_LOG.md). |
| Integration and baseline | `baseline` at `220f4ba8e92cec9ad1faa03324723a3e9da63d95` is an ancestor of HEAD. `origin/master` and the live master ref both resolved to `9e318fa35ec0917dec5be8009095bae44278b9c0`, with the feature 0 behind and 3 ahead before the two local commits. The before-commit hosted and tracking refs matched. Hosting branch protection and CI enforcement remain separate evidence. |
| Entry and review scope | Phase 1.1 started from a clean checkout. Commit preparation found exactly the 20 authorized paths and an empty index. The user authorized a script-only commit followed by the 19 documentation and evidence files. The Angular repository is a sibling, outside the team worktree. |
| Angular checkout | `D:\SQE-Project\spring-petclinic-angular`, upstream `https://github.com/spring-petclinic/spring-petclinic-angular.git`, clean detached HEAD at `f497b8fd61ec9cb896aa09447263b8872fb233be`. Angular core and CLI 22.1.0, TypeScript 6.0.3. [Runbook](ENVIRONMENT.md#phase-11-pinned-checkout). |
| Toolchain | The active pair for install/build was portable Windows x64 Node 24.22.3 and bundled npm 11.19.1 at `%TEMP%\sqe-area3-node-1d3d989034c645879d60f7f17a6dadd6\node-v24.22.3-win-x64`. Activate it on PATH in each new shell or recreate it with the verified ZIP instructions. The normal system pair remains Node 24.11.0/npm 11.6.1. |
| Private inputs | The assignment PDF and private prompt copies are ignored and untracked. The Phase 1.1 requirements and approval attachment were copied byte-for-byte into the ignored prompt directory. The inline amendment is saved alongside them. See the [prompt index](AI_USE_RECORD.md#prompt-index-and-recovery). |

## Executed commands and results

| Steps | Result |
| --- | --- |
| `git status --short --branch`, `git rev-parse`, `git show`, `git merge-base --is-ancestor`, `git rev-list --left-right --count origin/master...HEAD` | Local checkpoint and baseline checks passed. The initial restricted `git ls-remote origin refs/heads/master refs/heads/feat/area3-24i-3014` exited `128`. The same lookup through the approved network session exited `0` with the two matching refs. |
| `git clone --no-checkout`, `git cat-file -t` and `git checkout --detach` with the pinned SHA | Clone and checkout exited `0`. `git show -s`, `git remote get-url origin` and `git status --porcelain=v1 --branch --untracked-files=all` verified metadata, origin and clean detached checkout. [Acquisition](evidence/environment/E-ENV-02/2026-11-28-01/acquisition.log). |
| `curl.exe` for the Node ZIP and official `SHASUMS256.txt`, `Get-FileHash -Algorithm SHA256`, `Expand-Archive`, `node.exe --version`, `npm.cmd --version` | Official ZIP hash matched. Portable node and npm commands resolved from the same directory. npm's bundled semver check over 653 lock entries found zero Node/npm engine mismatches. [Acquisition](evidence/environment/E-ENV-02/2026-11-28-01/acquisition.log) · [Verification](evidence/environment/E-ENV-02/2026-11-28-01/verification.log). |
| `npm.cmd ci`, `npm.cmd ls --depth=0`, `npm.cmd run build` | Each exited `0`. Install added 522 packages, Angular built `dist/browser/index.html`, and the Git worktree and both input hashes were unchanged. npm reported 36 vulnerabilities. See [E-ENV-02](EVIDENCE_REGISTER.md#existing-documentation-evidence) for the original output and warnings. |
| PowerShell `Parser.ParseFile` and `powershell.exe -NoProfile -File scripts/team/setup-angular.ps1 -VerifyOnly` | Final parser and real checkout checks passed. Disposable directories confirmed four refusals with exit `1` and preserved fixture data. The system Node 24.11.0 also triggered the expected toolchain refusal. Initial validation exposed two Windows PowerShell issues in root defaulting and command resolution, which were corrected and rechecked. Full script acquisition/install from a fresh machine remains pending. [Script verification](evidence/environment/E-ENV-02/2026-11-28-01/setup-script.log). |

Commit preparation repeated the Git scope and ancestry checks, PowerShell parser and `-VerifyOnly`, Node archive hash and package hashes. The seven logs support the prior install and build, and the four raw npm streams match their originals byte-for-byte. `verification.log` matches the ignored private original after the recorded `%TEMP%` substitution and explanatory line. The raw build and dependency-list stdout retain the original table padding and terminal blank lines. Commit verification checks those bytes separately from prose and script whitespace. The reported independent ChatGPT review covered 15 Markdown files and the script. The student separately reported personal review of those materials, with individual checks and changes unspecified. [AI-use record](AI_USE_RECORD.md) · [Evidence register](EVIDENCE_REGISTER.md).

## Next authorized action

Phase 1.1 stops at the local commit series. If publication is separately authorized, recheck the live refs and publish the authorized feature branch. Record the actual documentation commit SHA and any later push during the next authorized checkpoint update. Read the [commit log](COMMIT_LOG.md) for earlier checkpoint evidence.

After Phase 1.1 publication and separate Phase 1.2 authorization, use the exact paired revisions and verify the secured browser. Start with Basic authentication and role outcomes, CORS preflight and payload acceptance. Then verify W1/W2 persistence, repeatable H2 reset and the storage plan. Team adoption of the pin and instructor guidance on durable-storage proof remain pending. The estimates remain proposed and the student's actual attended effort is unknown. [Compatibility matrix](ENVIRONMENT.md#w1w2-source-compatibility) · [Phase 1.2](IMPLEMENTATION_PLAN.md#12--verify-secured-w1w2-integration) · [Manual actions](MANUAL_ACTIONS.md).

## Commit strategy for future checkpoints

Plan the commit breakdown before implementation. Prefer small, coherent commits for tooling and environment setup, shared infrastructure and reusable fixtures, functional implementation, automated tests, verified defect fixes, genuine execution evidence and analysis, and documentation and reporting. Keep trivial changes together where they serve one purpose. Make each commit understandable on its own and preserve a usable repository state where practical.

Stage explicit paths and verify every staged diff. Avoid `git add .` and `git add -A`. Preserve published history and obtain separate authorization for any rewrite, squash or backdating. Preserve genuine contribution history. Commit and push require separate authorization.

## Proposed staging list

The approved Phase 1.1 scope is recorded below. Commit 1 contains `scripts/team/setup-angular.ps1` alone. Commit 2 contains the other 19 paths. Private prompts, original local-path metadata, the PDF, portable runtime, npm cache, installed packages and generated Angular `dist` remain outside the series.

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
docs/member-c/README.md
docs/member-c/REQUIREMENTS_CHECKLIST.md
docs/member-c/evidence/environment/E-ENV-02/2026-11-28-01/acquisition.log
docs/member-c/evidence/environment/E-ENV-02/2026-11-28-01/npm-build.stdout.log
docs/member-c/evidence/environment/E-ENV-02/2026-11-28-01/npm-ci.stderr.log
docs/member-c/evidence/environment/E-ENV-02/2026-11-28-01/npm-ci.stdout.log
docs/member-c/evidence/environment/E-ENV-02/2026-11-28-01/npm-ls.stdout.log
docs/member-c/evidence/environment/E-ENV-02/2026-11-28-01/setup-script.log
docs/member-c/evidence/environment/E-ENV-02/2026-11-28-01/verification.log
scripts/team/setup-angular.ps1
```
