# Three-point estimates and actual effort

Planning draft dated 2026-11-28 (Asia/Karachi). All effort is in **person-hours**. Use PERT unless the team has adopted another suitable documented three-point method.

> Expected effort = (Optimistic + 4 × Most likely + Pessimistic) / 6

Choose optimistic, most likely and pessimistic values for the same named scope before starting the work. Record the owner, assumptions, date and method. Use actual minus expected as variance, where a positive value means extra person-hours. Compare equivalent scope only. Keep the original baseline and separately date and explain any re-estimate or scope change. Totals below use unrounded calculations and display two decimals.

## Team area estimates

| Area | Owner | Work scope | Optimistic | Most likely | Pessimistic | Expected | Estimate date | Basis | Actual effort | Variance | Variance explanation | Status |
| --- | --- | --- | ---: | ---: | ---: | ---: | --- | --- | --- | --- | --- | --- |
| 1 | Eiman | Static/white-box, mutation and CI, own browser work | Pending | Pending | Pending | Pending | Pending | Owner input, including any earlier genuine estimates | Pending owner input | Pending | Pending | Earlier prospective values and actuals unavailable in local evidence |
| 2 | Hamza | OpenAPI, generated data, API/integration, own browser work | Pending | Pending | Pending | Pending | Pending | Owner input, including any earlier genuine estimates | Pending owner input | Pending | Pending | Earlier prospective values and actuals unavailable in local evidence |
| 3 | Huzaima | Remaining Phases 1–9, including C-led shared foundation and reporting | 44 | 79 | 144 | 84.00 | 2026-11-28 | Proposed future task breakdown below | Pending owner input | Pending | Pending | Draft planning values awaiting student baseline approval |

Checkpoint 0.1–0.3 personal effort and any original estimates remain unknown in this log. For Area 3, the draft row covers future work only. Retain any genuine earlier owner estimates if found. If an owner later reconstructs an estimate, label it retrospective, date it when made and report its limits separately from the prospective baseline. Use the same treatment for A/B’s completed work. Record any separately approved durability run as a scope addition before execution if required.

## Proposed Member C breakdown for future work

These ranges are planning inputs prepared with Codex and await the student's confirmation. They assume an existing team backend, a single local dev machine, partial fixture reuse and timely A/B review. Human debugging and reviewed rework sit within the ranges. External waiting and unattended execution stay outside the totals.

| Phase | Task | Optimistic | Most likely | Pessimistic | Expected | Assumption or uncertainty |
| --- | --- | ---: | ---: | ---: | ---: | --- |
| 1.1 | Angular pin, Node selection and reproducible acquisition | 2 | 4 | 8 | 4.33 | Existing runbook and candidate. Allow time for download or engine problems. |
| 1.2 | Secured browser/API pairing, seed/reset and storage plan | 4 | 8 | 16 | 8.67 | Includes observed CORS/auth diagnosis. Shared source changes and approved durability execution may need re-estimation. |
| 1.3 | Shared Playwright framework, fixtures, smoke and handoff | 5 | 9 | 16 | 9.50 | One package with pinned Chromium. C provides initial contributor instructions. |
| 2 | Team coordination and Member C use case specifications | 3 | 5 | 9 | 5.33 | Two C cases and shared flow conventions. Excludes writing A/B's cases. |
| 3 | Implement, run and diagnose C browser flows | 6 | 11 | 20 | 11.67 | At least ten flows with readback. Estimate assumes a working shared framework. |
| 4 | Two automated usability tasks | 2 | 4 | 7 | 4.17 | Reuse suitable flow fixtures and assertions. |
| 5 | Key-screen scans, finding analysis and one approved fix | 5 | 9 | 16 | 9.50 | Six mapped cases and comparable before/after. Authenticated tooling may need diagnosis. |
| 6 | Access, passive ZAP and dependency audit | 5 | 8 | 14 | 8.50 | Two seeded roles and a locally available advisory feed. Includes source corroboration. |
| 7 | k6 protocol, four runs and comparison | 3 | 5 | 9 | 5.33 | Includes attended setup and interpretation, excluding unattended execution. |
| 8 | Controlled recovery and independent state checks | 4 | 7 | 13 | 7.50 | One isolated write and an agreed failure point/datasource. |
| 9 | C reporting, CI coordination, viva and package checks | 5 | 9 | 16 | 9.50 | Includes ongoing C weekly updates and consolidation. Excludes A/B implementation. |
| Total | Remaining work | 44 | 79 | 144 | 84.00 | Sum of task estimates |

## Owner input and session log

Member A/B response template

```text
Owner / area / work scope:
Estimate date / prospective or retrospective / O–M–P person-hours:
Method, assumptions and original source if supplied:
Actual person-hours and period (or unknown):
Variance and explanation if comparable:
```

Add a row per work session, with one entry per contributor when multiple people work together. Count attended setup, review, debugging and interpretation as human effort. Record unattended build, test, load or training/run duration in elapsed hours separately. Use the student's own records for actuals and report unknown values as unknown.

| Date (Asia/Karachi) | Owner | Checkpoint and task | Attended person-hours | Unattended elapsed hours | Evidence or work note | Change from estimate |
| --- | --- | --- | ---: | ---: | --- | --- |
| Pending entry | | | | | | |

Aggregate these rows weekly and compare approved estimates with actual effort at completion. [Weekly template](../test-management/WEEKLY_PROGRESS_TEMPLATE.md) · [Manual action MA-08](MANUAL_ACTIONS.md).
