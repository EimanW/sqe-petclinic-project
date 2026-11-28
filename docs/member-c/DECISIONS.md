# Member C decision register

Dated 2026-11-28 (Asia/Karachi). `ACCEPTED` records a student-authorized project boundary or an assignment requirement. Technical choices awaiting evidence or agreement are `PROPOSED` or `BLOCKED`. When a decision is replaced, mark it `SUPERSEDED` and link the replacement. Dates below reflect entry creation.

## D-01 — Reuse the team REST repository

**2026-11-28 · ACCEPTED**

- **Context and alternatives.** A versioned backend, local baseline and A’s work already exist. Reuse it or bring in a second upstream checkout.
- **Selected option and rationale.** Reuse the team repository and preserve the upstream baseline. Shared changes require review against current team history.
- **Evidence.** [Repository audit](REPOSITORY_AUDIT.md#repository-and-baseline).
- **Responsibility and approval.** C. project scope supplied by student.
- **Reconsideration trigger.** Revisit if the authorized team repository changes.

## D-02 — Acquire Angular externally

**2026-11-28 · ACCEPTED**

- **Context and alternatives.** The backend contains no Angular package or submodule. Sibling checkout, nested checkout, or vendoring.
- **Selected option and rationale.** Acquire in the sibling directory after 1.1 authorization. Reproduction depends on access to the pin or an agreed archive.
- **Evidence.** [Environment runbook](ENVIRONMENT.md#angular-acquisition-and-compatibility).
- **Responsibility and approval.** The student authorized Phase 1.1 acquisition. C verified the sibling checkout and origin.
- **Reconsideration trigger.** Revisit if clean extraction cannot reproduce the frontend.

## D-03 — Evaluate an exact Angular revision

**2026-11-28 · ACCEPTED FOR EVALUATION**

- **Context.** The student authorized evaluation of `f497b8fd61ec9cb896aa09447263b8872fb233be`. The exact commit was acquired, installed and compiled.
- **Selected option and rationale.** Keep this immutable checkout for the next separately authorized compatibility checks. The source review identifies payload, authentication and CORS questions.
- **Evidence.** [Candidate, build and limitations](ENVIRONMENT.md#angular-acquisition-and-compatibility).
- **Responsibility and approval.** C with A. Team adoption remains pending secured runtime evidence and team agreement.
- **Reconsideration trigger.** Reopen on lock, licence, payload or secured browser incompatibility.

## D-04 — Select Node and npm from the pinned inputs

**2026-11-28 · VERIFIED FOR PHASE 1.1**

- **Context.** System Node 24.11.0 falls below the exact Angular 22.1.0 Node 24 minimum of 24.15.0.
- **Selected option and rationale.** Node 24.22.3 and bundled npm 11.19.1 from the official x64 ZIP satisfy the pinned engines and completed the locked install and build. Activation uses a session-local PATH and the portable runtime lives outside the team worktree.
- **Evidence.** [Pinned toolchain and build](ENVIRONMENT.md#phase-11-pinned-checkout).
- **Responsibility and approval.** C verified the portable pairing under the student's Phase 1.1 authorization. System-level changes require separate approval.
- **Reconsideration trigger.** Revisit after an engine mismatch or changed frontend pin.

## D-05 — Keep security enabled for assessed workflows

**2026-11-28 · ACCEPTED**

- **Context and alternatives.** The brief requires access decisions and control evidence. Use the secured setting throughout or use a separately approved UI-only diagnostic setting.
- **Selected option and rationale.** Use the secured configuration for W1/W2 and all security evidence. The setup must handle real browser authentication even during test bootstrapping.
- **Evidence.** [Project contract](PROJECT_CONTRACT.md#phase-boundaries).
- **Responsibility and approval.** C and A. shared security changes need approval.
- **Reconsideration trigger.** Revisit the diagnostic setup if auth blocks reproducibility.

## D-06 — Determine browser authentication and CORS from runtime

**2026-11-28 · VERIFIED FOR LOCAL PHASE 1.2**

- The direct Angular origin hit HTTP 401 and lacked CORS permission on both the owner GET and JSON preflight.
- The Phase 1.2 pairing uses the merged loopback proxy with a development-only relative API URL in a disposable workspace. It preserves the backend Basic challenge and uses separate browser contexts for admin and vet.
- C owns the environment evidence. A must confirm the shared adoption and approve the exact diff for a shared change. [Authentication runbook](ENVIRONMENT.md#authentication-and-cors).
- **Reconsideration trigger.** Reopen on preflight failure, missing credentials or role bleed.

## D-07 — Use H2 for local workflow reset

**2026-11-28 · VERIFIED FOR LOCAL PHASE 1.2**

- The secured H2 profile restored the initial seed after replacing the JVM. The marker was readable before restart and returned 404 afterward.
- Start each compatible run from verified seed or use an isolated instance per worker. A and B must agree on the team fixture and reset convention. [Reset evidence](PHASE_1_2_RESULTS.md#secured-backend-and-h2-reset).
- **Reconsideration trigger.** Revisit for nondeterministic or concurrent state changes.

## D-08 — Demonstrate durability separately with PostgreSQL

**2026-11-28 · PROPOSED**

- The brief requires persistent storage. Phase 1.2 H2 demonstrates process-local relational readback and loss on JVM restart. The checked Compose service declares no named data volume. Instructor guidance on the required proof and approval for a separate demonstration remain pending.
- **Selected option and rationale.** Ask the instructor to confirm expectations. Propose the PostgreSQL profile and an approved named volume with restart readback. Adds Docker and profile setup. H2 remains useful for two-account tests.
- **Evidence.** [Durable storage plan](ENVIRONMENT.md#database-strategy).
- **Responsibility and approval.** C and A. instructor interpretation and shared configuration approval.
- **Reconsideration trigger.** Revisit after assessor guidance or unavailable Docker.

## D-09 — Share one Playwright package with isolated fixtures

**2026-11-28 · PROPOSED**

- Reviewed master already provides `e2e/` with locked runner, Chromium config and teammate specs.
- Use that package as the provisional shared entry point. C coordinates confirmation of maintainers, contribution conventions and separate role/fixture behavior with A and B.
- A and B must confirm the assessed use-case split and review how the spec merged on hosted master through PR #12 fits the pinned environment. [Phase 1.3](IMPLEMENTATION_PLAN.md#13--adopt-the-shared-browser-framework).
- **Reconsideration trigger.** Revisit if a canonical suite or different CI entry point is discovered.

## D-10 — Isolate the recovery test

**2026-11-28 · PROPOSED**

- **Context and alternatives.** Recovery needs a controlled fault, a known datasource and explicit CI selection. Separate C test package or approved separate integration runner.
- **Selected option and rationale.** Propose `teamrecovery/` with its own profile and fresh committed-state reads. A dedicated test selection needs CI coordination and makes the profile explicit.
- **Evidence.** [Database strategy](ENVIRONMENT.md#database-strategy).
- **Responsibility and approval.** C with A. approval of fault, profile and CI boundary.
- **Reconsideration trigger.** Revisit if the fault cannot be reproduced before commit or the profile is unsuitable.

## D-11 — Use passive local security assessment

**2026-11-28 · ACCEPTED**

- **Context and alternatives.** The brief specifies passive inspection and normal workflow actions. Passive capture and source review or restrict the exercise to source review alone.
- **Selected option and rationale.** Use local synthetic tests, passive ZAP and dependency audit with evidence of both workloads. Coverage is bounded by observed traffic and audited components.
- **Evidence.** [Phase 6](IMPLEMENTATION_PLAN.md#phase-6--defensive-security).
- **Responsibility and approval.** C. A corroborates controls, B triages findings.
- **Reconsideration trigger.** Recheck scope whenever proxy mode, host or credentials change.

## D-12 — Preserve CI gates and add explicit test levels

**2026-11-28 · PROPOSED**

- **Context and alternatives.** Local CI has gates and a possible empty test selection. Extend canonical CI with explicit jobs or reuse one job with explicit nonempty selections.
- **Selected option and rationale.** A owns integration with A/B/C maintaining their suites. Preserve existing thresholds and prove real enforcement. Adds runtime and artifact coordination while exposing actual execution.
- **Evidence.** [Quality gate audit](REPOSITORY_AUDIT.md#member-a-tests-and-quality-gate).
- **Responsibility and approval.** A with B/C. team approval before changes.
- **Reconsideration trigger.** Revisit when the hosted pipeline or coverage basis changes.

## D-13 — Preserve genuine evidence

**2026-11-28 · ACCEPTED**

- **Context and alternatives.** The brief grades reproducibility and prohibits fabricated or edited evidence. Keep original tool artifacts and separate interpretation, or rely solely on report summaries.
- **Selected option and rationale.** Keep generated originals privately if sensitive. Register provenance and publish safe summaries and safe originals. Large or sensitive artifacts may need access controls and stable links.
- **Evidence.** [Evidence register](EVIDENCE_REGISTER.md).
- **Responsibility and approval.** Each owner. C coordinates evidence publication.
- **Reconsideration trigger.** Reconsider storage if access, size or credential checks fail.

## D-14 — Use persistent fresh-chat handovers

**2026-11-28 · ACCEPTED**

- **Context and alternatives.** Historical prompts are private and earlier authorization is checkpoint-specific. Use a fresh chat reconstructed from files or continue a session if the student requests it.
- **Selected option and rationale.** Maintain Git-checked handover, progress, prompt index and exact private copies. Requires regular concise updates, but makes Codex or Antigravity continuation reproducible.
- **Evidence.** [Project contract](PROJECT_CONTRACT.md#authority-and-git-checkpoints).
- **Responsibility and approval.** C and student. explicit exception if continuing the session.
- **Reconsideration trigger.** Update when the next confirmed checkpoint supersedes this one.

## D-15 — Use logical incremental commits and separately authorized pushes

**2026-11-28 · ACCEPTED**

- **Context and alternatives.** The branch is dedicated and master is protected by team instruction. One coherent commit per reviewable checkpoint or group closely related changes when appropriate.
- **Selected option and rationale.** Review explicit paths, commit on feature branch only and request separate push authorization. Review adds approval steps and preserves contributor history.
- **Evidence.** [Commit log](COMMIT_LOG.md).
- **Responsibility and approval.** Student authorizes each stage. hosting protection verification pending.
- **Reconsideration trigger.** Revisit staging if unexpected paths or remote changes appear.

## D-16 — Use the shared test management directory

**2026-11-28 · PROPOSED**

- **Context and alternatives.** Local inventory found no canonical management location. Hosted branches were unavailable. Adopt `docs/test-management/` or integrate with a canonical team location if identified.
- **Selected option and rationale.** Use the three provisional drafts for review and defer to any existing canonical artifacts. Team review is needed to prevent duplicate plans.
- **Evidence.** [Shared test plan](../test-management/TEST_PLAN.md).
- **Responsibility and approval.** C with A/B. team agrees before publication as canonical.
- **Reconsideration trigger.** Reopen when A/B provide paths, versions or approval.

## D-17 — Approve test basis and case ownership

**2026-11-28 · BLOCKED**

- **Context and alternatives.** Four business rules and at least eight requirements must support six use cases. Use source-grounded rules with justified assumptions where needed. Evaluate different eligible transitions and compound decisions.
- **Selected option and rationale.** Pending association and duplicate semantics, state-transition and MCC eligibility, and a two-case split per member. Locks dependent flow oracles. Environment setup can proceed once separately authorized.
- **Evidence.** [Phase 2](IMPLEMENTATION_PLAN.md#phase-2--use-cases-and-coordination).
- **Responsibility and approval.** A/B/C. instructor confirms disputed eligibility if needed.
- **Reconsideration trigger.** Close once the versioned test basis and split are agreed.
