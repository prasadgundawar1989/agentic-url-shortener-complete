# Agentic Software Engineering System — URL Shortener

A complete, runnable Java/Spring Boot prototype for the interview assignment. It combines a working URL shortener with an agentic SDLC orchestration layer.

## Requirements
- IntelliJ IDEA
- JDK 21
- Maven support (IntelliJ includes Maven integration)

## Fastest IntelliJ run — no Docker required
1. Unzip the project.
2. IntelliJ -> **File -> Open** -> select the project folder containing `pom.xml`.
3. Select **JDK 21** when IntelliJ asks for the Project SDK.
4. Allow Maven to import dependencies.
5. Run `UrlShortenerApplication`.
6. Verify: `GET http://localhost:8080/actuator/health`

The default profile uses an in-memory H2 database, so the application is immediately runnable.

## Optional PostgreSQL run
1. `docker compose up -d`
2. In IntelliJ Run Configuration, add program argument: `--spring.profiles.active=postgres`
3. Run `UrlShortenerApplication`.

## URL Shortener APIs
### Create
```bash
curl -X POST http://localhost:8080/api/v1/urls \
  -H 'Content-Type: application/json' \
  -d '{"url":"https://example.com","expirationMinutes":60}'
```

### Redirect
```bash
curl -i http://localhost:8080/{shortCode}
```

### Analytics
```bash
curl http://localhost:8080/api/v1/urls/{shortCode}/analytics
```

### Delete
```bash
curl -X DELETE http://localhost:8080/api/v1/urls/{shortCode}
```

## Agentic scenario APIs
### Greenfield
```bash
curl -X POST http://localhost:8080/api/v1/workflows/scenarios/greenfield
```
The workflow will stop at `IMPL` with `WAITING_FOR_APPROVAL`.

Approve implementation:
```bash
curl -X POST http://localhost:8080/api/v1/workflows/{workflowId}/approve/IMPL
```
Later approve release:
```bash
curl -X POST http://localhost:8080/api/v1/workflows/{workflowId}/approve/RELEASE
```

Inspect the full state, artifacts, task statuses, decisions, and audit trail:
```bash
curl http://localhost:8080/api/v1/workflows/{workflowId}
```

### Brownfield
```bash
curl -X POST http://localhost:8080/api/v1/workflows/scenarios/brownfield
```

### Ambiguous requirement
```bash
curl -X POST http://localhost:8080/api/v1/workflows/scenarios/ambiguous
```
This scenario adds a `CLARIFY` human approval gate before implementation.

Approve it:
```bash
curl -X POST http://localhost:8080/api/v1/workflows/{workflowId}/approve/CLARIFY
```
Then approve `IMPL`, and finally `RELEASE`.

## Dynamic re-planning
```bash
curl -X POST http://localhost:8080/api/v1/workflows/{workflowId}/replan \
  -H 'Content-Type: application/json' \
  -d '{"requirement":"Add authenticated analytics with p95 latency below 100ms","fromTask":"REQ"}'
```
This resets the changed node and downstream tasks while preserving workflow identity and audit history.

## Observability
- Health: `/actuator/health`
- Metrics: `/actuator/metrics`
- Prometheus: `/actuator/prometheus`
- Agentic metrics include workflow completions/failures, active workflows, retry count, rollback count, and latency.

## Project structure
```text
src/main/java/com/example/urlshortener
├── agents/          deterministic SDLC agents
├── controller/      URL + workflow REST APIs
├── dto/
├── exception/
├── model/
├── orchestrator/    DAG engine, gates, policy, audit, metrics, re-plan
├── repository/
└── service/

docs/
├── ARCHITECTURE.md
├── SCENARIOS.md
└── TESTING-AND-TRADEOFFS.md
```

## How to explain this in the interview
The key design choice is that this is **not a fixed Agent A -> Agent B -> Agent C chain**. Each task is a graph node with dependencies, state, retry budget, approval policy, and audit events. The engine executes only nodes whose entry dependencies are satisfied, stops at human gates, carries artifacts/decisions across stages, can invalidate a downstream subgraph when an upstream requirement changes, and safe-stops when it cannot proceed.

The agents are deterministic for reproducibility. The `Agent` interface is deliberately separated from orchestration, so an LLM/Copilot/Claude-backed adapter can be added without changing governance and lifecycle behavior.

See `docs/` for the architecture, required scenario mapping, testing approach, risks, limitations, and trade-offs.

## Retry + rollback demonstration
Start a greenfield workflow with a controlled test failure:
```bash
curl -X POST http://localhost:8080/api/v1/workflows/scenarios/greenfield \
  -H 'Content-Type: application/json' \
  -d '{"requirement":"Build the URL service SIMULATE_TEST_FAILURE"}'
```
Approve `IMPL`. The TEST node then exhausts its bounded retry budget; the engine fails safely and runs the configured compensating rollback hook for the completed implementation node. Inspect the audit trail with `GET /api/v1/workflows/{workflowId}`.
