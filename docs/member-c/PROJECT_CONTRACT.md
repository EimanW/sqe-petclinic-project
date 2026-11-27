# Member C project contract

## Scope and test basis

This is the three-person SE3002 Software Quality Engineering final project at FAST-NUCES. The team repository is [EimanW/sqe-petclinic-project](https://github.com/EimanW/sqe-petclinic-project). The MIS combines Spring PetClinic REST with the upstream Angular UI. The selected module is Owners/Pets.

| Workflow | Starting condition | User action and intended outcome |
| --- | --- | --- |
| W1 | An existing synthetic owner and an authorized account | Add a pet to the owner. The UI should confirm creation and the resulting Pet should be available under that owner. |
| W2 | The Pet from W1 exists under that owner | Book a visit for the Pet. The UI should confirm the visit and the persisted Visit should reference that Pet. |

The official assignment, the versioned source/configuration and OpenAPI definition, and observed local behavior are the sources of truth. Document expected behavior using a specific source or a team-approved, justified assumption. Treat reported behavior and defects as pending until corroborated. The acceptance status of each requirement lives in [REQUIREMENTS_CHECKLIST.md](REQUIREMENTS_CHECKLIST.md).

Capture the upstream baseline, integrated team revision, frontend pin and Member C revisions independently. Confirm permission and validation expectations, duplicate behavior and persisted postconditions before writing assertions. Use the actual documented data model to justify the mandatory state-transition rule with the team. The application source reviewed at 0.1 supplies no visit status lifecycle.

## Ownership

| Owner | Responsibilities |
| --- | --- |
| Member A | Area 1, including static analysis, white-box tests, mutation and CI quality gates. Leads the risk register and exit criteria. Owns two browser use cases and their flows. |
| Member B | Area 2, including OpenAPI test basis, generated data, API and integration testing. Leads traceability, defect records, root cause and Pareto analysis. Owns two browser use cases and their flows. |
| Member C | Area 3, including two owned use cases, shared Playwright conventions, usability, accessibility, defensive security, performance and recovery. Leads the shared test plan, weekly reporting and completion report. |
| All members | Coordinate shared scope, risks, release criteria and documentation. Supply individual estimates, actual effort, commits and AI-use verification. Each member must be able to explain the evidence in the viva. |

Keep original tests apart from upstream tests. Current team Java tests are under `src/test/java/org/springframework/samples/petclinic/team/`. Confirm `tests/team/e2e/` or a team-approved equivalent before adding Playwright. Add no teammate-owned flows without explicit coordination. Reuse shared documents that exist. If the team has no shared location, propose `docs/test-management/` for a single shared copy.

## Authority and Git checkpoints

- Documentation approval, committing, pushing, opening a pull request and starting a new checkpoint each require separate explicit decisions. Resolve the preceding checkpoint's commit and push as instructed before starting the next one. Follow the [implementation plan](IMPLEMENTATION_PLAN.md) and current handover for checkpoint scope and prerequisites.
- A development commit may proceed with pending student-verification details when the student explicitly authorizes it and the pre-commit checks pass. Record automated verification, Codex self-review, independent AI-assisted review and personal student verification separately. The student must personally verify AI outputs and record the checks before final submission.
- Use `feat/area3-24i-3014`. Check status, Git identity, remotes, branches, log, tags, baseline, HEAD and local `origin/master` before editing. Stop on a wrong branch or unrelated edits. Treat `master` as protected under team instructions. Hosting-side protection is still unverified.
- Preserve shared and upstream code, existing team tests, CI and all quality thresholds. For any legitimate shared change, obtain approval on the exact proposed diff before making it. Preserve genuine test failures and approved production rules.
- Stop at each independently reviewable checkpoint. Review status and diff, run relevant validation, record exact staging paths, evidence and unresolved issues, and update [PROGRESS.md](PROGRESS.md), [HANDOVER.md](HANDOVER.md) and [COMMIT_LOG.md](COMMIT_LOG.md). Propose one meaningful Conventional Commit for each coherent change. Separate setup, implementation, tests, fixes and evidence where it improves review.
- Require explicit authorization before committing or pushing. Stage reviewed paths individually. Push only to the Member C feature branch after authorization, then verify the commit and remote branch. Direct pushes to `master` and force pushes are prohibited. Require separate authorization for pull requests, merges, GitHub Issues and protection changes. Preserve published history, teammate branches and full submission history.
- A separate frontend must be acquired outside the backend tree only after approval. Pin an exact revision, retain its lockfile, verify the pairing, and record a reproducible way to apply any team-owned frontend change from the final team repo. No nested checkout or submodule is authorized.

Start each subsequent checkpoint or phase in a fresh Codex chat unless the student explicitly decides otherwise. Antigravity may continue in the same workspace. Reconstruct the state from Git, the persistent documents and the most recent authorized prompt without relying on prior chat context. The handover is authoritative for recorded progress and must be checked against Git and the worktree. Preserve and review any uncommitted checkpoint. New actions require current authorization. Follow [README.md](README.md) to recover exact prompts privately.

## Phase boundaries

| Phase | Outcome |
| --- | --- |
| 0 | Audit, assignment mapping and documentation. Review checkpoints 0.1–0.3 individually. |
| 1 | Reproducible local backend, approved pinned frontend, verified auth and seed/reset, and a team-owned Playwright foundation. |
| 2 | Two use cases per member and a shared flow matrix. |
| 3 | Member C's browser flows with visible outcomes, persistence assertions and genuine execution reports. |
| 4 | One usability task per workflow, including an error-correction scenario and keyboard-only form navigation and submission. |
| 5 | Key-screen Lighthouse and axe-core results, one improvement and comparable before/after evidence. |
| 6 | Passive security assessment, validated role controls and reproducible dependency auditing on isolated local synthetic data. |
| 7 | One workflow operation, predeclared limits and four comparable k6 runs. |
| 8 | Automated controlled recovery of one actual write, including before/after persistence and exactly-once verification. |
| 9 | CI integration, final test management updates and completion report, evidence review, release or hold decision, and viva and submission preparation. |

Keep security enabled for the local assessment. Isolate role credentials and corroborate authorization at the server. Keep any separately approved UI-only configuration identifiable. Security checks use normal local application actions, source review and passive ZAP. Preserve original artifacts and separately identify observations that still require verification.

Establish the initial shared test plan during checkpoint 0.3 or before technical implementation. Obtain three-point estimates before the corresponding work starts. Record actual effort as work happens and maintain weekly reports throughout development.

## Evidence and academic integrity

Use synthetic records and local test accounts. Keep real credentials, tokens, personal information, dependencies and caches out of commits. Preserve original evidence and record the generating command, tool version, environment, commit and date. Never fabricate or edit evidence, including screenshots, tool reports, pipeline runs and test results. Count each executable case once within its level.

Follow the assignment's prohibitions on plagiarism, unauthorized sharing and outside help. Never copy another group's work or pass off upstream tests, online material or previously reported team results as your own. Preserve individual authorship and commit history. For permitted AI assistance, keep the exact prompts, disclose the tool, purpose and output, and record the student's personal verification and changes after they occur in the [AI-use record](AI_USE_RECORD.md).
