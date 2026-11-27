# Member C project documentation

SE3002 Software Quality Engineering, Fall 2026. Member C owns Area 3 and leads the shared test plan, weekly reports and completion report.

Every continuation session reads these files before making changes:

1. This README for document order and current scope.
2. [PROJECT_CONTRACT.md](PROJECT_CONTRACT.md) for repository restrictions, ownership and phase boundaries.
3. [PROGRESS.md](PROGRESS.md) for dated results and pending work.
4. [HANDOVER.md](HANDOVER.md) for the exact continuation point and evidence.

The [requirements checklist](REQUIREMENTS_CHECKLIST.md) maps the official brief to deliverables and acceptance evidence. The [commit log](COMMIT_LOG.md) distinguishes proposed checkpoints from actual commits. The [AI-use record](AI_USE_RECORD.md) records assistance and pending student verification.

Checkpoint 0.1 covers the project contract, assignment checklist and initial continuation records. Checkpoint 0.2 will hold the full audit and environment commands. Checkpoint 0.3 will add the implementation plan, initial shared test management, evidence register, decisions, effort log, manual actions and viva preparation.

## Fresh-chat continuation

Start every subsequent checkpoint or phase in a fresh Codex chat unless the student explicitly decides otherwise. Antigravity may take over in the same workspace and follows the same continuation rules. Every agent must reconstruct the state from Git, the persistent documents and the most recent authorized prompt. Prior chat context and historical authorizations cannot serve as authority for new work.

Use the handover as the authoritative record of documented progress and check it against the actual worktree and Git state. Preserve and review any uncommitted checkpoint before continuing. Confirm the preceding checkpoint's commit and push have been resolved as instructed and obtain the separate authorization for the next checkpoint.

The exact initialization, 0.1 correction and 0.1 commit approval prompts are retained locally under `prompts/generated/`. The [AI-use record](AI_USE_RECORD.md) indexes these copies and identifies prompts still to recover. Git ignores `generated/`. For a fresh clone or missing copy, recover the prompt from the student's private backup or the original chat attachment and supply it privately to the next session.
