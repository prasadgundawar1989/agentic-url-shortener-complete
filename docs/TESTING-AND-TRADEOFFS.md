# Testing, risks, limitations, trade-offs

## Testing
- Unit test for URL generation.
- Workflow approval-gate test.
- Bounded retry recovery test.
- Dependency-cycle rejection test.
- Spring application context smoke test.
- Manual API walkthrough in README.

## Risks and controls
- Invalid/unsafe input -> scheme validation and Bean Validation.
- Uncontrolled agent action -> human gates + PolicyGuard.
- Infinite retry -> bounded retry count.
- Partial workflow failure -> safe-stop + compensating rollback hooks.
- Lost reasoning context -> ExecutionContext + decision map + audit events.
- Upstream change -> re-plan only the impacted subgraph.

## Limitations
This prototype intentionally uses deterministic in-process agents rather than requiring a paid/external LLM API. That keeps the submission reproducible. `Agent` is an abstraction point where an LLM-backed implementation can later be plugged in.

The workflow engine is stateful in memory for interview/demo simplicity. A production system would persist workflow/task state, use a durable queue/workflow platform, distribute locks, authenticate approval endpoints, and store audit records durably.

Independent ready DAG nodes execute concurrently using Java 21 virtual threads and synchronize before the next dependency level. A production version would move this execution to durable distributed workers.

Fallback is deliberately conservative: when the retry budget is exhausted and no explicitly safe fallback exists, the engine records that fact and safe-stops rather than inventing a potentially unsafe fallback. This demonstrates the governance principle that fallback behavior must be predefined and bounded.
