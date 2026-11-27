# Member C project documentation

SE3002 Software Quality Engineering, Fall 2026. Member C owns Area 3 and leads the shared test plan, weekly reports and completion report.

Every continuation session reads these files before making changes:

1. This README and the [handover](HANDOVER.md) for the current Git state, exact checkpoint scope and next authorization.
2. [Project contract](PROJECT_CONTRACT.md), [assignment checklist](REQUIREMENTS_CHECKLIST.md) and [progress](PROGRESS.md) for the test basis, ownership and documented status.
3. [Repository audit](REPOSITORY_AUDIT.md) and [environment runbook](ENVIRONMENT.md) for source and CI facts and the proposed setup.
4. [Implementation plan](IMPLEMENTATION_PLAN.md), [decisions](DECISIONS.md) and [manual actions](MANUAL_ACTIONS.md) for checkpoint boundaries, approvals and coordination.
5. [Evidence register](EVIDENCE_REGISTER.md), [effort log](EFFORT_LOG.md) and [viva preparation](VIVA_PREPARATION.md) for proof, person-hours and demonstrations.
6. [Shared test plan](../test-management/TEST_PLAN.md), [GQM definitions](../test-management/GQM_METRICS.md) and [weekly reporting](../test-management/WEEKLY_PROGRESS_TEMPLATE.md) for the proposed management foundation.
7. [Commit log](COMMIT_LOG.md) and [AI-use record](AI_USE_RECORD.md) for actual commits, assistance disclosures and student verification.

Checkpoints 0.1 and 0.2 were committed and pushed according to the student's PowerShell evidence. Checkpoint 0.3 passed the reported independent technical review. The student reported personal review and authorized a local commit. Push and Phase 1.1 each require separate authorization. [Current handover](HANDOVER.md).

## Fresh-chat continuation

Start every subsequent checkpoint or phase in a fresh Codex chat unless the student explicitly decides otherwise. Antigravity may take over in the same workspace and follows the same continuation rules. Every agent must reconstruct the state from Git, the persistent documents and the most recent authorized prompt. Prior chat context and historical authorizations cannot serve as authority for new work.

Use the handover as the authoritative record of documented progress and check it against the actual worktree and Git state. Preserve and review any uncommitted checkpoint before continuing. Confirm the preceding checkpoint's commit and push have been resolved as instructed and obtain the separate authorization for the next checkpoint.

The exact initialization, 0.1 correction, 0.1 commit approval, 0.2 audit, 0.2 commit approval and 0.3 planning prompts are retained locally under `prompts/generated/`. The [AI-use record](AI_USE_RECORD.md#prompt-index-and-recovery) indexes these copies and identifies prompts still to recover. Git ignores `generated/`. For a fresh clone or missing copy, recover the prompt from the student's private backup or the original chat attachment and supply it privately to the next session.
