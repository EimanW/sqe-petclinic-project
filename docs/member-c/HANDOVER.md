# Member C handover

## Continuation point

| Item | Verified or reported state on 2026-11-28 (Asia/Karachi) |
| --- | --- |
| Current boundary | Checkpoint 0.3 passed the reported independent technical review. The student also reported personally reviewing the checkpoint and authorized one local commit of the 16 paths below after verification. Push and Phase 1.1 each require separate authorization. |
| Repository and branch | `D:\SQE-Project\sqe-petclinic-project` on `feat/area3-24i-3014`, tracking `origin/feat/area3-24i-3014`. Origin is `https://github.com/EimanW/sqe-petclinic-project.git`. Local author name matches the student. |
| Pre-commit HEAD and tracking | Both local refs resolved to `48450e0619d6915664e7a5e405f4de1ff7eea272`, checkpoint 0.2, with eight changed paths. Its parent is `8cefbb958b491b29a46ba9ecddaa3b021b0a3c75`, checkpoint 0.1, with seven changed paths. The student supplied separate successful push evidence for both. Read local Git for the 0.3 SHA after creation. |
| Integration and baseline | Local `origin/master` and 0.1 parent are `9e318fa35ec0917dec5be8009095bae44278b9c0`. Before the 0.3 commit the comparison was 0 behind, 2 ahead. The `baseline` tag at `220f4ba8e92cec9ad1faa03324723a3e9da63d95` resolved and was an ancestor of pre-commit HEAD. Master is protected by team instruction. |
| Hosted evidence supplied by student | The student successfully ran `git ls-remote origin refs/heads/master refs/heads/feat/area3-24i-3014`. The lookup returned feature `48450e0619d6915664e7a5e405f4de1ff7eea272` and master `9e318fa35ec0917dec5be8009095bae44278b9c0`, matching the local tracking refs at lookup time. Hosting-side protection and CI enforcement still await evidence. MA-02 specifies the next pre-push lookup. |
| Entry diff | Only `docs/member-c/AI_USE_RECORD.md` was unstaged. It updated the 0.2 row and added the student's reported manual review of all eight files. Both parts were retained. This is separate from the independent ChatGPT review and leaves specific checks, changes and runtime evidence pending. |
| Private sources | The exact 0.3 planning and commit-approval attachments were copied into the existing ignored prompt directory and checked via SHA-256. Earlier recovery tasks stay open. The official PDF remains locally ignored. |

## Proposed staging list

| Change | Path |
| --- | --- |
| New | `docs/member-c/IMPLEMENTATION_PLAN.md` |
| New | `docs/member-c/DECISIONS.md` |
| New | `docs/member-c/EVIDENCE_REGISTER.md` |
| New | `docs/member-c/MANUAL_ACTIONS.md` |
| New | `docs/member-c/EFFORT_LOG.md` |
| New | `docs/member-c/VIVA_PREPARATION.md` |
| New | `docs/test-management/TEST_PLAN.md` |
| New | `docs/test-management/GQM_METRICS.md` |
| New | `docs/test-management/WEEKLY_PROGRESS_TEMPLATE.md` |
| Modified | `docs/member-c/README.md` |
| Modified | `docs/member-c/REQUIREMENTS_CHECKLIST.md` |
| Modified | `docs/member-c/PROJECT_CONTRACT.md` |
| Modified | `docs/member-c/PROGRESS.md` |
| Modified | `docs/member-c/HANDOVER.md` |
| Modified | `docs/member-c/COMMIT_LOG.md` |
| Modified | `docs/member-c/AI_USE_RECORD.md` |

Authorized commit subject is `docs(area3): add implementation plan and evidence tracking`. Audit and runbook remain historical 0.2 snapshots. For historical commands see `git show 48450e0:docs/member-c/HANDOVER.md`.

## Decisions and dependencies to carry forward

- Reuse the secured REST repository. Acquire the exact Angular candidate only in a sibling checkout. `f497b8fd61ec9cb896aa09447263b8872fb233be` awaits evaluation approval and runtime verification. Select Node from exact pinned engines and confirm download access. [Decisions](DECISIONS.md) · [Runbook](ENVIRONMENT.md).
- Record the approved evaluation SHA, Node/npm version, lock checksum and upstream notices in 1.1. In 1.2 prove secured browser integration, Basic role and H2 reset outcomes. Obtain assessor clarification for durable storage and coordinate any PostgreSQL/volume work with A.
- Confirm the shared docs and Playwright path with A/B. Deliver the secured common fixtures, smoke and contributor instructions at 1.3. Final requirements, business rules, association and duplicates, oracles and six-use-case split gate Phases 2/3. Member C candidates are UC-C01 and UC-C02. A/B still supply two distinct cases each.
- A's risk/exit evidence and B's traceability/defect inputs gate adoption of affected criteria and final metrics/reporting. A's actual Surefire, JaCoCo, PIT/survivor and Sonar reports and hosted enforcement proof remain pending. The local source count is 72 test methods. Shared test management drafts are proposals and should defer to any canonical material surfaced by the team. [Shared plan](../test-management/TEST_PLAN.md).
- Weekly measures and real person-hours begin as evidence becomes available. The first snapshot records genuine 0.1/0.2 progress. [Weekly baseline](../test-management/WEEKLY_PROGRESS_TEMPLATE.md#initial-reporting-baseline) · [Effort log](EFFORT_LOG.md).

## Verification and next authorized action

Local Git checks covered branch, tracking, identity, previous commit contents, baseline ancestry and diff scope. Git-bundled `pdftotext.exe -layout -enc UTF-8` extracted the seven-page source and `Get-FileHash -Algorithm SHA256` matched the prior PDF hash. `Copy-Item -LiteralPath` and SHA-256 comparison preserved the prompt in the ignored directory. Markdown verification used `git diff --check`, `git diff --cached --check` and direct Python reads of all 18 planning Markdown files. It checked 36 changed or new tables, 221 relative links and anchors, all 39 checklist IDs and six transitions, 32 unique evidence IDs and the proposed PERT total. The worktree has only the 16 proposed staging paths outside ignored files. Runtime activities and Git publication were outside this checkpoint.

The student supplied the independent review result for all 18 documents. It confirmed eight columns for each of the 39 unique checklist IDs, counts of 22 NOT STARTED, 15 IN PROGRESS, 2 NEEDS HUMAN REVIEW and 0 VERIFIED, 32 unique evidence IDs, clean trailing whitespace and PERT (44 + 4 × 79 + 144) / 6 = 84 proposed person-hours. The Area 3 effort baseline awaits explicit adoption, and a PostgreSQL demonstration may add work. The shared plan and three draft exit criteria await team approval and canonical risk links.

Verify the authorized commit's actual SHA and parent in local Git. Record the SHA and successful push evidence at the next authorized documentation update. Before separately authorized publication, use the read-only Git command in MA-02 and retain its output. Once the 0.3 commit and push are verified, start a fresh Codex or Antigravity session with a separate 1.1 prompt. Reconstruct the repository state and check candidate approval, Git access, the intended sibling path and the prospective personal estimate before acquisition. Independent 1.1 work can proceed without the later test-basis decisions. [Manual actions](MANUAL_ACTIONS.md) · [Phase 1.1](IMPLEMENTATION_PLAN.md#11--evaluate-and-acquire-angular).
