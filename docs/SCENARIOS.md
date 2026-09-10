# Required scenarios

## 1. Greenfield
Requirement: build a URL shortener with core APIs, analytics, and reliability features.
Demonstrates decomposition, architecture/security branches, approval, implementation, validation, docs, and release gate.

## 2. Brownfield
Requirement: add analytics/reliability without breaking existing APIs.
Adds an explicit impact-analysis agent before architecture and security work. The produced artifact lists affected modules and compatibility constraints.

## 3. Ambiguous
Requirement: "Make the URL shortener secure and fast."
RequirementAgent detects ambiguous language and emits clarification questions. The DAG introduces a human-approved CLARIFY task before implementation. Demo assumptions are recorded in decision lineage.
