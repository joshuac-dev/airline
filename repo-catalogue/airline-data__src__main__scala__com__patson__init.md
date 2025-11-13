<!-- CATALOG:PATH="airline-data\src\main\scala\com\patson\init" SLUG="airline-data__src__main__scala__com__patson__init" -->

# Repo Catalogue — airline-data\src\main\scala\com\patson\init

*Slug:* `airline-data__src__main__scala__com__patson__init`  
*Commit:* `6160503`  
*Generated:* `2025-11-12T22:41:11Z`

**Summary (2–4 sentences):** Batch initializers, patchers, and generators that populate or correct the simulation’s core datasets (airports, runways, geodata, airplane models, features), create NPC/AI airlines, pre-compute transit caches, and import weather or other external metadata. These utilities mainly operate as one-off or scheduled maintenance scripts that read external sources (Wikipedia/API/CSVs), derive domain attributes (e.g., airport size, runway capability), and write normalized records into the database. They are critical for reproducing the canonical world-state and for data hygiene across versions. The rewrite should convert them into idempotent, versioned, observable data pipelines with clear provenance.

**Contents overview**
- Files: 27  |  Subfolders: 0  |  Languages: Scala (~96%), Java (~4%)
- Notable responsibilities:
  - Seed and patch world data (airports, runways, geospatial attributes, features, weather).
  - Generate airplane models and AI airlines; adjust pricing and player resets.
  - Produce asset blueprints and relationship graphs (country mutual relations, transit).
  - Import/update from external sources (Wikipedia/Google/Weather), normalize and persist.
  - Provide main entry points to run initial/all-up migrations and targeted patch jobs.

### File entries

#### airline-data/src/main/scala/com/patson/init/AdditionalLoader.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AdditionalLoader.scala
lang: Scala
role: "Loads supplemental domain data (post-core seed), e.g., extras/features/metadata"
size:
  lines_est: 180
  functions_est: 14
  classes_est: 2
public_api:
  routes: []
  exports:
    - name: AdditionalLoader
      kind: object
      summary: "Entry points to load extra datasets after base initialization"
data_model:
  tables_read: ["airport","country","asset","feature"]           # assumed
  tables_written: ["feature","asset","aux_tables"]               # assumed
  migrations: []
  entities:
    - name: SupplementalDataset
      summary: "Derived or optional records enriching the world model"
queries:
  sql: []
  orm_calls:
    - "FeatureSource.load(...) — read external and upsert features"
external_io:
  http_calls: []
  message_queues: []
  files_read: ["data/*.csv","data/*.json"]                       # assumed
  files_written: []
config:
  env_vars: []
  config_keys: ["loader.additional.enabled?"]
  feature_flags: ["data.additional"]
concurrency:
  pattern: "single-threaded batch"
  shared_state: []
  timing: "manual/one-off or admin-triggered"
invariants:
  - "Idempotent re-runs do not duplicate rows"
  - "Foreign keys resolve to existing base entities"
error_handling:
  expected_errors: ["Missing optional datasets","FK violations"]
  retries_timeouts: "manual rerun; no built-in retry"
security:
  authz: "admin/operator only"
  input_validation: "basic schema checks"
  sensitive_ops: []
tests:
  files: []
  coverage_quality: "low"
  golden_seeds: []
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Move to declarative ETL stage with schema validation & upsert guards"
  risks: ["Non-idempotent inserts","Silent partial loads"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/AiPricePatcher.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AiPricePatcher.scala
lang: Scala
role: "Bulk-corrects AI airline prices to target heuristics"
size:
  lines_est: 35
  functions_est: 3
  classes_est: 1
public_api: {routes: [], exports: [{name: AiPricePatcher, kind: object, summary: "Scan and patch AI link prices"}]}
data_model:
  tables_read: ["link","airline"]
  tables_written: ["link"]
  migrations: []
  entities:
    - name: Link
      summary: "Operational route to adjust price"
queries:
  sql:
    - op: UPDATE
      target: link
      where_keys: ["airline_id (AI)"]
      notes: "Adjust price margin toward heuristic target"
external_io: {http_calls: [], message_queues: [], files_read: [], files_written: []}
config:
  env_vars: []
  config_keys: ["pricing.ai.target.margin?"]
  feature_flags: []
concurrency: {pattern: "single-threaded batch", shared_state: [], timing: "manual run"}
invariants:
  - "New price ≥ floor"
  - "No changes to player airlines"
error_handling: {expected_errors: ["No AI links found"], retries_timeouts: "N/A"}
security: {authz: "admin/operator", input_validation: "bounds check", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["Pricing.scala"]
rewrite_notes:
  mapping: "Admin job in pricing service with guardrails & audit"
  risks: ["Overwriting human-tuned prices","Missing audit log"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/AirlineGenerator.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AirlineGenerator.scala
lang: Scala
role: "Creates initial airlines (NPC/AI and/or seed players) with attributes"
size:
  lines_est: 260
  functions_est: 18
  classes_est: 2
public_api: {routes: [], exports: [{name: AirlineGenerator, kind: object, summary: "Generate starter airlines"}]}
data_model:
  tables_read: ["airport","country"]
  tables_written: ["airline","airline_info","airline_base"]
  migrations: []
  entities:
    - name: Airline
      summary: "Seeded airline entities for baseline simulation"
    - name: AirlineBase
      summary: "Initial presence at selected airports"
queries:
  sql:
    - op: INSERT
      target: airline
      where_keys: []
      notes: "Initial airlines with strategy attributes"
external_io:
  http_calls: []
  message_queues: []
  files_read: ["data/seed/airlines.json?"]                       # assumed
  files_written: []
config:
  env_vars: []
  config_keys: ["seed.ai.count?","seed.base.start.level?"]
  feature_flags: ["seed.airlines"]
concurrency: {pattern: "single-threaded batch", shared_state: [], timing: "bootstrap step"}
invariants:
  - "Airline code/name uniqueness"
  - "Base airports exist and are open"
error_handling:
  expected_errors: ["Duplicate alliances/codes","Invalid seed inputs"]
  retries_timeouts: "N/A"
security: {authz: "admin/operator", input_validation: "schema validation", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Idempotent seeding via migrations with repeatable scripts"
  risks: ["Non-deterministic random seeding","Drift with later patchers"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/AirplaneModelInitializer.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AirplaneModelInitializer.scala
lang: Scala
role: "Seeds airplane models into the catalog"
size:
  lines_est: 30
  functions_est: 3
  classes_est: 1
public_api: {routes: [], exports: [{name: AirplaneModelInitializer, kind: object, summary: "Insert baseline plane models"}]}
data_model:
  tables_read: []
  tables_written: ["airplane_model"]
  migrations: ["airline-data/db_scripts/issue271_airplane_family.sql"]
  entities:
    - name: AirplaneModel
      summary: "Static aircraft types"
queries:
  sql:
    - op: INSERT
      target: airplane_model
      where_keys: []
      notes: "Bulk insert family, range, economics"
external_io: {http_calls: [], message_queues: [], files_read: ["data/aircraft.csv?"], files_written: []}
config: {env_vars: [], config_keys: ["model.catalog.source?"], feature_flags: []}
concurrency: {pattern: "single-threaded", shared_state: [], timing: "bootstrap"}
invariants:
  - "Model names unique"
  - "Positive capacity/range"
error_handling: {expected_errors: ["Missing rows"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "schema & numeric bounds", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["airplane/Model.scala"]
rewrite_notes:
  mapping: "Declarative catalog migration + checksum verification"
  risks: ["Drift with in-code model constants"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/AirplaneModelPatcher.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AirplaneModelPatcher.scala
lang: Scala
role: "Adjusts existing airplane model stats (fixes/retunes)"
size:
  lines_est: 15
  functions_est: 2
  classes_est: 1
public_api: {routes: [], exports: [{name: AirplaneModelPatcher, kind: object, summary: "Update model fields"}]}
data_model:
  tables_read: ["airplane_model"]
  tables_written: ["airplane_model"]
  migrations: []
  entities:
    - name: AirplaneModel
      summary: "Static spec updates"
queries:
  sql:
    - op: UPDATE
      target: airplane_model
      where_keys: ["model_id or name"]
      notes: "Apply delta to capacity/range/fuel"
external_io: {http_calls: [], message_queues: [], files_read: ["patches/*.csv?"], files_written: []}
config: {env_vars: [], config_keys: ["patch.model.version"], feature_flags: []}
concurrency: {pattern: "single-threaded", shared_state: [], timing: "on demand"}
invariants:
  - "Values remain within valid domain"
error_handling: {expected_errors: ["No matching model"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "delta validation", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["airplane/Model.scala"]
rewrite_notes:
  mapping: "Versioned diff migrations; additive changelogs"
  risks: ["Out-of-band adjustments without audit"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/AirportAnimationPatcher.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AirportAnimationPatcher.scala
lang: Scala
role: "Derives/patches UI animation metadata for airports"
size:
  lines_est: 430
  functions_est: 28
  classes_est: 2
public_api: {routes: [], exports: [{name: AirportAnimationPatcher, kind: object, summary: "Compute and set animation flags"}]}
data_model:
  tables_read: ["airport","airport_stats"]
  tables_written: ["airport_animation"]
  migrations: []
  entities:
    - name: AirportAnimation
      summary: "Visual/UX state data"
queries:
  sql:
    - op: UPSERT
      target: airport_animation
      where_keys: ["airport_id"]
      notes: "Compute from traffic/cargo/reputation"
external_io: {http_calls: [], message_queues: [], files_read: [], files_written: []}
config: {env_vars: [], config_keys: ["animation.thresholds.*"], feature_flags: []}
concurrency: {pattern: "single-threaded batch", shared_state: [], timing: "manual/scheduled"}
invariants:
  - "No orphan animation records"
error_handling: {expected_errors: ["Missing stats"], retries_timeouts: "N/A"}
security: {authz: "admin/operator", input_validation: "FK checks", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["model/animation/AirportAnimation.scala"]
rewrite_notes:
  mapping: "Computed projection job with deterministic spec and tests"
  risks: ["UI-coupled back-end data","Opaque thresholds"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/AirportFeaturePatcher.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AirportFeaturePatcher.scala
lang: Scala
role: "Assigns or corrects airport features (capabilities/perks)"
size:
  lines_est: 340
  functions_est: 22
  classes_est: 2
public_api: {routes: [], exports: [{name: AirportFeaturePatcher, kind: object, summary: "Patch airport_features by rules"}]}
data_model:
  tables_read: ["airport","airport_features","airport_stats"]
  tables_written: ["airport_features"]
  migrations: []
  entities:
    - name: AirportFeature
      summary: "Capability label"
queries:
  sql:
    - op: UPSERT
      target: airport_features
      where_keys: ["airport_id","feature_type"]
      notes: "Rule-based adding/removing entries"
external_io: {http_calls: [], message_queues: [], files_read: ["data/feature_rules.json?"], files_written: []}
config: {env_vars: [], config_keys: ["feature.rules.path"], feature_flags: []}
concurrency: {pattern: "single-threaded", shared_state: [], timing: "batch"}
invariants:
  - "No duplicate features per airport"
error_handling: {expected_errors: ["Rule conflicts"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "rule schema validation", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["model/AirportFeature.scala"]
rewrite_notes:
  mapping: "Rules engine + declarative policy with test vectors"
  risks: ["Hard-coded heuristics","No dry-run/audit"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/AirportGeoPatcher.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AirportGeoPatcher.scala
lang: Scala
role: "Fixes/updates airport geospatial attributes (lat/lon/elevation/ICAO)"
size:
  lines_est: 110
  functions_est: 8
  classes_est: 1
public_api: {routes: [], exports: [{name: AirportGeoPatcher, kind: object, summary: "Apply geodata corrections"}]}
data_model:
  tables_read: ["airport"]
  tables_written: ["airport"]
  migrations: []
  entities:
    - name: Airport
      summary: "Geospatial corrections"
queries:
  sql:
    - op: UPDATE
      target: airport
      where_keys: ["iata/icao","name"]
      notes: "Correct lat/lon/elevation from authoritative source"
external_io: {http_calls: [], message_queues: [], files_read: ["data/geo/airports.csv"], files_written: []}
config: {env_vars: [], config_keys: ["geo.source.path"], feature_flags: []}
concurrency: {pattern: "single-threaded", shared_state: [], timing: "batch"}
invariants:
  - "Coordinates within valid bounds"
error_handling: {expected_errors: ["Unmatched airports"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "format checks", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["GeoDataPatcher.scala","GeoDataGenerator.scala"]
rewrite_notes:
  mapping: "ETL transform stage with diff report & rollback"
  risks: ["Source mismatch by name collisions"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/AirportProfilePicturePatcher.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AirportProfilePicturePatcher.scala
lang: Scala
role: "Sets or updates airport profile images (URLs/asset refs)"
size:
  lines_est: 105
  functions_est: 8
  classes_est: 1
public_api: {routes: [], exports: [{name: AirportProfilePicturePatcher, kind: object, summary: "Patch airport profile pics"}]}
data_model:
  tables_read: ["airport"]
  tables_written: ["airport"]
  migrations: []
  entities:
    - name: Airport
      summary: "Visual metadata fields"
queries:
  sql:
    - op: UPDATE
      target: airport
      where_keys: ["airport_id"]
      notes: "Set image url/hash"
external_io:
  http_calls: ["wikipedia.org","wikimedia.org"]                  # assumed
  message_queues: []
  files_read: []
  files_written: []
config:
  env_vars: []
  config_keys: ["media.source","media.license.allowlist"]
  feature_flags: []
concurrency: {pattern: "single-threaded", shared_state: [], timing: "batch"}
invariants:
  - "Only CC-compatible/allowed licenses"
  - "Valid URL format"
error_handling:
  expected_errors: ["404 image","license not allowed"]
  retries_timeouts: "simple retry/backoff (assumed)"
security:
  authz: "admin"
  input_validation: "URL & license checks"
  sensitive_ops: []
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["WikiUtil.scala"]
rewrite_notes:
  mapping: "Media pipeline with license verification & caching"
  risks: ["Hotlinking","Copyright risks"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/AirportRunwayAnalyzer.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AirportRunwayAnalyzer.scala
lang: Scala
role: "Analyzes runway data to infer operational capabilities"
size:
  lines_est: 195
  functions_est: 14
  classes_est: 2
public_api: {routes: [], exports: [{name: AirportRunwayAnalyzer, kind: object, summary: "Compute runway-derived constraints"}]}
data_model:
  tables_read: ["airport","runway?"]
  tables_written: ["airport_stats","airport_features?"]
  migrations: []
  entities:
    - name: RunwayCapability
      summary: "Derived limits from length/surface/elevation"
queries:
  sql:
    - op: SELECT
      target: runway
      where_keys: ["airport_id"]
      notes: "Aggregate lengths/surfaces"
external_io: {http_calls: [], message_queues: [], files_read: ["data/runways.csv"], files_written: []}
config: {env_vars: [], config_keys: ["runway.min.lengths.*"], feature_flags: []}
concurrency: {pattern: "single-threaded batch", shared_state: [], timing: "on demand"}
invariants:
  - "Derived capability consistent with aircraft requirements"
error_handling: {expected_errors: ["Missing runway records"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "numeric bounds", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["AirportRunwayPatcher.scala"]
rewrite_notes:
  mapping: "Analytics job → materialized constraint view"
  risks: ["Heuristic thresholds hard-coded"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/AirportRunwayPatcher.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AirportRunwayPatcher.scala
lang: Scala
role: "Patches runway attributes (length/surface/ids) from authoritative source"
size:
  lines_est: 25
  functions_est: 3
  classes_est: 1
public_api: {routes: [], exports: [{name: AirportRunwayPatcher, kind: object, summary: "Update runway table from upstream"}]}
data_model:
  tables_read: ["runway"]
  tables_written: ["runway"]
  migrations: []
  entities:
    - name: Runway
      summary: "Physical runway row"
queries:
  sql:
    - op: UPDATE
      target: runway
      where_keys: ["airport_id","runway_ident"]
      notes: "Normalize units & surfaces"
external_io: {http_calls: [], message_queues: [], files_read: ["data/runways.csv"], files_written: []}
config: {env_vars: [], config_keys: ["runway.source.path"], feature_flags: []}
concurrency: {pattern: "single-threaded", shared_state: [], timing: "batch"}
invariants:
  - "No duplicate runway idents per airport"
error_handling: {expected_errors: ["Unmatched ident"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "format & unit checks", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["AirportRunwayAnalyzer.scala"]
rewrite_notes:
  mapping: "ETL normalization step with unit conversion"
  risks: ["Partial updates without transaction"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/AirportSizeAdjust.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AirportSizeAdjust.scala
lang: Scala
role: "Recalculates airport size/classification from traffic and facilities"
size:
  lines_est: 290
  functions_est: 20
  classes_est: 2
public_api: {routes: [], exports: [{name: AirportSizeAdjust, kind: object, summary: "Adjust size tiers and related stats"}]}
data_model:
  tables_read: ["airport","airport_stats","link_stats"]
  tables_written: ["airport"]
  migrations: []
  entities:
    - name: Airport
      summary: "Updated size tier field"
queries:
  sql:
    - op: UPDATE
      target: airport
      where_keys: ["airport_id"]
      notes: "Compute new size tier and persist"
external_io: {http_calls: [], message_queues: [], files_read: [], files_written: []}
config: {env_vars: [], config_keys: ["airport.size.thresholds.*"], feature_flags: []}
concurrency: {pattern: "single-threaded batch", shared_state: [], timing: "scheduled/manual"}
invariants:
  - "Size monotonically reflects traffic bands"
error_handling: {expected_errors: ["No stats available"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "band bounds", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["model/Airport.scala"]
rewrite_notes:
  mapping: "Deterministic classification job with locked thresholds"
  risks: ["Magic thresholds baked-in"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/AirportWeatherData.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AirportWeatherData.scala
lang: Scala
role: "Weather metadata holder/transformer for airports"
size:
  lines_est: 100
  functions_est: 8
  classes_est: 2
public_api: {routes: [], exports: [{name: AirportWeatherData, kind: object, summary: "Compute/store airport weather info"}]}
data_model:
  tables_read: ["airport","weather?"]
  tables_written: ["airport_weather?"]
  migrations: []
  entities:
    - name: AirportWeather
      summary: "Derived weather stats by airport"
queries: {sql: [], orm_calls: []}
external_io:
  http_calls: []
  message_queues: []
  files_read: ["weather/*.csv?"]
  files_written: []
config: {env_vars: [], config_keys: ["weather.source.path"], feature_flags: []}
concurrency: {pattern: "batch", shared_state: [], timing: "on demand"}
invariants:
  - "Values within climatology ranges"
error_handling: {expected_errors: ["Missing station match"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "bounds checks", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["WeatherImporter.java"]
rewrite_notes:
  mapping: "Weather ETL with station-to-airport mapping rules"
  risks: ["Station mapping ambiguity"]
  confidence: "low"
```

#### airline-data/src/main/scala/com/patson/init/AssetBlueprintGenerator.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/AssetBlueprintGenerator.scala
lang: Scala
role: "Generates blueprint templates for airport assets/upgrades"
size:
  lines_est: 380
  functions_est: 28
  classes_est: 3
public_api: {routes: [], exports: [{name: AssetBlueprintGenerator, kind: object, summary: "Produce asset configuration blueprints"}]}
data_model:
  tables_read: []
  tables_written: ["asset_blueprint"]
  migrations: []
  entities:
    - name: AssetBlueprint
      summary: "Template for buildable asset types"
queries:
  sql:
    - op: INSERT
      target: asset_blueprint
      where_keys: []
      notes: "Emit level/cost/effects from formulas"
external_io: {http_calls: [], message_queues: [], files_read: ["data/asset_rules.json?"], files_written: []}
config: {env_vars: [], config_keys: ["asset.levels.max","asset.cost.scale"], feature_flags: []}
concurrency: {pattern: "single-threaded batch", shared_state: [], timing: "bootstrap/regeneration"}
invariants:
  - "Monotonic cost curve"
  - "Effect sizes within game balance"
error_handling: {expected_errors: ["Invalid rule input"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "rule schema", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["model/AirportAsset.scala"]
rewrite_notes:
  mapping: "Config-driven generator with locked unit tests"
  risks: ["Implicit constants across modules"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/CountryMutualRelationshipGenerator.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/CountryMutualRelationshipGenerator.scala
lang: Scala
role: "Computes bilateral country relationship scores"
size:
  lines_est: 150
  functions_est: 10
  classes_est: 1
public_api: {routes: [], exports: [{name: CountryMutualRelationshipGenerator, kind: object, summary: "Derive country affinity matrix"}]}
data_model:
  tables_read: ["country"]
  tables_written: ["country_mutual_relationship"]
  migrations: []
  entities:
    - name: CountryRelationship
      summary: "Score among country pairs"
queries:
  sql:
    - op: INSERT
      target: country_mutual_relationship
      where_keys: ["country_id_a","country_id_b"]
      notes: "Symmetric/normalized scores"
external_io: {http_calls: [], message_queues: [], files_read: ["data/country_relations.csv?"], files_written: []}
config: {env_vars: [], config_keys: ["relationship.weights.*"], feature_flags: []}
concurrency: {pattern: "batch", shared_state: [], timing: "bootstrap or recalibration"}
invariants:
  - "Score bounded [-1, 1]"
  - "Matrix symmetry (a,b) == (b,a)"
error_handling: {expected_errors: ["Missing country"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "id mapping validation", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["model/AirlineCountryRelationship.scala"]
rewrite_notes:
  mapping: "Materialized affinity table with provenance"
  risks: ["Opaque weighting scheme"]
  confidence: "low"
```

#### airline-data/src/main/scala/com/patson/init/GeneratedUserPurger.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/GeneratedUserPurger.scala
lang: Scala
role: "Deletes generated/test users and associated airlines"
size:
  lines_est: 18
  functions_est: 2
  classes_est: 1
public_api: {routes: [], exports: [{name: GeneratedUserPurger, kind: object, summary: "Purge synthetic accounts"}]}
data_model:
  tables_read: ["user","airline"]
  tables_written: ["user","airline"]
  migrations: []
  entities:
    - name: User
      summary: "Account rows to remove"
queries:
  sql:
    - op: DELETE
      target: user
      where_keys: ["name/prefix=test?"]
      notes: "Cascade airline cleanup"
external_io: {http_calls: [], message_queues: [], files_read: [], files_written: []}
config: {env_vars: [], config_keys: ["purge.user.prefix"], feature_flags: []}
concurrency: {pattern: "one-off job", shared_state: [], timing: "manual"}
invariants:
  - "No deletion of real users"
error_handling: {expected_errors: ["No matching users"], retries_timeouts: "N/A"}
security: {authz: "admin-only", input_validation: "prefix validation", sensitive_ops: ["deletion"]}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Admin purge with dry-run and audit log"
  risks: ["Accidental deletions without preview"]
  confidence: "high"
```

#### airline-data/src/main/scala/com/patson/init/GenericTransitGenerator.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/GenericTransitGenerator.scala
lang: Scala
role: "Precomputes generic transit graph/caches for routing"
size:
  lines_est: 95
  functions_est: 8
  classes_est: 1
public_api: {routes: [], exports: [{name: GenericTransitGenerator, kind: object, summary: "Build transit graph artifacts"}]}
data_model:
  tables_read: ["airport","link"]
  tables_written: ["transit_cache"]
  migrations: []
  entities:
    - name: TransitCache
      summary: "Prebuilt nodes/edges for fast routing"
queries:
  sql:
    - op: INSERT
      target: transit_cache
      where_keys: ["origin_id","dest_id"]
      notes: "Graph edges & metrics"
external_io: {http_calls: [], message_queues: [], files_read: [], files_written: []}
config: {env_vars: [], config_keys: ["transit.max.hops","transit.cache.size"], feature_flags: []}
concurrency: {pattern: "batch", shared_state: [], timing: "bootstrap or scheduled refresh"}
invariants:
  - "No cycles in stored paths"
error_handling: {expected_errors: ["Disconnected components"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "graph constraints", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["model/GenericTransit.scala"]
rewrite_notes:
  mapping: "Graph build job with deterministic seeds & metrics"
  risks: ["Stale cache if topology changes"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/GeoDataGenerator.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/GeoDataGenerator.scala
lang: Scala
role: "Generates/normalizes global geodata (cities/countries/regions)"
size:
  lines_est: 720
  functions_est: 50
  classes_est: 4
public_api: {routes: [], exports: [{name: GeoDataGenerator, kind: object, summary: "Seed master geo tables from sources"}]}
data_model:
  tables_read: []
  tables_written: ["country","city","region","timezone?"]
  migrations: []
  entities:
    - name: Country
      summary: "Nation rows"
    - name: City
      summary: "Population/coordinates for markets"
queries:
  sql:
    - op: INSERT
      target: country
      where_keys: []
      notes: "Initial population from reference"
external_io:
  http_calls: []
  message_queues: []
  files_read: ["data/geo/*.csv","data/iso/*.csv"]
  files_written: []
config: {env_vars: [], config_keys: ["geo.source.*"], feature_flags: []}
concurrency: {pattern: "single-threaded", shared_state: [], timing: "bootstrap"}
invariants:
  - "ISO codes valid/unique"
  - "Coordinates bounded"
error_handling: {expected_errors: ["Malformed rows"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "schema & referential checks", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["GeoDataPatcher.scala","AirportGeoPatcher.scala"]
rewrite_notes:
  mapping: "Scripted ETL with schema contracts and data diffing"
  risks: ["Implicit joins/assumptions across files"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/GeoDataPatcher.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/GeoDataPatcher.scala
lang: Scala
role: "Applies targeted corrections to existing geodata"
size:
  lines_est: 45
  functions_est: 4
  classes_est: 1
public_api: {routes: [], exports: [{name: GeoDataPatcher, kind: object, summary: "Small fixups for geo tables"}]}
data_model:
  tables_read: ["country","city","region"]
  tables_written: ["country","city","region"]
  migrations: []
  entities:
    - name: Country
      summary: "Reference fixups"
queries:
  sql:
    - op: UPDATE
      target: city
      where_keys: ["name/country_code"]
      notes: "Coordinate or population adjustments"
external_io: {http_calls: [], message_queues: [], files_read: ["patches/geo.csv"], files_written: []}
config: {env_vars: [], config_keys: ["patch.geo.path"], feature_flags: []}
concurrency: {pattern: "batch", shared_state: [], timing: "on demand"}
invariants:
  - "FKs remain valid"
error_handling: {expected_errors: ["Row not found"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "delta bounds", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["GeoDataGenerator.scala"]
rewrite_notes:
  mapping: "Diff-driven migration with rollback"
  risks: ["Manual patch drift"]
  confidence: "high"
```

#### airline-data/src/main/scala/com/patson/init/IsolatedAirportPatcher.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/IsolatedAirportPatcher.scala
lang: Scala
role: "Identifies/marks isolated airports (low connectivity)"
size:
  lines_est: 70
  functions_est: 6
  classes_est: 1
public_api: {routes: [], exports: [{name: IsolatedAirportPatcher, kind: object, summary: "Flag isolated airports"}]}
data_model:
  tables_read: ["airport","link"]
  tables_written: ["airport"]
  migrations: []
  entities:
    - name: Airport
      summary: "Isolation flag/score"
queries:
  sql:
    - op: UPDATE
      target: airport
      where_keys: ["airport_id"]
      notes: "Set isolation score/flag"
external_io: {http_calls: [], message_queues: [], files_read: [], files_written: []}
config: {env_vars: [], config_keys: ["isolation.threshold"], feature_flags: []}
concurrency: {pattern: "batch", shared_state: [], timing: "scheduled/manual"}
invariants:
  - "Consistent scoring window across runs"
error_handling: {expected_errors: ["No links data"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "band thresholds", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["AirportSizeAdjust.scala"]
rewrite_notes:
  mapping: "Analytics projection with locked formula"
  risks: ["Heuristics hard-coded"]
  confidence: "low"
```

#### airline-data/src/main/scala/com/patson/init/MainInit.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/MainInit.scala
lang: Scala
role: "Top-level orchestrator for seeding/patching tasks"
size:
  lines_est: 20
  functions_est: 2
  classes_est: 1
public_api: {routes: [], exports: [{name: MainInit, kind: object, summary: "Run initialization sequence"}]}
data_model:
  tables_read: []
  tables_written: []
  migrations: []
  entities: []
queries: {sql: [], orm_calls: []}
external_io: {http_calls: [], message_queues: [], files_read: [], files_written: []}
config:
  env_vars: []
  config_keys: ["init.steps","init.dryRun"]
  feature_flags: []
concurrency: {pattern: "sequential job orchestrator", shared_state: [], timing: "one-off"}
invariants:
  - "Steps run in dependency order"
error_handling: {expected_errors: ["Step failure"], retries_timeouts: "manual rerun per step"}
security: {authz: "admin/operator", input_validation: "step list validation", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Migration runner with idempotency & progress checkpoints"
  risks: ["Partial state if interrupted"]
  confidence: "high"
```

#### airline-data/src/main/scala/com/patson/init/MaxAirportChampTest.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/MaxAirportChampTest.scala
lang: Scala
role: "Ad-hoc analyzer for 'max airport champ' scenario (dev/testing)"
size:
  lines_est: 60
  functions_est: 5
  classes_est: 1
public_api: {routes: [], exports: [{name: MaxAirportChampTest, kind: object, summary: "Diagnostic sandbox"}]}
data_model:
  tables_read: ["airport","airline","link"]
  tables_written: []
  migrations: []
  entities: []
queries: {sql: [], orm_calls: []}
external_io: {http_calls: [], message_queues: [], files_read: [], files_written: []}
config: {env_vars: [], config_keys: [], feature_flags: []}
concurrency: {pattern: "one-off console app", shared_state: [], timing: "manual"}
invariants:
  - "Read-only"
error_handling: {expected_errors: [], retries_timeouts: "N/A"}
security: {authz: "dev-only", input_validation: "N/A", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Remove or move to standalone analytics notebook"
  risks: ["Not part of production pipeline"]
  confidence: "high"
```

#### airline-data/src/main/scala/com/patson/init/PlayerResetPatcher.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/PlayerResetPatcher.scala
lang: Scala
role: "Reset player state (balances, progress) per policy"
size:
  lines_est: 55
  functions_est: 5
  classes_est: 1
public_api: {routes: [], exports: [{name: PlayerResetPatcher, kind: object, summary: "Apply resets to player accounts"}]}
data_model:
  tables_read: ["user","airline","airline_info"]
  tables_written: ["airline","airline_info"]
  migrations: []
  entities:
    - name: Airline
      summary: "Reset progression fields"
queries:
  sql:
    - op: UPDATE
      target: airline_info
      where_keys: ["airline_id"]
      notes: "Reset KPIs to baseline"
external_io: {http_calls: [], message_queues: [], files_read: [], files_written: []}
config: {env_vars: [], config_keys: ["reset.policy","reset.cutoff.cycle"], feature_flags: []}
concurrency: {pattern: "batch", shared_state: [], timing: "event/scheduled"}
invariants:
  - "Only eligible players reset"
error_handling: {expected_errors: ["No eligible rows"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "eligibility checks", sensitive_ops: ["bulk updates"]}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Admin endpoint with preview/diff & audit"
  risks: ["Player-facing impact without rollback"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/SantaClausPatcher.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/SantaClausPatcher.scala
lang: Scala
role: "Seasonal event patcher (Christmas-specific enablement/cleanup)"
size:
  lines_est: 75
  functions_est: 6
  classes_est: 1
public_api: {routes: [], exports: [{name: SantaClausPatcher, kind: object, summary: "Toggle/seed Christmas event data"}]}
data_model:
  tables_read: ["seasonal_award?","airline"]
  tables_written: ["seasonal_award?"]
  migrations: []
  entities:
    - name: SeasonalAward
      summary: "Event-specific rewards state"
queries: {sql: [], orm_calls: []}
external_io: {http_calls: [], message_queues: [], files_read: [], files_written: []}
config: {env_vars: [], config_keys: ["christmas.event.enabled"], feature_flags: ["event.christmas"]}
concurrency: {pattern: "one-off/event-timed", shared_state: [], timing: "seasonal"}
invariants:
  - "Event bounded by configured window"
error_handling: {expected_errors: ["Already active"], retries_timeouts: "N/A"}
security: {authz: "admin", input_validation: "window checks", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["model/christmas/*"]
rewrite_notes:
  mapping: "Generic event framework with toggles & schedules"
  risks: ["Event-specific logic embedded here"]
  confidence: "low"
```

#### airline-data/src/main/scala/com/patson/init/WeatherImporter.java
```yaml
file: airline-data/src/main/scala/com/patson/init/WeatherImporter.java
lang: Java
role: "Imports weather datasets and maps to airports"
size:
  lines_est: 170
  functions_est: 12
  classes_est: 1
public_api:
  routes: []
  exports:
    - name: WeatherImporter
      kind: class
      summary: "Parse external weather files and persist normalized rows"
data_model:
  tables_read: ["airport"]
  tables_written: ["airport_weather?","weather_station?"]
  migrations: []
  entities:
    - name: WeatherStation
      summary: "External station metadata"
queries: {sql: [], orm_calls: []}
external_io:
  http_calls: []
  message_queues: []
  files_read: ["weather/*.csv","weather/*.txt"]
  files_written: []
config:
  env_vars: []
  config_keys: ["weather.station.mapping","weather.file.pattern"]
  feature_flags: []
concurrency:
  pattern: "file-IO batch"
  shared_state: []
  timing: "manual"
invariants:
  - "Station-airport mapping deterministic"
error_handling:
  expected_errors: ["Bad records","Unmatched stations"]
  retries_timeouts: "N/A"
security:
  authz: "admin"
  input_validation: "basic CSV row checks"
  sensitive_ops: []
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["AirportWeatherData.scala"]
rewrite_notes:
  mapping: "Consolidate under Scala/Kotlin ETL; add schema contracts"
  risks: ["Mixed language build; fragile parsers"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/WikiUtil.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/WikiUtil.scala
lang: Scala
role: "Wikipedia/Wikimedia helper for fetching/parsing content (e.g., airport images)"
size:
  lines_est: 200
  functions_est: 16
  classes_est: 1
public_api:
  routes: []
  exports:
    - name: WikiUtil
      kind: object
      summary: "Thin utility around MediaWiki endpoints/content parsing"
data_model:
  tables_read: []
  tables_written: []
  migrations: []
  entities: []
queries: {sql: [], orm_calls: []}
external_io:
  http_calls: ["wikipedia.org","wikimedia.org"]
  message_queues: []
  files_read: []
  files_written: []
config:
  env_vars: []
  config_keys: ["wiki.userAgent","wiki.rateLimit.rps"]
  feature_flags: []
concurrency:
  pattern: "HTTP calls; likely sequential with throttling"
  shared_state: []
  timing: "on demand"
invariants:
  - "Respect robots/rate limits"
error_handling:
  expected_errors: ["HTTP 429/5xx","Malformed markup"]
  retries_timeouts: "backoff on transient errors (assumed)"
security:
  authz: "none"
  input_validation: "URL/path sanitization"
  sensitive_ops: []
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: ["AirportProfilePicturePatcher.scala"]
rewrite_notes:
  mapping: "Dedicated integration client with retry/circuit-breaker"
  risks: ["Parsing fragility","No caching layer"]
  confidence: "med"
```

#### airline-data/src/main/scala/com/patson/init/package.scala
```yaml
file: airline-data/src/main/scala/com/patson/init/package.scala
lang: Scala
role: "Package object: shared constants/types for init jobs"
size:
  lines_est: 12
  functions_est: 0
  classes_est: 1
public_api: {routes: [], exports: [{name: package, kind: object, summary: "Shared config/constants for init"}]}
data_model:
  tables_read: []
  tables_written: []
  migrations: []
  entities: []
queries: {sql: [], orm_calls: []}
external_io: {http_calls: [], message_queues: [], files_read: [], files_written: []}
config: {env_vars: [], config_keys: ["init.*"], feature_flags: []}
concurrency: {pattern: "N/A", shared_state: [], timing: "N/A"}
invariants:
  - "Constants referenced across patchers"
error_handling: {expected_errors: [], retries_timeouts: "N/A"}
security: {authz: "N/A", input_validation: "N/A", sensitive_ops: []}
tests: {files: [], coverage_quality: "low"}
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Move constants to typed config"
  risks: ["Hidden globals"]
  confidence: "high"
```

### File entries (notes)
- The data_model and external_io sections contain inferred tables/sources based on filenames and surrounding modules. Confirm exact schema during rewrite.

## Rollup for airline-data\src\main\scala\com\patson\init

**Key responsibilities (top 5):**
1. Seed and normalize core reference data (geodata, airplane models, assets).
2. Patch and reclassify airport/runway/features and related analytics flags.
3. Generate AI airlines, transit caches, and relationship matrices for simulation.
4. Import and map external data (Wikipedia media, weather datasets) to internal entities.
5. Provide operators with one-off maintenance tools (resets, purges, seasonal toggles).

**Cross-module dependencies (top 10 by frequency):**
- airport/airport_stats/link_stats → used by size/animation/feature patchers — derive classifications and UX states.
- airplane_model → used by model initializer/patcher — standardize aircraft catalog.
- asset_blueprint → generated by AssetBlueprintGenerator — build/upgrade templates for model/AirportAsset logic.
- country/city/region → GeoDataGenerator/Patcher — canonical world geodata for all modules.
- country_mutual_relationship → used by diplomacy/reputation systems — airline-country logic.
- transit_cache → GenericTransitGenerator — accelerates routing in passenger simulation.
- link → AI price patcher, isolation detection — pricing and connectivity heuristics.
- user/airline/airline_info → PlayerResetPatcher/Generator/Purger — account lifecycle ops.
- weather_* tables → WeatherImporter/AirportWeatherData — climate effects and features.
- Wikipedia/Wikimedia HTTP → WikiUtil/ProfilePicturePatcher — media/branding integration.

**High-leverage files to study first (top 10):**
- GeoDataGenerator.scala — defines canonical geodata import/normalization.
- AirportFeaturePatcher.scala — rules assigning airport capabilities.
- AirportSizeAdjust.scala — determines airport classification bands.
- AirportRunwayAnalyzer.scala — bridges runway data to operational constraints.
- AssetBlueprintGenerator.scala — emits asset upgrade curves affecting economics.
- AirlineGenerator.scala — controls AI airline seed distribution.
- GenericTransitGenerator.scala — topology cache for routing performance.
- AiPricePatcher.scala — pricing heuristics for AI carriers.
- AirportProfilePicturePatcher.scala + WikiUtil.scala — external media ingestion policies.
- WeatherImporter.java + AirportWeatherData.scala — climate data ETL and mapping.

**Mermaid: high-level dependency sketch (optional, if meaningful)**
```mermaid
graph TD
  Seed[Init/Patch Jobs] --> DB[(DB)]
  Seed --> Ext[External Data (CSV/APIs)]
  Ext --> Wiki[Wikipedia/Wikimedia]
  Ext --> Weather[Weather files]
  Seed --> Geo[Geodata Tables]
  Seed --> Airport[Airports/Features/Runways]
  Seed --> Fleet[Airplane Models]
  Seed --> Assets[Asset Blueprints]
  Seed --> Transit[Transit Cache]
```