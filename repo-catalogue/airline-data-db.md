<!-- CATALOG:PATH="airline-data\db" SLUG="airline-data-db" -->

# Repo Catalogue — airline-data\db

*Slug:* `airline-data-db`  
*Commit:* `b72fda0`  
*Generated:* `2025-11-12T21:29:14Z`

**Summary (2–4 sentences):** This folder historically held local developer database files for the airline data layer (likely lightweight embedded DBs such as SQLite) before a migration to MySQL (per commit message “Migrated to MySQL”). The tracked `.gitignore` shows which ephemeral database artifacts were produced during development (`default.db`, `c-default.db`). No schema or migration artifacts remain here—domain persistence logic has moved elsewhere.

**Contents overview**
- Files: 1  |  Subfolders: 0  |  Languages: gitignore(100%)
- Notable responsibilities:
  - Prevent committing local embedded database files
  - Documents legacy file-based DB naming patterns
  - Serves as a hint of prior persistence approach (embedded → MySQL)

### File entries

#### airline-data/db/.gitignore

```yaml
file: airline-data/db/.gitignore
lang: gitignore
role: "Ignore local embedded database artifacts from version control"
size:
  lines_est: 4
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
  files_read: ["./default.db", "./c-default.db", "./default - Copy.db"]   # implied ignored artifacts
  files_written: ["./default.db", "./c-default.db"]                      # assumed during local runs
config:
  env_vars: []
  config_keys: []
  feature_flags: []
concurrency:
  pattern: "none"
  shared_state: []
  timing: "none"
invariants:
  - "Local database files are never committed"
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
  mapping: "Replace with centralized relational schema migrations & seed scripts in new stack"
  risks: ["Historical data model lost if only present in untracked .db files", "Potential divergence between legacy file DB and current MySQL schema"]
  confidence: "low"
```

## Rollup for airline-data\db

**Key responsibilities (top 5):**
1. Captures legacy embedded DB artifact names to keep repo clean.
2. Signals prior persistence strategy before MySQL migration.
3. Separates transient developer state from committed domain schema.
4. Helps identify missing historical schema definitions.
5. Guides need to locate real current data-model source elsewhere.

**Cross-module dependencies (top 10 by frequency):**
- (None — no code files present)

**High-leverage files to study first (top 10):**
- airline-data/db/.gitignore — Only file; hints at legacy storage approach needing reconstruction.

**Mermaid: high-level dependency sketch (optional, if meaningful)**
```mermaid
graph TD
  A[airline-data\db] --> B[Legacy Embedded DB (untracked *.db)]
  B --> C[Current MySQL schema (elsewhere)]
```