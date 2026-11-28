# Member C manual actions

MA-01 records the Phase 1.1 review. Both Phase 1.1 commits were published. Phase 1.2 is locally committed and awaits a separate publication decision under MA-02. Team coordination, estimation, registration confirmation and private prompt recovery can proceed when their prerequisites are met.

| ID and checkpoint | Purpose and prerequisite | Instructions and expected result | Evidence to return | Impact and status |
| --- | --- | --- | --- | --- |
| MA-01 · 1.1 | Record the student's reported review of the Angular setup checkpoint. | The reported scope is the 15 Member C Markdown files and the setup script covered by ChatGPT's review. Codex separately verified the seven raw and summary evidence files. | The review statement and two-commit authorization are recorded in the [AI-use record](AI_USE_RECORD.md). | Reported review and commit authorization complete. Individual student checks and changes remain unspecified. |
| MA-02 · 0.3 onward | Commit and push on the feature branch. Requires reviewed staging scope and separate authorizations. | Review `git status --short --branch` and `git diff --cached --name-status` before an authorized commit. Compare `git ls-remote origin refs/heads/master refs/heads/feat/area3-24i-3014` with the local snapshots before a separately authorized push. Verify the hosted feature tip after the push. | Commit SHA/parent and path list, read-only lookup output, push result and final HEAD/tracking/hosted refs. | Both Phase 1.1 commits are published. The student merge and four Phase 1.2 commits are local. The publication decision is pending. Hosted master has advanced through PR #12. |
| MA-03 · 1.1–1.3 | Supply Git or download access if Codex cannot reach an approved service. Requires phase authorization and approved evaluation SHA for acquisition. | Run the [runbook's exact sibling acquisition](ENVIRONMENT.md#angular-acquisition-and-compatibility) in the student's PowerShell if needed. Use `git ls-remote https://github.com/spring-petclinic/spring-petclinic-angular.git HEAD refs/heads/master 'refs/tags/*'` when reference evidence is needed. Acquire Maven, npm and browser downloads only in the authorized checkpoint. | Command status, full checkout SHA, lock checksum and any first meaningful error. | Phase 1.1 GitHub, Node distribution and npm registry access succeeded in the approved session. Conditional for later authorized steps. |
| MA-04 · 1.1, 1.3, 5, 6 | Enable approved tool installations/version changes. Requires exact locked requirements. | Follow the [runbook](ENVIRONMENT.md#toolchain-on-2026-11-28). If the pinned engines require it, select an official exact Node release and record `node --version` and `npm.cmd --version`. Use the locked Playwright CLI for matched Chromium when missing; install the approved Lighthouse/axe and passive ZAP/dependency tooling only in their phases. | Exact versions, install/verification outcome and platform. | Phase 1.1 portable Node 24.22.3/npm 11.19.1 verified. Conditional for later authorized steps. |
| MA-05 · 1.2 durability plan / 8 if needed | Provide an approved isolated Docker engine/profile. Requires MA-09 storage decision or agreed recovery design. | Start Docker Desktop manually if required and verify `docker info --format '{{.ServerVersion}}'`. Follow the reviewed local profile/volume instructions with A. Confirm expected container/profile/volume before the durability or recovery run. | Engine version and relevant profile/volume identifier or startup error. | Conditional blocker for the Docker-based work. Pending. |
| MA-06 · 1.2, 5, 6 | Resolve a UI-only step when the runner cannot perform it. Requires the secured app, correct role and matching build. | Use logged-in Chrome DevTools Lighthouse or required passive GUI setup only when the automated route is unavailable. Return the original output. | Route, screen landmark, role and statuses or original report. | Phase 1.2 completed its browser probe without manual execution. Later GUI work is conditional. |
| MA-07 · 0.3–2 and 9 | Coordinate team ownership, requirements and current evidence. | Follow up with A and B using the message below. Record genuine replies before adopting shared changes or assessed cases. | Versioned paths, owner replies and genuine report links. | Outreach reported. Replies on shared adoption, test basis and case ownership are pending. |
| MA-08 · Each phase, especially 7 | Track effort as incurred and reserve a comparable k6 run window when needed. Requires owner availability and approved protocol for performance. | Give genuine prospective estimates before upcoming work and log actual person-hours as work happens. Separate attended effort and unattended elapsed time. In Phase 7 use the same machine and starting state and an agreed quiet run window. | Owner/dates/estimates, actual effort log and run-condition notes. | Prospective timing is time-sensitive. The quiet window gates comparable measured runs. Pending owner inputs. |
| MA-09 · 1.2, 2 and registration as soon as feasible | Resolve disputed assignment interpretations and registration evidence. | Ask the instructor what durable-storage proof they expect and whether the proposed state transition or compound rule qualifies if disputed. Confirm the BSE-5A Sheet row, repository/uniqueness and dates against the announcement and five-day rule. | Instructor guidance and restricted evidence of the row/date and announcement. | Interpretation gates affected storage/flows and registration evidence gates final scope claim. Authorized setup can proceed. Pending. |
| MA-10 · 0.3 onward, finish in 9 | Maintain original AI evidence and personal verification. | Follow the [prompt recovery index](AI_USE_RECORD.md#prompt-index-and-recovery), save exact prompts and full reviews privately, confirm backup and record what was personally checked or changed with dates. Before submission agree on restricted instructor access to originals. | Updated AI-use record and private backup/access confirmation without the prompt content in Git. | MA-01 tracks Phase 1.1 personal review. Earlier review statements and outstanding details are in the AI-use record. Remaining verification and disclosure gate final submission. |
| MA-11 · 9 | Verify own contribution, viva and final group archive. Requires approved criteria and actual results. | Rehearse a rerun and explain assertions and rule/spec change impact. Use the [viva notes](VIVA_PREPARATION.md). Confirm the PDF and ZIP filenames, public repo link, full `.git`, clean extraction and exclusions. Student/team handles the external submission. | Rerun note, personal review and archive manifest/extraction checks. | Blocks final delivery. Planned. |

Phase 1.1 confirmed both hosted refs through a read-only lookup. MA-02 describes the renewed check before any separately authorized push. A changed master ref warrants inspection and agreement before an integration decision.

## Optional screenshot catalogue

Every image below is supplementary. The brief calls for genuine reports and execution evidence. Capture only after the named run or operation exists and use the [register’s storage rules](EVIDENCE_REGISTER.md#run-record-and-storage).

| Evidence ID and requirement | Screen or terminal | What must be visible | Prerequisite | Suggested filename |
| --- | --- | --- | --- | --- |
| E-SUB-01 · S02 | BSE-5A registration tab | Only the relevant row, its section and available registration date | Genuine registered row and privacy-safe view | `section-registration.png` |
| E-USAB-01 · C03, C04 | Local task form or actual Playwright report | Correction-specific feedback or keyboard/focus assertion context | Actual invalid-input or keyboard test executed | `usability-task-feedback.png` |
| E-A11Y-03 · C06 | Before and after instances of the selected key screen | Same screen state with the changed accessible element | Both tools run before and after under comparable conditions | `accessibility-screen-before.png`, `accessibility-screen-after.png` |
| E-SEC-01 · C07 | Sanitized local Playwright report | Role label, action and observed outcome | Real authorized/denied role checks completed | `security-role-result.png` |
| E-CI-02 · M10, F01 | GitHub PR checks view | Actual blocked state and failed required check, without secrets | Hosted blocked PR outcome exists and is accessible | `blocked-pr-check.png` |

## Copy-ready team message

Hi Eiman and Hamza, I verified the W1/W2 browser writes and independent readbacks using Angular `f497b8f` and the existing `e2e/` proxy. On that pin the merged vet smoke passed, while W1 stopped at the blank Owner field after the successful API read. Please confirm these inputs before shared E2E changes or case approval.

- Test basis with at least eight finalized functional requirements (at least three per workflow) and four business rules. Please identify the boundary, compound Boolean decision and state transition, and specify duplicate/retry and Owner–Pet association expectations.
- Both of you, please confirm the six distinct use-case IDs with two per person and explicitly confirm who owns UC1 and UC2.
- Eiman, please confirm the Angular revision used for PR #11 and the later full-use-case run, whether `e2e/` is canonical, and its maintainers and contribution rules. Please share the original browser run evidence and context for the defect #9 expected failure. The reported Area 1 document gives different EC1/EC2 definitions and severity proposals from earlier input. Please identify the versions the team should review.
- Hamza, please share the canonical traceability and defect records and the source or approved rationale for duplicate/retry and Owner–Pet association behavior. Please flag any expectation still under discussion.
- Please supply any genuine prospective estimates and actual person-hours you have recorded. For upcoming work, include optimistic, most likely and pessimistic person-hours and the estimate date. Mark missing values pending.

Quick reply format

```text
Owner / area:
Requirements + rule link (boundary, compound decision, transition):
Duplicate and Owner–Pet expectations:
My two case names / IDs:
Risk + exit criteria OR traceability + defect links:
Source and runtime evidence links:
Estimate date / scope / O–M–P person-hours / prospective or retrospective:
Actual person-hours + period (or unknown):
Preferred shared docs and browser paths:
Open decision / needed by:
```
