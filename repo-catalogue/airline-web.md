<!-- CATALOG:PATH="airline-web" SLUG="airline-web" -->

# Repo Catalogue — airline-web

*Slug:* `airline-web`  
*Commit:* `6160503`  
*Generated:* `2025-11-13T01:18:15Z`

**Summary (2–4 sentences):** Top-level Play Framework web project directory for the airline UI. Contains build configuration (SBT), IDE metadata, legacy Typesafe Activator launchers, repository licensing/readme, and a few miscellaneous assets. All application code and static assets live under subfolders (app/, conf/, public/, project/, tokens/) which are catalogued separately. Treat this folder as the build/tooling shell around the web app.

**Contents overview**
- Files: 9  |  Subfolders: 5 (app, conf, project, public, tokens)  |  Languages: Config/SBT (~35%), Shell/Batch (~30%), Text/Docs (~20%), Binary (~15%)
- Notable responsibilities:
  - Define Scala/Play build (build.sbt) and IDE/module metadata.
  - Provide legacy Typesafe Activator scripts/jar for local development.
  - Hold legal/documentation files (LICENSE, README).
  - Contain a miscellaneous image (output.png) likely used in documentation/testing.

### File entries

#### airline-web/.gitignore

```yaml
file: airline-web/.gitignore
lang: Config
role: "Ignore patterns for the airline-web subproject"
size:
  lines_est: 20
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
queries: { sql: [], orm_calls: [] }
external_io: {}
config: {}
concurrency:
  pattern: "N/A"
  shared_state: []
  timing: "N/A"
invariants: []
error_handling: {}
security: {}
tests:
  files: []
  coverage_quality: "low"
  golden_seeds: []
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Replace with repo-wide ignore and tool-specific ignore as needed"
  risks: []
  confidence: "high"
```

#### airline-web/LICENSE

```yaml
file: airline-web/LICENSE
lang: Text
role: "License file for airline-web"
size:
  lines_est: 20
  functions_est: 0
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io: {}
config: {}
concurrency: {}
invariants: []
error_handling: {}
security: {}
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Confirm license compatibility for the rewritten stack and any vendored assets"
  risks: ["Unclear scope if multiple modules have differing licenses"]
  confidence: "med"
```

#### airline-web/README

```yaml
file: airline-web/README
lang: Text
role: "Project-level README for the web app (setup/run notes)"
size:
  lines_est: 55
  functions_est: 0
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io: {}
config: {}
concurrency: {}
invariants: []
error_handling: {}
security: {}
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Replace with new README covering modern tooling, dev/prod workflows, and environment config"
  risks: ["Stale instructions (Activator)"]
  confidence: "high"
```

#### airline-web/activator

```yaml
file: airline-web/activator
lang: Shell
role: "Typesafe Activator launcher script for UNIX-like systems"
size:
  lines_est: 300
  functions_est: 0
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io:
  files_read: ["./activator-launch-1.3.6.jar"]
config:
  env_vars: ["JAVA_HOME", "JDK_JAVA_OPTIONS?"]
  config_keys: []
  feature_flags: []
concurrency:
  pattern: "Process spawn of JVM"
  shared_state: []
  timing: "Developer-invoked"
invariants:
  - "Requires compatible JDK"
error_handling:
  expected_errors: ["JRE/JDK missing", "Jar not found"]
  retries_timeouts: "N/A"
security: {}
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files:
  - "airline-web/activator.bat"
rewrite_notes:
  mapping: "Replace with sbt runner or modern build tool (sbt, Gradle, or npm scripts for new stack)"
  risks: ["Obsolete tooling"]
  confidence: "high"
```

#### airline-web/activator-launch-1.3.6.jar

```yaml
file: airline-web/activator-launch-1.3.6.jar
lang: Binary (JAR)
role: "Typesafe Activator bootstrap JAR"
size:
  lines_est: 0
  functions_est: 0
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io: {}
config: {}
concurrency: {}
invariants:
  - "Deprecated; not required for modern Play/sbt"
error_handling: {}
security: {}
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Remove; not needed in the rewrite"
  risks: ["Binary bloat, outdated dependency"]
  confidence: "high"
```

#### airline-web/activator.bat

```yaml
file: airline-web/activator.bat
lang: Batch
role: "Typesafe Activator launcher script for Windows"
size:
  lines_est: 220
  functions_est: 0
  classes_est: 0
public_api: {}
external_io:
  files_read: ["activator-launch-1.3.6.jar"]
config:
  env_vars: ["JAVA_HOME", "JDK_JAVA_OPTIONS?"]
  config_keys: []
  feature_flags: []
concurrency:
  pattern: "Process spawn of JVM"
  shared_state: []
  timing: "Developer-invoked"
invariants:
  - "Requires Windows + JDK in PATH/JAVA_HOME"
error_handling:
  expected_errors: ["JRE/JDK missing", "Jar not found"]
  retries_timeouts: "N/A"
security: {}
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files:
  - "airline-web/activator"
rewrite_notes:
  mapping: "Remove; use sbt or new build scripts"
  risks: ["Obsolete tooling"]
  confidence: "high"
```

#### airline-web/airline-web.iml

```yaml
file: airline-web/airline-web.iml
lang: XML (IDE)
role: "IntelliJ IDEA module definition for airline-web"
size:
  lines_est: 1100
  functions_est: 0
  classes_est: 0
public_api: {}
config:
  env_vars: []
  config_keys: ["IDE module and library mappings"]
  feature_flags: []
concurrency: {}
invariants: []
error_handling: {}
security: {}
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Exclude from repo or regenerate via IDE/import for new stack"
  risks: ["Editor-specific drift"]
  confidence: "high"
```

#### airline-web/build.sbt

```yaml
file: airline-web/build.sbt
lang: SBT (Scala)
role: "Build definition for Play/Scala web project (dependencies, settings)"
size:
  lines_est: 55
  functions_est: 0
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io: {}
config:
  env_vars: []
  config_keys:
    - "scalaVersion"
    - "libraryDependencies (Play framework, JSON, mailer, etc.)"
    - "resolvers"
    - "Play plugin enablement"
  feature_flags: []
concurrency: {}
invariants:
  - "Dependency versions must be compatible with Scala/Play versions"
error_handling: {}
security: {}
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files:
  - "airline-data/build.sbt (if present in repo root)"
rewrite_notes:
  mapping: "Translate dependencies into new stack equivalents (server framework, mail/WS, JSON, websockets); remove Play-specific plugins if not used"
  risks: ["Hidden transitive dependencies; tight coupling to Play"]
  confidence: "med"
```

#### airline-web/output.png

```yaml
file: airline-web/output.png
lang: Image (PNG)
role: "Miscellaneous image (likely sample/output illustration)"
size:
  lines_est: 0
  functions_est: 0
  classes_est: 0
public_api: {}
external_io: {}
config: {}
concurrency: {}
invariants:
  - "Non-critical; safe to remove unless referenced by docs"
error_handling: {}
security: {}
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Remove or move to docs/assets if still relevant"
  risks: []
  confidence: "high"
```

### Subfolders (catalogued separately)
- app — controllers, models, views, websocket actors (see per-subfolder catalogues).
- conf — Play config and routes (not yet catalogued here).
- project — sbt meta-build (not yet catalogued here).
- public — static assets (images, css, js, plugins) (see per-subfolder catalogues).
- tokens — environment/token artifacts (not yet catalogued here).

## Rollup for airline-web

**Key responsibilities (top 5):**
1. Define the build system and developer tooling for the Play-based web app (SBT, Activator).
2. Provide legal and documentation files (LICENSE, README) for the web module.
3. Hold IDE/project metadata to ease local development (IntelliJ .iml).
4. Anchor the subfolder structure where application code, configuration, and assets reside.
5. Include miscellaneous assets referenced by documentation or examples.

**Cross-module dependencies (top 10 by frequency):**
- build.sbt → manages dependencies used by app/controllers, app/websocket, public assets.
- conf/ (routes, application.conf) → wires controller endpoints (to be catalogued separately).
- public/ → served static files consumed by app/views and client JS.
- app/ → main code invoking airline-data DAOs and airline-rest endpoints.
- project/ → sbt plugins/tasks affecting build and run lifecycle.

**High-leverage files to study first (top 10):**
- build.sbt — authoritative list of dependencies and versions; informs rewrite mapping.
- README — reveals original dev workflows and assumptions; replace with modern process.
- activator, activator.bat, activator-launch-1.3.6.jar — confirm deprecation and remove.
- airline-web.iml — IDE hints; can be excluded from VCS in the rewrite.
- conf/ (subfolder) — next to catalogue for routes/config binding controllers to URLs.
- project/ (subfolder) — next to catalogue for sbt meta-build and plugins.
