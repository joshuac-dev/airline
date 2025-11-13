<!-- CATALOG:PATH="airline-data\project" SLUG="airline-data-project" -->

# Repo Catalogue — airline-data\project

*Slug:* `airline-data-project`  
*Commit:* `6160503`  
*Generated:* `2025-11-12T21:31:23Z`

**Summary (2–4 sentences):** This folder contains the SBT build definition scaffolding for the `airline-data` module: build tool version pinning, plugin declarations (currently all commented out), and ignore rules for build outputs. It governs how the data layer code (elsewhere) was compiled and packaged but holds no domain logic itself. The presence of only commented plugin lines suggests a stripped-down or transitional build setup.

**Contents overview**
- Files: 4  |  Subfolders: 1  |  Languages: gitignore(~50%), sbt/scala(~25%), properties(~25%)
- Notable responsibilities:
  - Pin SBT version (1.9.9) for reproducible builds
  - (Historically) Manage IDE (Eclipse) plugin integration (now disabled)
  - Enforce ignoring of build artifacts (`target/`)
  - Provide an inner project meta-build structure (`project/project`)

### File entries

#### airline-data/project/.gitignore

```yaml
file: airline-data/project/.gitignore
lang: gitignore
role: "Exclude SBT target build artifacts from version control"
size:
  lines_est: 2
  functions_est: 0
  classes_est: 0
public_api:
  routes: []
  exports: []
data_model:
  tables_read: []
  tables_written: []
  migrations: []
  entities: []
queries:
  sql: []
  orm_calls: []
external_io:
  http_calls: []
  message_queues: []
  files_read: ["/target/"]          # implicit pattern
  files_written: ["/target/"]       # build output
config:
  env_vars: []
  config_keys: []
  feature_flags: []
concurrency:
  pattern: "none"
  shared_state: []
  timing: "none"
invariants:
  - "Compiled artifacts under target/ are never committed"
error_handling:
  expected_errors: []
  retries_timeouts: "none"
security:
  authz: "none"
  input_validation: "none"
  sensitive_ops: []
tests:
  files: []
  coverage_quality: "low"
  golden_seeds: []
similar_or_duplicate_files:
  - "airline-data/project/project/.gitignore"
rewrite_notes:
  mapping: "Replicate artifact ignore in new build system (.gitignore or build tool config)"
  risks: ["Duplicate ignore file may cause divergent maintenance"]
  confidence: "high"
```

#### airline-data/project/build.properties

```yaml
file: airline-data/project/build.properties
lang: properties
role: "Pins SBT version for the module meta-build"
size:
  lines_est: 2
  functions_est: 0
  classes_est: 0
public_api:
  routes: []
  exports: []
data_model:
  tables_read: []
  tables_written: []
  migrations: []
  entities: []
queries:
  sql: []
  orm_calls: []
external_io:
  http_calls: []
  message_queues: []
  files_read: []
  files_written: []
config:
  env_vars: []
  config_keys: ["sbt.version=1.9.9"]
  feature_flags: []
concurrency:
  pattern: "none"
  shared_state: []
  timing: "none"
invariants:
  - "Build uses SBT 1.9.9 consistently"
error_handling:
  expected_errors: []
  retries_timeouts: "none"
security:
  authz: "none"
  input_validation: "none"
  sensitive_ops: []
tests:
  files: []
  coverage_quality: "low"
  golden_seeds: []
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Map to new build tool version pin (e.g., Gradle wrapper / PNPM / Maven)"
  risks: ["Outdated SBT version relative to latest Scala ecosystem"]
  confidence: "high"
```

#### airline-data/project/plugins.sbt

```yaml
file: airline-data/project/plugins.sbt
lang: sbt
role: "Defines (currently commented) SBT plugins for the data module"
size:
  lines_est: 2
  functions_est: 0
  classes_est: 0
public_api:
  routes: []
  exports: []
data_model:
  tables_read: []
  tables_written: []
  migrations: []
  entities: []
queries:
  sql: []
  orm_calls: []
external_io:
  http_calls: []
  message_queues: []
  files_read: []
  files_written: []
config:
  env_vars: []
  config_keys: []
  feature_flags: []
concurrency:
  pattern: "none"
  shared_state: []
  timing: "none"
invariants:
  - "No active plugins; build behavior minimal"
error_handling:
  expected_errors: []
  retries_timeouts: "none"
security:
  authz: "none"
  input_validation: "none"
  sensitive_ops: []
tests:
  files: []
  coverage_quality: "low"
  golden_seeds: []
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Decide required build plugins (code formatting, dependency mgmt, packaging) in new stack"
  risks: ["Missing plugin-driven tasks (codegen, lint) may have existed historically", "Commented line indicates prior IDE integration"]
  confidence: "medium"
```

#### airline-data/project/project/.gitignore

```yaml
file: airline-data/project/project/.gitignore
lang: gitignore
role: "Meta-build ignore for nested project structure (SBT uses project/ for meta-build)"
size:
  lines_est: 2
  functions_est: 0
  classes_est: 0
public_api:
  routes: []
  exports: []
data_model:
  tables_read: []
  tables_written: []
  migrations: []
  entities: []
queries:
  sql: []
  orm_calls: []
external_io:
  http_calls: []
  message_queues: []
  files_read: ["/target/"]
  files_written: ["/target/"]
config:
  env_vars: []
  config_keys: []
  feature_flags: []
concurrency:
  pattern: "none"
  shared_state: []
  timing: "none"
invariants:
  - "Meta-build artifacts under nested target/ not committed"
error_handling:
  expected_errors: []
  retries_timeouts: "none"
security:
  authz: "none"
  input_validation: "none"
  sensitive_ops: []
tests:
  files: []
  coverage_quality: "low"
  golden_seeds: []
similar_or_duplicate_files:
  - "airline-data/project/.gitignore"
rewrite_notes:
  mapping: "Single top-level ignore may suffice in new build; simplify structure"
  risks: ["Redundant ignore duplication can hide differences if expanded later"]
  confidence: "high"
```

## Rollup for airline-data\project

**Key responsibilities (top 5):**
1. Define and pin SBT version for the `airline-data` module.
2. (Historically) Manage build plugins—currently none active.
3. Enforce ignoring of build outputs to keep repo clean.
4. Provide meta-build structure (`project/project`) consistent with SBT conventions.
5. Serve as an indicator of minimal or transitional build configuration phase.

**Cross-module dependencies (top 10 by frequency):**
- (None explicit; build layer only references SBT tooling)

**High-leverage files to study first (top 10):**
- airline-data/project/plugins.sbt — Decide future plugin/tooling equivalents.
- airline-data/project/build.properties — Assess upgrade path / alignment with new build tool.
- airline-data/project/.gitignore — Confirm artifact patterns for new stack.
- airline-data/project/project/.gitignore — Consider consolidation (redundancy).
- (Remaining slots unused — no additional domain-impacting files)

**Mermaid: high-level dependency sketch (optional, if meaningful)**
```mermaid
graph TD
  A[airline-data\project] --> B[SBT Meta-Build]
  B --> C[airline-data Source (elsewhere)]
```