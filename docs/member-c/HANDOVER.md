# Member C handover

## Continuation point

| Item | State on 2026-11-28 (Asia/Karachi) |
| --- | --- |
| Current authorization | The student reported the passing independent ChatGPT review and authorized staging the eight documents and creating one local checkpoint 0.2 commit after verification. Push, checkpoint 0.3 and Phase 1 each require separate authorization. |
| Repository and branch | `D:\SQE-Project\sqe-petclinic-project` on `feat/area3-24i-3014`, tracking `origin/feat/area3-24i-3014`. Origin is `https://github.com/EimanW/sqe-petclinic-project.git`. Local author matches Muhammad Huzaima Malik. |
| Pre-commit HEAD and 0.1 | `8cefbb958b491b29a46ba9ecddaa3b021b0a3c75` is checkpoint 0.1 and the local feature tracking tip. Git confirms its author, message and exactly seven Member C documents. The student supplied the successful push outcome. See [commit log](COMMIT_LOG.md) and [AI-use record](AI_USE_RECORD.md). |
| Branch base and baseline | 0.1’s parent and local `origin/master` are `9e318fa35ec0917dec5be8009095bae44278b9c0`, PR #5’s merge. `baseline` is `220f4ba8e92cec9ad1faa03324723a3e9da63d95` and an ancestor of HEAD. Local `origin/master...HEAD` is 0 behind, 1 ahead. |
| Hosted status | Current hosted tips and any newer integration commits are pending. Codex's restricted network could not reach GitHub through its local proxy. The student demonstrated PowerShell access with the earlier push. No current manual lookup output accompanied the approval. Treat master as protected under the team contract. See [runbook](ENVIRONMENT.md#blockers-and-dependencies). |

At the start of the audit the worktree was clean. At the start of approval the index was empty and exactly eight intended Markdown files were pending. `PROJECT_CONTRACT.md` stays at the reviewed 0.1 revision.

| Change | Authorized staging path |
| --- | --- |
| New | `docs/member-c/REPOSITORY_AUDIT.md` |
| New | `docs/member-c/ENVIRONMENT.md` |
| Modified | `docs/member-c/README.md` |
| Modified | `docs/member-c/REQUIREMENTS_CHECKLIST.md` |
| Modified | `docs/member-c/COMMIT_LOG.md` |
| Modified | `docs/member-c/PROGRESS.md` |
| Modified | `docs/member-c/HANDOVER.md` |
| Modified | `docs/member-c/AI_USE_RECORD.md` |

The ignored prompt copies are indexed in the [AI-use record](AI_USE_RECORD.md). Keep them with the private originals. The official assignment PDF is locally ignored.

## Commands actually executed

| Check | Outcome |
| --- | --- |
| `git --no-optional-locks status --short --branch`, `git branch -avv`, `git remote -v`, `git var GIT_AUTHOR_IDENT`, `git show --format=fuller --stat HEAD`, `git log --oneline --decorate --all` and `git log --format='%h %s' baseline..HEAD` | Verified clean entry state, authorized branch and configured identity, checkpoint 0.1 metadata, and teammate history. |
| `git rev-parse HEAD origin/feat/area3-24i-3014 origin/master`, `git rev-parse --abbrev-ref '@{upstream}'`, `git rev-parse 'baseline^{commit}'`, `git merge-base --is-ancestor baseline HEAD` and `git rev-list --left-right --count origin/master...HEAD` | Hashes above, correct upstream, valid baseline ancestry and exactly one feature commit beyond the tracking snapshot of master. |
| `git ls-remote origin refs/heads/master refs/heads/feat/area3-24i-3014` and `git ls-remote https://github.com/spring-petclinic/spring-petclinic-angular.git HEAD refs/heads/master 'refs/tags/*'` | Both failed to connect to GitHub port 443 via `127.0.0.1`. Exact failure and retry details are in the [runbook](ENVIRONMENT.md#blockers-and-dependencies). |
| `Get-Content -Encoding utf8`, `rg --files` and targeted `rg`/source reads, including `rg -c '^\s*@Test\s*$'` | Read the seven 0.1 docs and latest retained prompt. Confirmed route/service paths, roles and seed differences, configured CI settings and 72 team Java `@Test` methods. |
| Existing Git-bundled `pdftotext.exe -f 1 -l 5 -layout -enc UTF-8` and `-f 6 -l 7` on the local PDF, plus `Get-FileHash -Algorithm SHA256` | Cross-checked the relevant assignment sections and rubric. PDF hash matches the [checklist](REQUIREMENTS_CHECKLIST.md). |
| `Get-Command`, `git --version`, `java -version`, Java home `Test-Path`, `node --version`, `npm.cmd --version`, `python --version`, `docker --version`, `docker compose version`, `docker info --format '{{.ServerVersion}}'`, `k6 version`, `k6 run --help` and `k6 inspect --help` | Read tool versions and k6 CLI capabilities. Compose was available and its engine was unreachable. Read Chromium cache names and checked Chrome paths. No browser launch or workload execution. |
| Public GitHub/Angular documentation reads | Identified evaluation SHA `f497b8fd61ec9cb896aa09447263b8872fb233be` and the Angular 22 Node 24 floor. Public mutable files informed route and auth inspection. Exact pinned files and the secured runtime pairing remain pending. |
| `Test-Path -LiteralPath '..\spring-petclinic-angular'`, `git ls-files --stage` and tracked file inventory | No sibling Angular checkout, tracked npm frontend files or Git submodule. |
| `Copy-Item -LiteralPath`, `Get-FileHash -Algorithm SHA256` and `git check-ignore -v` | Preserved and hash-verified the exact ignored 0.2 attachment. Confirmed ignore rules for private prompts and the PDF. |
| `git status --porcelain=v1 --branch -uall`, `git diff --check`, `git diff --cached --check`, `git diff HEAD --name-only` and Python document validation | Verified the eight documentation paths including the two untracked files, empty index, Markdown links, unchanged 39 checklist IDs and statuses, and syntax of the proposed PowerShell blocks with the PowerShell parser. The parser did not execute the blocks. |

During approval, Git reconfirmed the branch, configured author, baseline ancestry, 0.1 parent and eight-file scope. Codex retried `git ls-remote origin refs/heads/master refs/heads/feat/area3-24i-3014`. The restricted session failed to connect to GitHub port 443 via `127.0.0.1` after 2119 ms, and public API and branch queries returned no reference evidence. The exact approval attachment was copied privately and hash-verified.

No repository was cloned. No tools, applications or databases were installed or started. The Maven wrapper, test suite and security assessments remain unexecuted.

## Carry forward

- The [repository audit](REPOSITORY_AUDIT.md) records W1/W2 source behavior, source test counts, CI scope and rule gaps. Member A’s package and quality gates remain protected. Request genuine runtime reports and hosted evidence separately.
- The [environment runbook](ENVIRONMENT.md) records the proposed H2/Java 21 path, tested tool inventory, planned PostgreSQL durability demonstration subject to team and instructor agreement and external Angular evaluation. Startup, auth/CORS, seed/reset, Chromium launch, accepted frontend SHA and persistent-storage interpretation are open.
- Resolve the team test basis, owner–pet and duplicate expectations, use-case split, early estimates and shared management ownership on the schedule in [progress](PROGRESS.md) and [blockers](ENVIRONMENT.md#blockers-and-dependencies).

## Next action

For the current approval, verify and record the local commit SHA and parent. Before any separately authorized push, run the read-only `git ls-remote origin refs/heads/master refs/heads/feat/area3-24i-3014` from the student's PowerShell and compare with the local snapshots. Identify any new integration commits and stop for review if the feature tip differs unexpectedly. Preserve any personal verification details in the AI-use record. Record the actual commit and push metadata in the next authorized documentation checkpoint.
