# Architecture

## Components
1. **URL Shortener API** — create, redirect, analytics, delete.
2. **Persistence** — H2 by default; PostgreSQL profile for production-like execution.
3. **Scenario Factory** — creates greenfield, brownfield, and ambiguous workflows.
4. **Agentic Workflow Engine** — explicit DAG, dependency gates, bounded retries, approval checkpoints, rollback hooks, safe-stop, re-planning.
5. **Execution Context** — shared cross-stage artifacts and decision lineage.
6. **Policy Guard** — prevents unapproved high-impact implementation/release tasks.
7. **Audit Trail** — timestamped workflow/task events.
8. **Metrics** — completed/failed workflows, retries, rollbacks, latency, active workflows.

## Control flow
Requirement -> (Brownfield impact analysis when applicable) -> Architecture + Security -> Human-approved Implementation -> Test -> Validation -> Documentation -> Human-approved Release.

For the ambiguous scenario, an additional human clarification checkpoint is inserted after requirement analysis.

## Controlled autonomy
Agents can generate and validate engineering outputs, but the engine stops at defined high-impact gates. Humans approve implementation/release decisions. Failures consume a bounded retry budget and then stop safely; completed high-impact actions expose rollback hooks.

## Dynamic re-planning
`POST /api/v1/workflows/{id}/replan` changes the requirement and invalidates the chosen task plus all downstream descendants, preserving unrelated upstream decisions.
