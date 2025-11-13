<!-- CATALOG:PATH="airline-rest" SLUG="airline-rest" -->

# Repo Catalogue — airline-rest

*Slug:* `airline-rest`  
*Commit:* `6160503`  
*Generated:* `2025-11-13T00:24:32Z`

Important note about listing completeness:
- The directory listing was retrieved via the GitHub Contents API, which pages results. Some items might not appear here due to pagination limits. For a complete view, browse the folder in GitHub: https://github.com/joshuac-dev/airline/tree/master/airline-rest

**Summary (2–4 sentences):** Play Framework–based REST API exposing airline simulation operations (links, airlines, airplanes) to clients. Controllers translate HTTP requests into calls to the data/simulation layers in airline-data, serialize domain results to JSON, and enforce basic request shaping and validation. Configuration (application.conf) defines server, routing, and environment settings; routes file binds URL patterns to controller actions. This module should be rewritten as a modern HTTP service with explicit contracts, typed schemas, and auth in front of the simulation/data services.

**Contents overview**
- Files: 17  |  Subfolders: 4 (app, app/controllers, conf, project)  |  Languages: Scala (~60%), HOCON (~8%), SBT (~6%), Shell/Batch (~10%), Text/Markdown (~8%), Binary (~8%)
- Notable responsibilities:
  - Define HTTP endpoints for airline, airplane, and link operations.
  - Validate inputs, marshal/unmarshal JSON, map to data-layer calls.
  - Configure Play app (ports, filters, possible DB connection params).
  - Provide build metadata and Play Activator tooling (legacy).
  - Declare public routing table.

### File entries

#### airline-rest/.gitignore
```yaml
file: airline-rest/.gitignore
lang: text
role: "Ignore patterns for this Play subproject"
size:
  lines_est: 10
  functions_est: 0
  classes_est: 0
public_api: {routes: [], exports: []}
data_model: {tables_read: [], tables_written: [], migrations: [], entities: []}
queries: {sql: [], orm_calls: []}
external_io: {}
config: {}
concurrency: {pattern: "N/A", shared_state: [], timing: "N/A"}
invariants: []
error_handling: {}
security: {}
tests: {files: [], coverage_quality: "low", golden_seeds: []}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Replicate ignore rules in new build"
  risks: []
  confidence: "high"
```

#### airline-rest/LICENSE
```yaml
file: airline-rest/LICENSE
lang: text
role: "Module license"
size:
  lines_est: 40
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
tests: {}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Preserve licensing in rewritten service"
  risks: []
  confidence: "high"
```

#### airline-rest/README.md
```yaml
file: airline-rest/README.md
lang: Markdown
role: "Basic run/build instructions for this REST service"
size:
  lines_est: 12
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
tests: {}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Update with new service’s usage and endpoints"
  risks: ["Outdated Play-specific guidance"]
  confidence: "high"
```

#### airline-rest/activator
```yaml
file: airline-rest/activator
lang: Shell
role: "Play Activator wrapper script (legacy tooling)"
size:
  lines_est: 300
  functions_est: 0
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io: {files_read: [], files_written: []}
config: {env_vars: ["JAVA_HOME?","SBT_OPTS?"], config_keys: [], feature_flags: []}
concurrency: {}
invariants: []
error_handling: {}
security: {}
tests: {}
similar_or_duplicate_files:
  - "airline-rest/activator.bat"
  - "airline-rest/activator-launch-1.3.6.jar"
rewrite_notes:
  mapping: "Replace with modern build/run (e.g., sbt/Gradle CLI, containerized)"
  risks: ["Legacy dependency management"]
  confidence: "high"
```

#### airline-rest/activator-launch-1.3.6.jar
```yaml
file: airline-rest/activator-launch-1.3.6.jar
lang: Binary (JAR)
role: "Play Activator launcher (binary)"
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
invariants: []
error_handling: {}
security: {}
tests: {}
similar_or_duplicate_files:
  - "airline-rest/activator"
  - "airline-rest/activator.bat"
rewrite_notes:
  mapping: "Do not carry forward; not needed in rewrite"
  risks: ["Binary in repo; ignore in future"]
  confidence: "high"
```

#### airline-rest/activator.bat
```yaml
file: airline-rest/activator.bat
lang: Batch
role: "Windows wrapper for Play Activator"
size:
  lines_est: 240
  functions_est: 0
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io: {}
config: {env_vars: ["JAVA_HOME?"], config_keys: [], feature_flags: []}
concurrency: {}
invariants: []
error_handling: {}
security: {}
tests: {}
similar_or_duplicate_files:
  - "airline-rest/activator"
rewrite_notes:
  mapping: "Drop in favor of unified dev workflow"
  risks: []
  confidence: "high"
```

#### airline-rest/build.sbt
```yaml
file: airline-rest/build.sbt
lang: SBT
role: "Build definition for Play REST service"
size:
  lines_est: 12
  functions_est: 0
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io: {}
config:
  env_vars: []
  config_keys: ["scalaVersion","libraryDependencies","enablePlugins(PlayScala)"]
  feature_flags: []
concurrency: {}
invariants: []
error_handling: {}
security: {}
tests: {}
similar_or_duplicate_files:
  - "airline-data/build.sbt"
rewrite_notes:
  mapping: "Translate to modern toolchain; pin dependencies and plugins"
  risks: ["Outdated Play/Scala versions"]
  confidence: "high"
```

#### airline-rest/app/controllers/AirlineApplication.scala
```yaml
file: airline-rest/app/controllers/AirlineApplication.scala
lang: Scala
role: "Controller for airline-related endpoints (profiles, info, notices)"
size:
  lines_est: 190
  functions_est: 12
  classes_est: 1
public_api:
  routes:
    - method: GET
      path: "/airlines/:id"
      summary: "Fetch airline by id"
    - method: POST
      path: "/airlines/:id"
      summary: "Update airline info/settings"
  exports:
    - name: AirlineApplication
      kind: class
      summary: "Play controller wiring JSON in/out for airline ops"
data_model:
  tables_read: []
  tables_written: []
  migrations: []
  entities: []
queries:
  sql: []
  orm_calls:
    - "AirlineSource.load(...) — load airline details"
    - "AirlineSource.updateAirlineInfo(...) — persist KPIs/settings"
external_io:
  http_calls: []
  message_queues: []
  files_read: []
  files_written: []
config:
  env_vars: []
  config_keys: ["play.http.parser.maxMemoryBuffer?"]
  feature_flags: []
concurrency:
  pattern: "Play request-per-thread (stateless controller actions)"
  shared_state: ["util caches (AirlineCache) accessed indirectly"]
  timing: "on HTTP request"
invariants:
  - "Airline id must exist"
  - "Input JSON schema valid"
error_handling:
  expected_errors: ["404 not found","400 invalid json"]
  retries_timeouts: "none"
security:
  authz: "likely none or simple token (enforce upstream in rewrite)"
  input_validation: "JSON body and path params"
  sensitive_ops: ["Airline info updates"]
tests:
  files: []
  coverage_quality: "low"
  golden_seeds: []
similar_or_duplicate_files:
  - "app/controllers/Application.scala"
rewrite_notes:
  mapping: "Define OpenAPI/JSON schema; implement RBAC/JWT; add DTO mappers"
  risks: ["Business logic leakage into controller"]
  confidence: "med"
```

#### airline-rest/app/controllers/AirplaneApplication.scala
```yaml
file: airline-rest/app/controllers/AirplaneApplication.scala
lang: Scala
role: "Controller for airplane models and fleet endpoints"
size:
  lines_est: 155
  functions_est: 10
  classes_est: 1
public_api:
  routes:
    - method: GET
      path: "/airplane-models"
      summary: "List airplane models/specs"
    - method: GET
      path: "/airlines/:id/airplanes"
      summary: "List airline fleet"
  exports:
    - name: AirplaneApplication
      kind: class
      summary: "Endpoints to query models and fleet state"
data_model:
  tables_read: []
  tables_written: []
  migrations: []
  entities: []
queries:
  sql: []
  orm_calls:
    - "ModelSource.loadAll() — airplane models"
    - "AirplaneSource.loadByAirline(...) — fleet details"
external_io: {}
config: {env_vars: [], config_keys: [], feature_flags: []}
concurrency:
  pattern: "Stateless per-request"
  shared_state: ["AirplaneModelCache"]
  timing: "on HTTP request"
invariants:
  - "IDs numeric"
error_handling:
  expected_errors: ["404 airline not found"]
  retries_timeouts: "none"
security:
  authz: "ownership required in rewrite for mutating actions"
  input_validation: "path params"
  sensitive_ops: ["None (read endpoints)"]
tests: []
coverage_quality: "low"
golden_seeds: []
similar_or_duplicate_files:
  - "airline-data/src/main/scala/com/patson/data/airplane/ModelSource.scala"
rewrite_notes:
  mapping: "Contract-first endpoints; pagination/caching"
  risks: ["Direct exposure of internal model fields"]
  confidence: "med"
```

#### airline-rest/app/controllers/Application.scala
```yaml
file: airline-rest/app/controllers/Application.scala
lang: Scala
role: "General application endpoints (health, search, misc lookups)"
size:
  lines_est: 180
  functions_est: 12
  classes_est: 1
public_api:
  routes:
    - method: GET
      path: "/"
      summary: "Health or landing endpoint"
    - method: GET
      path: "/search"
      summary: "Generic search (airports/airlines)"
  exports:
    - name: Application
      kind: class
      summary: "Misc endpoints consolidating cross-domain queries"
data_model:
  tables_read: []
  tables_written: []
  migrations: []
  entities: []
queries:
  sql: []
  orm_calls:
    - "AirportSource.search(...)"
    - "AirlineSource.search(...)"
external_io: {}
config: {env_vars: [], config_keys: ["filters.cors?","play.filters.enabled?"], feature_flags: []}
concurrency:
  pattern: "Stateless per-request"
  shared_state: []
  timing: "on HTTP request"
invariants:
  - "Query params validated"
error_handling:
  expected_errors: ["400 invalid query"]
  retries_timeouts: "none"
security:
  authz: "none (health) / limited for search"
  input_validation: "query params"
  sensitive_ops: []
tests: []
coverage_quality: "low"
golden_seeds: []
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Split health vs search into distinct controllers; add rate limits"
  risks: ["God controller doing too much"]
  confidence: "low"
```

#### airline-rest/app/controllers/LinkApplication.scala
```yaml
file: airline-rest/app/controllers/LinkApplication.scala
lang: Scala
role: "Controller for link (route) CRUD, pricing, stats, and history"
size:
  lines_est: 620
  functions_est: 40
  classes_est: 1
public_api:
  routes:
    - method: GET
      path: "/links/:id"
      summary: "Fetch link details and current pricing"
    - method: POST
      path: "/links"
      summary: "Create a new link (route) with schedule/pricing"
    - method: PUT
      path: "/links/:id"
      summary: "Update link frequency/pricing"
    - method: DELETE
      path: "/links/:id"
      summary: "Delete a link"
    - method: GET
      path: "/links/:id/stats"
      summary: "Retrieve performance stats/history"
  exports:
    - name: LinkApplication
      kind: class
      summary: "Main surface for route operations"
data_model:
  tables_read: []
  tables_written: []
  migrations: []
  entities: []
queries:
  sql: []
  orm_calls:
    - "LinkSource.load/insert/update/delete(...) — link persistence"
    - "LinkStatisticsSource.loadByLink(...) — stats"
    - "ConsumptionHistorySource.loadByLink(...) — histories"
external_io: {}
config:
  env_vars: []
  config_keys: ["pagination.defaultLimit?","pricing.bounds? (validated upstream)"]
  feature_flags: []
concurrency:
  pattern: "Stateless per-request; interacts with caches & DAOs"
  shared_state: ["AirportCache","AirlineCache","AirplaneOwnershipCache"]
  timing: "on HTTP request"
invariants:
  - "Frequency and seat capacity must be feasible"
  - "Price ≥ 0; bookings ≤ seats enforced by engine"
error_handling:
  expected_errors: ["409 conflict on constraints","400 invalid payload","404 not found"]
  retries_timeouts: "none"
security:
  authz: "airline ownership required for writes (add in rewrite)"
  input_validation: "payload schema & path params"
  sensitive_ops: ["Economic state mutation via writes"]
tests:
  files: []
  coverage_quality: "low"
  golden_seeds: []
similar_or_duplicate_files:
  - "airline-data/src/main/scala/com/patson/LinkSimulation.scala"
rewrite_notes:
  mapping: "Separate command (write) endpoints vs query endpoints; add optimistic locking"
  risks: ["Controller performing business logic; lack of auth/audit"]
  confidence: "med"
```

#### airline-rest/app/controllers/package.scala
```yaml
file: airline-rest/app/controllers/package.scala
lang: Scala
role: "Shared controller utilities: JSON formats, helpers, constants"
size:
  lines_est: 55
  functions_est: 4
  classes_est: 0
public_api:
  routes: []
  exports:
    - name: JsonFormats
      kind: object
      summary: "Implicit Reads/Writes for domain types"
data_model: {tables_read: [], tables_written: [], migrations: [], entities: []}
queries: {sql: [], orm_calls: []}
external_io: {}
config: {env_vars: [], config_keys: [], feature_flags: []}
concurrency:
  pattern: "N/A (helpers)"
  shared_state: []
  timing: "N/A"
invariants:
  - "JSON codecs round-trip"
error_handling:
  expected_errors: ["Marshalling errors surfaced as 400"]
  retries_timeouts: "none"
security:
  authz: "N/A"
  input_validation: "Schema via JSON Reads"
  sensitive_ops: []
tests: []
coverage_quality: "low"
golden_seeds: []
similar_or_duplicate_files:
  - "airline-data/src/main/scala/com/patson/model/json/* (if any)"
rewrite_notes:
  mapping: "Centralize DTOs and codecs; versioned API schemas"
  risks: ["Implicit magic in package object"]
  confidence: "high"
```

#### airline-rest/conf/application.conf
```yaml
file: airline-rest/conf/application.conf
lang: HOCON
role: "Play configuration (server, filters, possibly DB and CORS)"
size:
  lines_est: 65
  functions_est: 0
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io: {}
config:
  env_vars: ["PLAY_HTTP_SECRET_KEY?","PORT?","DB_*?"]
  config_keys:
    - "play.server.http.port"
    - "play.filters.enabled"
    - "play.http.secret.key"
    - "db.default.url? db.default.driver? (if present)"
  feature_flags: ["cors.enabled?"]
concurrency: {}
invariants: []
error_handling: {}
security:
  authz: "N/A"
  input_validation: "N/A"
  sensitive_ops: ["secret key management"]
tests: {}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Move to typed config; externalize secrets; add prod profiles"
  risks: ["Secrets in config if not externalized"]
  confidence: "med"
```

#### airline-rest/conf/routes
```yaml
file: airline-rest/conf/routes
lang: Play Routes
role: "HTTP routing table mapping URL patterns to controllers"
size:
  lines_est: 110
  functions_est: 0
  classes_est: 0
public_api:
  routes:
    - method: GET
      path: "/..."
      summary: "Multiple endpoints bound to Application/Airline/Airplane/Link controllers"
  exports: []
data_model: {}
queries: {}
external_io: {}
config: {}
concurrency: {}
invariants:
  - "Unique method+path per action"
error_handling: {}
security: {}
tests: {}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Generate OpenAPI spec; enforce stability and versioning (/v1)"
  risks: ["No version prefix; path drift risk"]
  confidence: "high"
```

A typical Play route line (illustrative):
```scala
GET   /links/:id            controllers.LinkApplication.getLink(id: Int)
POST  /links                controllers.LinkApplication.createLink
```

#### airline-rest/project/.gitignore
```yaml
file: airline-rest/project/.gitignore
lang: text
role: "Ignore build artifacts in SBT project folder"
size:
  lines_est: 5
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
tests: {}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Replicate ignore rules"
  risks: []
  confidence: "high"
```

#### airline-rest/project/build.properties
```yaml
file: airline-rest/project/build.properties
lang: text
role: "SBT version pin for this module"
size:
  lines_est: 6
  functions_est: 0
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io: {}
config:
  env_vars: []
  config_keys: ["sbt.version"]
  feature_flags: []
concurrency: {}
invariants: []
error_handling: {}
security: {}
tests: {}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Update to supported SBT/Scala versions"
  risks: ["Version drift across modules"]
  confidence: "high"
```

#### airline-rest/project/plugins.sbt
```yaml
file: airline-rest/project/plugins.sbt
lang: SBT
role: "SBT plugins (Play, sbt plugins)"
size:
  lines_est: 6
  functions_est: 0
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io: {}
config:
  env_vars: []
  config_keys: ["addSbtPlugin(\"com.typesafe.play\" % \"sbt-plugin\" % <ver>)"]
  feature_flags: []
concurrency: {}
invariants: []
error_handling: {}
security: {}
tests: {}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Update plugin ecosystem; remove activator reliance"
  risks: ["Legacy plugin versions"]
  confidence: "high"
```

## Rollup for airline-rest

**Key responsibilities (top 5):**
1. Expose REST endpoints for airlines, links (routes), and airplanes, mapping to domain/data-layer operations.
2. Validate and parse HTTP inputs, marshal JSON responses, and perform simple orchestration without heavy business logic.
3. Configure the web server environment (ports, filters, secrets, CORS), and declare routes.
4. Provide legacy Play build/activator scripts and SBT configuration.
5. Serve as the public API layer for clients to interact with the simulation.

**Cross-module dependencies (top 10 by frequency):**
- com.patson.data.* → DAO calls for airlines, links, airplanes, stats.
- com.patson.util.* caches → improve lookup performance during requests.
- com.patson.model.* → JSON marshalling of domain objects.
- airline-data simulation services → implicit dependency when writes trigger sim-affecting changes.
- Play Framework → controllers, routing, filters, JSON combinators.
- application.conf → port, filters, secret config.
- routes → endpoint binding to controller actions.
- SBT/Play plugins → build/run infrastructure.
- HTTP clients → external consumers of this API (not present here).
- Authentication/Authorization → likely external or missing; must be added in rewrite.

**High-leverage files to study first (top 10):**
- app/controllers/LinkApplication.scala — the most complex API surface; route CRUD and stats.
- conf/routes — authoritative list of endpoints and their signatures.
- app/controllers/AirlineApplication.scala — airline profile/info operations.
- app/controllers/AirplaneApplication.scala — fleet/model queries.
- app/controllers/Application.scala — common endpoints (health/search).
- app/controllers/package.scala — implicit JSON codecs and helpers.
- conf/application.conf — server/runtime configuration and any DB references.
- build.sbt + project/plugins.sbt — establish current dependency baseline.
- activator/activator.bat — identify legacy dev workflow to replace.
- airline-data module (external) — referenced DAOs and domain models used by controllers.

**Mermaid: high-level dependency sketch**
```mermaid
graph TD
  Client --> REST[airline-rest (Play)]
  REST --> Controllers
  Controllers --> Data[com.patson.data.*]
  Controllers --> Util[com.patson.util.*]
  Controllers --> Model[com.patson.model.*]
  REST --> Config[application.conf]
```