# PetClinic test plan

**Revision 0.3 · 2026-11-28 · Proposed for team review.** Member C maintains this draft. Local history contains Member A's CFG, mutation notes and existing CI. No canonical plan, risk register, release criteria, traceability matrix, defect log or weekly report was found in the available local refs. A and B should supply any canonical material before the team adopts this path. Link existing team artifacts in the sections below as they become available. [Member C handover](../member-c/HANDOVER.md).

## Objectives, scope and test basis

Evaluate the reliability of the Owners/Pets module on the local secured Spring PetClinic REST backend and a pinned upstream Angular client, with synthetic records. W1 adds a Pet to an existing Owner. W2 books a Visit for that Pet. Verify success in the UI and independently verify the stored data and associations. Record initial state, user action, feedback, stored outcome and scope limits per flow. In-scope support includes owner and pet reads, visit readback, seeded roles, data validation, repeated requests, and a source-grounded recovery fault. Out of scope are unrelated modules, production or external systems, active attacks and human usability studies. Vet directory reads may serve as a local authorization control.

The test basis is the seven-page SE3002 brief, versioned backend code/config and OpenAPI, exact frontend revision, secured observed behavior and approved justified assumptions. Approve at least eight requirements with at least three per workflow and four business rules including a boundary, a compound Boolean rule and a state transition. Confirm Owner–Pet association, missing/mismatched identifiers and duplicate/retry expectations. Use Member B's traceability matrix once available and retain revision, requirement/rule, case/flow, test level and evidence links. Explicitly record discrepancies between the source, observed behavior and approved expectation. [Checklist](../member-c/REQUIREMENTS_CHECKLIST.md) · [Source audit](../member-c/REPOSITORY_AUDIT.md).

## Approach and risk-based test depth

| Level or activity | Planned method and owners |
| --- | --- |
| Static, unit and mutation | A interprets Sonar, compares a hand-calculated CFG and applies rule coverage, 100% server-side statement/branch coverage with exceptions justified, full MCC and derived MC/DC for the critical module, and PIT/custom operators and survivor analysis. |
| API and integration | B aligns versioned OpenAPI, generates EP and 3-point BVA data per input, demonstrates regeneration after two constraint changes and exercises decision tables, valid/invalid transitions, duplicate behavior and cross-workflow readback. Agree with B on coverage dimensions and targets. Use one invalid class per case and applicable below/at/above inputs for each limit. |
| Browser and usability | Each member specifies and automates two distinct use cases, minimum five flows each. C provides shared Playwright/Chromium setup. Test confirmations and independent postconditions for each flow and retain actual failures. C verifies error correction and keyboard navigation/submission within the two task tests. Reconcile unique executable cases per test level. |
| Accessibility and security | C covers all six key-screen mappings with authenticated Lighthouse and full axe scans and a matched before/after comparison for one approved fix. C runs local Playwright access checks, passive ZAP during W1/W2, reviews at least two controls and audits dependencies. |
| Performance and recovery | C predeclares workload limits, uses four comparable k6 runs and compares results. A and C coordinate an isolated controlled write failure and exactly-once final state proof. |
| CI | A coordinates integration with B/C. PR CI is to run all required test levels, Sonar and mutation with explicit nonempty selections and enforce agreed thresholds. Capture genuine failed/fixed and blocked PR evidence. Preserve the configured gates. |

A leads the canonical risk register and 5 × 5 likelihood-by-impact method (1–5 each), with at least eight product and two project risks. Team review will set risk IDs, scores and risk-level cutoffs. Pending that review, examine wrong Owner/Pet/Visit associations, unauthorized writes, partial/duplicate writes, rejected input changing state and unconfirmed workflow feedback as product risk candidates. Project risk candidates include external frontend/tool access, dependency on the late test basis and CI integration delays. Treat these as prompts for A's scoring. For approved highest risks, require cross-level success/rejection cases, relevant boundaries or role checks, independent state readback and regression evidence. Medium risk gets representative valid/invalid and boundary cases where applicable. Lower risk gets smoke and relevant regression. Required assignment coverage applies at all risk levels. Canonical risk link and highest-risk IDs are pending A. [Decision and dependencies](../member-c/DECISIONS.md).

## Roles, environment and test data

Eiman owns Area 1 and leads risk/exit criteria and CI. Hamza owns Area 2 and leads traceability, defects, RCA and Pareto. Huzaima owns Area 3 and leads the plan, weekly reporting, completion and the shared browser setup. A, B and C each own two browser use cases and supply their own evidence and effort. Team agreement and student approvals govern shared changes and publication.

Use Java 21 and the existing Maven wrapper, secured backend on loopback port 9966 with `/petclinic/`, separately pinned Angular and supported Node/npm. Use H2 for proposed repeatable browser/API runs. Existing Java tests use HSQLDB. Preserve separate account contexts and verify seed/reset before dependent tests. Document an approved PostgreSQL durability demonstration if required. Use seeded synthetic owners/types plus unique fixtures, pass W1's actual Pet ID into W2, reread outside the operation, and reset via verified seed restart or isolation. Serialize shared writes or provision per-worker instances. Record identical starting data and environment per measured performance run. [Environment and test data](../member-c/ENVIRONMENT.md).

## Schedule, entry, suspension and resumption

| Gate | Planned milestone | Entry and milestone evidence |
| --- | --- | --- |
| 0.3 | Review planning, management and effort baseline | Resolve local Git provenance and record ownership, assumptions and open risks. Draft is available for review. |
| 1.1–1.3 | Pin frontend, prove secured pairing, publish shared Playwright to A/B | Resolve 0.3 and authorize Phase 1. Verify lock, authenticated runtime, seed/reset and real smoke. Agree on shared package before 1.3. |
| 2–3 | Agree test basis and six cases, execute browser suites | Agree requirements/rules, oracles and ownership. Use the qualified shared package. Preserve visible and persisted results. |
| 4–6 | Usability, accessibility and defensive security | Verify routes, data, authentication and role conditions. Keep original reports and compare the chosen accessibility fix. |
| 7–8 | Performance and recovery | Predeclare workload thresholds and comparable fixtures. Agree isolated recovery profile and failure point with A. |
| 9 | CI, completion, viva and package | Integrate actual results and enforce agreed gates. Verify report, evidence, releases and clean package reproduction. |

Each checkpoint requires current authorization, known revisions, approved scope and applicable fixtures. Suspend affected tests for incorrect target/auth/profile, compromised isolation, unavailable data, broken environment, unresolved oracle or lost evidence. Record the trigger, affected scope and evidence. Resume after the owner verifies contained scope, restored inputs and seed/reset, relevant smoke or failure-point check, approval where needed and a recorded new execution plan. Other independent work may continue within its authorized scope. Calendar dates and weekly cadence start when the team approves its first reporting period.

## Draft measurable exit criteria

These are proposed release thresholds. Actual results are pending. A and C will align at least two criteria with confirmed highest product risk IDs in A's canonical register before approving the plan. The first two rows are candidate mappings. Review the thresholds and publish the criterion revision before dependent execution.

| Draft criterion | Proposed target and measure | Candidate risk link | Evidence |
| --- | --- | --- | --- |
| W1/W2 persisted integrity | 100% of approved critical W1/W2 flow cases must execute and pass the visible and applicable persistence assertions. Zero unexpected association, partial or duplicate writes in those cases. | Wrong association/partial or duplicate writes. Highest-risk ID pending A. | E-UC-03, E-UC-04, E-REC-01 and B's API readback |
| Access control | 100% of approved access matrix decisions observed as expected, at least four across two roles including allow and deny. Zero persisted writes in tested denied write cases if selected by the approved matrix. | Unauthorized workflow access/write. Highest-risk ID pending A. | E-SEC-01 and server-side corroboration |
| Performance | Four valid measured runs with at least 60 seconds at target in each. All four meet their respective predeclared p95 and unexpected failure limits. Record p95 ms, completed req/s and unexpected failure %. Numeric limits and release adoption are pending Phase 7 protocol approval. | Slow or unreliable workflow operation. Risk ID pending A. | E-PERF-01 through E-PERF-05 |

Complete assignment-required evidence and check active CI and triage outcomes as supporting release inputs. Report actuals, tested/untested scope, deviations, open defects and residual risk per risk, then recommend release or hold from the agreed criteria.

## Defects, deliverables and reporting

Supply reproduction context, expected versus observed outcome, sanitized original evidence, related requirement/risk and a severity/priority proposal to B. B leads deduplication, triage, approval of GitHub Issue activity and lifecycle tracking. Record owner, severity, priority, status, triage decision, retest outcome and reopen history. Use 5 Whys and fishbone with corrective/preventive actions on at least two genuine major defects, and Pareto by category. Canonical defect log, RCA and issue links are pending B.

C updates the plan and reports weekly using actual tests, risks, defects, effort, GQM and owner/date control actions. A/B validate their source and runtime results. Revisit the plan on scope or risk changes and record date, owner and approval. Archive claims with reproducible scripts, exact revisions/tool versions, safe original reports and actual CI links. Review submission and AI-use evidence before final packaging. [GQM](GQM_METRICS.md) · [Weekly report](WEEKLY_PROGRESS_TEMPLATE.md) · [Evidence register](../member-c/EVIDENCE_REGISTER.md) · [Effort log](../member-c/EFFORT_LOG.md).

## Approval and open decisions

Team approval is pending for this plan, its shared path, test basis, six-case split, risk ranking and exit targets, traceability/defect sources, storage proof and shared suite location. Confirm frontend approval and tool access before acquisition. Resolve C's publication process at the 0.3 review boundary. Thereafter test-basis, final risk thresholds and A/B runtime reports can be obtained in parallel with authorized independent environment work. [Dependencies and response template](../member-c/MANUAL_ACTIONS.md).
