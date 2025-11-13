<!-- CATALOG:PATH="airline-web\public\javascripts" SLUG="airline-web__public__javascripts" -->

# Repo Catalogue — airline-web\public\javascripts

*Slug:* `airline-web__public__javascripts`  
*Commit:* `6160503`  
*Generated:* `2025-11-13T00:57:32Z`

Important note about listing completeness:
- This folder listing was gathered via the GitHub Contents API and may be incomplete due to pagination. For a complete, authoritative view of files under this path, browse: https://github.com/joshuac-dev/airline/tree/master/airline-web/public/javascripts

**Summary (2–4 sentences):** Front-end JavaScript powering the airline web UI. Each module maps to a feature domain (airline, airplanes, airports/assets, alliances, events, finance, rankings, search) or cross-cutting concern (bootstrapping, charts, maps, theming, WebSocket, emoji, lazy loading). Scripts orchestrate REST calls to airline-rest, render charts/tables/maps, manage UI state and forms, and maintain a live WebSocket for chat/broadcast updates. Vendor libraries (jQuery, jQuery UI, Highcharts, reconnecting-websocket, lazysizes, emoji) are vendored under subfolders.

**Contents overview**
- Files: 40+  |  Subfolders: 6 (departure-board, emojify, jemoji, lazysizes, libs, splitting)  |  Languages: JavaScript (~92%), CSS (~6%), Markdown/Text (~2%)
- Notable responsibilities:
  - Feature flows for airline operations, fleet management, airport/asset management, alliances/missions, campaigns, oil and loans, events.
  - UI composition and bootstrapping (global handlers, AJAX wrappers, prompts/modals, theme controls).
  - Analytics and visualizations (charts, heatmaps, link histories) via chart libraries.
  - Real-time chat/broadcast via WebSocket with auto-reconnect and emoji rendering.
  - Vendored JS libraries for legacy patterns and visuals.

### File entries

#### airline-web/public/javascripts/about.js

```yaml
file: airline-web/public/javascripts/about.js
lang: JavaScript
role: "About page interactions"
size:
  lines_est: 10
  functions_est: 2
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
external_io: {}
config:
  env_vars: []
  config_keys: []
  feature_flags: []
concurrency:
  pattern: "browser event loop"
  shared_state: []
  timing: "on DOM ready"
invariants: []
error_handling:
  expected_errors: []
  retries_timeouts: "N/A"
security:
  authz: "N/A"
  input_validation: "N/A"
  sensitive_ops: []
tests:
  files: []
  coverage_quality: "low"
  golden_seeds: []
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Small SPA page/component"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/admin.js

```yaml
file: airline-web/public/javascripts/admin.js
lang: JavaScript
role: "Admin UI: maintenance tools, toggles, data views"
size:
  lines_est: 380
  functions_est: 28
  classes_est: 0
public_api:
  routes: []
  exports: []
data_model: {}
queries: {}
external_io:
  http_calls: ["/admin/* (admin endpoints)"]
config:
  env_vars: []
  config_keys: ["admin.*"]
  feature_flags: []
concurrency:
  pattern: "DOM handlers + XHR/fetch"
  shared_state: []
  timing: "user actions"
invariants:
  - "Admin-only actions guarded server-side"
error_handling:
  expected_errors: ["403 Forbidden"]
  retries_timeouts: "N/A"
security:
  authz: "server-enforced"
  input_validation: "basic client checks"
  sensitive_ops: []
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Admin console (RBAC, audit)"
  risks: ["Assumes backend-only enforcement"]
  confidence: "med"
```

#### airline-web/public/javascripts/airline.js

```yaml
file: airline-web/public/javascripts/airline.js
lang: JavaScript
role: "Airline domain UI: info, bases, reputation, branding, notices/alerts"
size:
  lines_est: 2700
  functions_est: 180
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io:
  http_calls: ["/airlines/*","/notices/*","/alerts/*"]
config:
  env_vars: []
  config_keys: ["reputation.*","branding.*"]
  feature_flags: []
concurrency:
  pattern: "DOM events + XHR"
  shared_state: ["global UI state"]
  timing: "user-driven"
invariants:
  - "Reputation bounds enforced"
  - "Unique airline name"
error_handling:
  expected_errors: ["409 Conflict","404 Not Found"]
  retries_timeouts: "manual retry"
security:
  authz: "server-side ownership"
  input_validation: "form-level checks"
  sensitive_ops: []
tests: { files: [], coverage_quality: "med" }
similar_or_duplicate_files:
  - "color.js"
  - "ui-theme.js"
rewrite_notes:
  mapping: "SPA feature slice (state + API client)"
  risks: ["Monolithic DOM coupling"]
  confidence: "med"
```

#### airline-web/public/javascripts/airplane.js

```yaml
file: airline-web/public/javascripts/airplane.js
lang: JavaScript
role: "Fleet UI: models, purchase/sell, assignment, maintenance"
size:
  lines_est: 1450
  functions_est: 110
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io:
  http_calls: ["/airplane-models","/airlines/{id}/airplanes","/links/*"]
config:
  env_vars: []
  config_keys: ["maintenance.*"]
  feature_flags: []
concurrency:
  pattern: "DOM + XHR"
  shared_state: ["selected aircraft"]
  timing: "user-driven"
invariants:
  - "Seat splits ≤ capacity"
  - "Feasible assignments"
error_handling:
  expected_errors: ["invalid assignment","insufficient funds"]
  retries_timeouts: "N/A"
security:
  authz: "server-side"
  input_validation: "bounds/feasibility checks"
  sensitive_ops: ["financial UI"]
tests: { files: [], coverage_quality: "med" }
similar_or_duplicate_files:
  - "model-configuration.js"
rewrite_notes:
  mapping: "Transactional flows with optimistic updates"
  risks: ["Stale data races"]
  confidence: "med"
```

#### airline-web/public/javascripts/airport-asset.js

```yaml
file: airline-web/public/javascripts/airport-asset.js
lang: JavaScript
role: "Airport assets UI: upgrade lifecycle, income"
size:
  lines_est: 520
  functions_est: 36
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io:
  http_calls: ["/airport-assets/*","/income/*"]
config:
  env_vars: []
  config_keys: ["asset.level.max","asset.upgrade.cooldown"]
  feature_flags: []
concurrency:
  pattern: "DOM + XHR"
  shared_state: []
  timing: "user-driven"
invariants:
  - "Level within bounds"
error_handling:
  expected_errors: ["cooldown violation","insufficient funds"]
  retries_timeouts: "N/A"
security:
  authz: "ownership (server)"
  input_validation: "bounds"
  sensitive_ops: []
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Asset service UI"
  risks: ["Client vs server rule drift"]
  confidence: "med"
```

#### airline-web/public/javascripts/airport.js

```yaml
file: airline-web/public/javascripts/airport.js
lang: JavaScript
role: "Airport views: stats, facilities, maps"
size:
  lines_est: 1350
  functions_est: 95
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io:
  http_calls: ["/airports/*","/airport-stats/*"]
config:
  env_vars: []
  config_keys: ["map.*","airport.size.thresholds"]
  feature_flags: []
concurrency:
  pattern: "DOM + map events + XHR"
  shared_state: ["map state"]
  timing: "user-driven"
invariants:
  - "Markers ⇔ dataset alignment"
error_handling:
  expected_errors: ["missing data"]
  retries_timeouts: "UI retry"
security:
  authz: "public/session"
  input_validation: "params"
  sensitive_ops: []
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files:
  - "heatmap.js"
  - "map-style.js"
rewrite_notes:
  mapping: "Map component + data hooks"
  risks: ["DOM id coupling"]
  confidence: "med"
```

#### airline-web/public/javascripts/alliance.js

```yaml
file: airline-web/public/javascripts/alliance.js
lang: JavaScript
role: "Alliance UI: create/join/leave, info"
size:
  lines_est: 980
  functions_est: 70
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io:
  http_calls: ["/alliances/*","/alliance-members/*"]
config: { env_vars: [], config_keys: [], feature_flags: [] }
concurrency:
  pattern: "DOM + XHR"
  shared_state: ["membership"]
  timing: "user-driven"
invariants:
  - "No duplicate membership"
error_handling: { expected_errors: ["conflict"] , retries_timeouts: "N/A" }
security:
  authz: "server-side"
  input_validation: "ids"
  sensitive_ops: []
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Alliance module"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/bank.js

```yaml
file: airline-web/public/javascripts/bank.js
lang: JavaScript
role: "Loans UI: create/repay, interest"
size:
  lines_est: 210
  functions_est: 16
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io: { http_calls: ["/loans/*"] }
config: { env_vars: [], config_keys: ["loan.*"], feature_flags: [] }
concurrency:
  pattern: "DOM + XHR"
  shared_state: []
  timing: "user-driven"
invariants:
  - "Principal ≥ 0"
error_handling: { expected_errors: ["overpayment"], retries_timeouts: "N/A" }
security:
  authz: "ownership (server)"
  input_validation: "amount bounds"
  sensitive_ops: ["financial UI"]
tests: { files: [], coverage_quality: "low" }
rewrite_notes:
  mapping: "Finance UI"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/campaign.js

```yaml
file: airline-web/public/javascripts/campaign.js
lang: JavaScript
role: "Campaigns UI"
size:
  lines_est: 390
  functions_est: 28
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io: { http_calls: ["/campaigns/*"] }
config: { env_vars: [], config_keys: ["campaign.*"], feature_flags: [] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "user-driven" }
invariants:
  - "Duration ≥ 0"
error_handling: {}
security: { authz: "server", input_validation: "bounds", sensitive_ops: [] }
tests: { files: [], coverage_quality: "low" }
rewrite_notes:
  mapping: "Campaign module"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/chat-popup.js

```yaml
file: airline-web/public/javascripts/chat-popup.js
lang: JavaScript
role: "Toggle chat window"
size:
  lines_est: 40
  functions_est: 4
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io: {}
config: {}
concurrency: { pattern: "DOM events", shared_state: [], timing: "on click" }
invariants: []
error_handling: {}
security: {}
tests: { files: [], coverage_quality: "low" }
rewrite_notes:
  mapping: "Component state"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/chat.js

```yaml
file: airline-web/public/javascripts/chat.js
lang: JavaScript
role: "Chat UI: rooms, messages, emoji"
size:
  lines_est: 420
  functions_est: 34
  classes_est: 0
public_api: {}
data_model: {}
queries: {}
external_io:
  message_queues: ["WebSocket via websocket.js"]
config:
  env_vars: []
  config_keys: ["chat.*"]
  feature_flags: []
concurrency:
  pattern: "WS handlers + DOM"
  shared_state: ["room membership"]
  timing: "real-time"
invariants:
  - "Message length limits"
error_handling:
  expected_errors: ["disconnects"]
  retries_timeouts: "auto-reconnect lib"
security:
  authz: "session"
  input_validation: "sanitize/limit"
  sensitive_ops: []
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files:
  - "websocket.js"
  - "emojify/jemoji"
rewrite_notes:
  mapping: "Typed events; rate-limits"
  risks: ["No moderation; buffering"]
  confidence: "med"
```

#### airline-web/public/javascripts/christmas.js

```yaml
file: airline-web/public/javascripts/christmas.js
lang: JavaScript
role: "Seasonal event UI (Christmas)"
size:
  lines_est: 300
  functions_est: 22
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/events/christmas/*"] }
config:
  env_vars: []
  config_keys: ["event.christmas.*"]
  feature_flags: ["event.christmas"]
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "user-driven" }
invariants:
  - "Tier bounds"
error_handling: {}
security: {}
tests: { files: [], coverage_quality: "low" }
rewrite_notes:
  mapping: "Generic event UI"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/color-scheme.js

```yaml
file: airline-web/public/javascripts/color-scheme.js
lang: JavaScript
role: "Theme switching"
size:
  lines_est: 40
  functions_est: 4
  classes_est: 0
public_api: {}
external_io: {}
config:
  env_vars: []
  config_keys: ["ui.theme.*"]
  feature_flags: []
concurrency:
  pattern: "DOM + localStorage"
  shared_state: ["theme"]
  timing: "on load/toggle"
invariants:
  - "Contrast preserved"
tests: { files: [], coverage_quality: "low" }
rewrite_notes:
  mapping: "Theme provider"
  risks: ["specificity clashes"]
  confidence: "high"
```

#### airline-web/public/javascripts/color.js

```yaml
file: airline-web/public/javascripts/color.js
lang: JavaScript
role: "Brand color picker/validation"
size:
  lines_est: 55
  functions_est: 5
  classes_est: 0
public_api: {}
config: {}
concurrency: { pattern: "DOM", shared_state: [], timing: "on input" }
invariants:
  - "Valid hex"
rewrite_notes:
  mapping: "Form component"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/confetti.js

```yaml
file: airline-web/public/javascripts/confetti.js
lang: JavaScript
role: "Confetti animation"
size:
  lines_est: 70
  functions_est: 6
  classes_est: 0
public_api: {}
concurrency:
  pattern: "requestAnimationFrame"
  shared_state: []
  timing: "on event"
invariants:
  - "Adequate performance"
rewrite_notes:
  mapping: "Optional effect"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/country.js

```yaml
file: airline-web/public/javascripts/country.js
lang: JavaScript
role: "Country relationships, titles, market shares"
size:
  lines_est: 600
  functions_est: 40
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/countries/*","/airline-country-relationships/*"] }
config: { env_vars: [], config_keys: ["title.thresholds"], feature_flags: [] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "user-driven" }
invariants:
  - "Scores bounded"
rewrite_notes:
  mapping: "Country slice"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/delegate.js

```yaml
file: airline-web/public/javascripts/delegate.js
lang: JavaScript
role: "Delegates availability/assignments"
size:
  lines_est: 230
  functions_est: 18
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/delegates/*"] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "user-driven" }
invariants:
  - "No over-assignment"
rewrite_notes:
  mapping: "Task scheduling UI"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/departures.js

```yaml
file: airline-web/public/javascripts/departures.js
lang: JavaScript
role: "Glue for departure-board component"
size:
  lines_est: 95
  functions_est: 8
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/departures/*"] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "periodic refresh" }
invariants:
  - "Columns align"
rewrite_notes:
  mapping: "Component adapter"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/event.js

```yaml
file: airline-web/public/javascripts/event.js
lang: JavaScript
role: "Events UI (create/list/advance)"
size:
  lines_est: 720
  functions_est: 52
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/events/*"] }
config: { env_vars: [], config_keys: ["event.*"], feature_flags: [] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "user-driven" }
invariants:
  - "Valid event windows"
rewrite_notes:
  mapping: "Generic event manager"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/facility.js

```yaml
file: airline-web/public/javascripts/facility.js
lang: JavaScript
role: "Facility display (read-only)"
size:
  lines_est: 240
  functions_est: 20
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/airport/facilities/*"] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "user-driven" }
invariants: []
similar_or_duplicate_files:
  - "airport-asset.js"
rewrite_notes:
  mapping: "Read vs write separation"
  risks: ["overlap with asset UI"]
  confidence: "med"
```

#### airline-web/public/javascripts/gadgets.js

```yaml
file: airline-web/public/javascripts/gadgets.js
lang: JavaScript
role: "Widgets (modals, tabs, tooltips, dropdowns)"
size:
  lines_est: 800
  functions_est: 60
  classes_est: 0
public_api: {}
concurrency: { pattern: "DOM events + timers", shared_state: [], timing: "user-driven" }
invariants:
  - "Keyboard/focus accessible"
rewrite_notes:
  mapping: "Component library primitives"
  risks: ["reinventing controls; a11y gaps"]
  confidence: "med"
```

#### airline-web/public/javascripts/heatmap.js

```yaml
file: airline-web/public/javascripts/heatmap.js
lang: JavaScript
role: "Heatmap rendering"
size:
  lines_est: 230
  functions_est: 18
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/heatmap/*","/stats/*"] }
concurrency: { pattern: "DOM + XHR + canvas/SVG", shared_state: ["map state"], timing: "user-driven" }
invariants:
  - "Legend matches scale"
rewrite_notes:
  mapping: "Unified charting layer"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/link-history.js

```yaml
file: airline-web/public/javascripts/link-history.js
lang: JavaScript
role: "Link (route) time-series charts"
size:
  lines_est: 520
  functions_est: 38
  classes_est: 0
public_api: {}
external_io:
  http_calls: ["/links/{id}/stats","/passenger-history/*"]
concurrency: { pattern: "DOM + XHR + charts", shared_state: [], timing: "user-driven" }
invariants:
  - "Monotonic cycles"
rewrite_notes:
  mapping: "Chart adapter over chosen lib"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/log.js

```yaml
file: airline-web/public/javascripts/log.js
lang: JavaScript
role: "Admin log viewer"
size:
  lines_est: 170
  functions_est: 12
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/logs/*"] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "user-driven" }
invariants: []
rewrite_notes:
  mapping: "Observability UI"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/main.js

```yaml
file: airline-web/public/javascripts/main.js
lang: JavaScript
role: "App bootstrap: global handlers, AJAX, session, toasts"
size:
  lines_est: 860
  functions_est: 65
  classes_est: 0
public_api: {}
external_io:
  http_calls: ["shared wrapper over many endpoints"]
concurrency:
  pattern: "DOM ready + event bus"
  shared_state: ["global UI/session"]
  timing: "on load"
invariants:
  - "Consistent error/loading UX"
error_handling:
  expected_errors: ["network"]
  retries_timeouts: "ad-hoc retry/prompt"
security:
  authz: "server-side"
  input_validation: "basic client checks"
  sensitive_ops: []
tests: { files: [], coverage_quality: "med" }
similar_or_duplicate_files:
  - "gadgets.js"
  - "prompt.js"
rewrite_notes:
  mapping: "SPA shell (router/store/http)"
  risks: ["implicit globals; coupling"]
  confidence: "med"
```

#### airline-web/public/javascripts/map-button.js

```yaml
file: airline-web/public/javascripts/map-button.js
lang: JavaScript
role: "Map control buttons"
size:
  lines_est: 35
  functions_est: 3
  classes_est: 0
public_api: {}
concurrency: { pattern: "DOM events", shared_state: ["map state"], timing: "on click" }
invariants: []
rewrite_notes:
  mapping: "Map control component"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/map-style.js

```yaml
file: airline-web/public/javascripts/map-style.js
lang: JavaScript
role: "Map theming/layers"
size:
  lines_est: 260
  functions_est: 18
  classes_est: 0
public_api: {}
config:
  env_vars: []
  config_keys: ["map.theme.*"]
  feature_flags: []
concurrency: { pattern: "DOM + map API", shared_state: ["style state"], timing: "on load/toggle" }
invariants:
  - "Legend ↔ layer alignment"
rewrite_notes:
  mapping: "Map style layer"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/mobile.js

```yaml
file: airline-web/public/javascripts/mobile.js
lang: JavaScript
role: "Mobile tweaks"
size:
  lines_est: 18
  functions_est: 2
  classes_est: 0
public_api: {}
concurrency: { pattern: "DOM + resize", shared_state: [], timing: "on load/resize" }
invariants: []
rewrite_notes:
  mapping: "Prefer CSS-first responsiveness"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/model-configuration.js

```yaml
file: airline-web/public/javascripts/model-configuration.js
lang: JavaScript
role: "Cabin/class seat configuration UI"
size:
  lines_est: 420
  functions_est: 30
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/airplane-config/*"] }
concurrency: { pattern: "DOM + XHR", shared_state: ["seat layout state"], timing: "user-driven" }
invariants:
  - "Sum seats ≤ capacity"
rewrite_notes:
  mapping: "Config editor component"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/office.js

```yaml
file: airline-web/public/javascripts/office.js
lang: JavaScript
role: "Corporate/office UI (properties, staff, tasks)"
size:
  lines_est: 1200
  functions_est: 85
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/office/*"] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "user-driven" }
invariants: []
rewrite_notes:
  mapping: "Confirm domain scope first"
  risks: ["unclear domain; large script"]
  confidence: "low"
```

#### airline-web/public/javascripts/oil.js

```yaml
file: airline-web/public/javascripts/oil.js
lang: JavaScript
role: "Oil market/hedging UI"
size:
  lines_est: 310
  functions_est: 24
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/oil/price","/oil/contracts/*"] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "user-driven" }
invariants:
  - "Price ≥ 0"
rewrite_notes:
  mapping: "Economic UI"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/pending-action.js

```yaml
file: airline-web/public/javascripts/pending-action.js
lang: JavaScript
role: "Pending actions/prompts"
size:
  lines_est: 90
  functions_est: 7
  classes_est: 0
public_api: {}
concurrency: { pattern: "DOM updates", shared_state: [], timing: "on load" }
invariants: []
rewrite_notes:
  mapping: "Prompt/notification system"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/plot-fusioncharts.js

```yaml
file: airline-web/public/javascripts/plot-fusioncharts.js
lang: JavaScript
role: "FusionCharts adapter: build series/options"
size:
  lines_est: 1400
  functions_est: 95
  classes_est: 0
public_api: {}
concurrency: { pattern: "chart callbacks", shared_state: [], timing: "on demand" }
invariants:
  - "Series/categories alignment"
rewrite_notes:
  mapping: "Standardize on one chart lib; central adapter"
  risks: ["vendor coupling; config sprawl"]
  confidence: "med"
```

#### airline-web/public/javascripts/plot-highcharts.js

```yaml
file: airline-web/public/javascripts/plot-highcharts.js
lang: JavaScript
role: "Highcharts wrapper"
size:
  lines_est: 28
  functions_est: 2
  classes_est: 0
public_api: {}
concurrency: { pattern: "render on demand", shared_state: [], timing: "on demand" }
invariants: []
rewrite_notes:
  mapping: "Consolidate charting"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/profile.js

```yaml
file: airline-web/public/javascripts/profile.js
lang: JavaScript
role: "Profile page (PII forms, avatar/logo)"
size:
  lines_est: 120
  functions_est: 10
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/profile/*"] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "user-driven" }
invariants:
  - "Sanitize PII"
rewrite_notes:
  mapping: "Identity/profile module"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/prompt.js

```yaml
file: airline-web/public/javascripts/prompt.js
lang: JavaScript
role: "Prompt/notice system"
size:
  lines_est: 330
  functions_est: 26
  classes_est: 0
public_api: {}
concurrency: { pattern: "DOM + timers", shared_state: [], timing: "on demand" }
invariants:
  - "State-driven visibility"
rewrite_notes:
  mapping: "Notifications framework"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/ranking.js

```yaml
file: airline-web/public/javascripts/ranking.js
lang: JavaScript
role: "Leaderboards/rankings"
size:
  lines_est: 160
  functions_est: 12
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/rankings/*"] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "user-driven" }
invariants:
  - "Ordering stability"
rewrite_notes:
  mapping: "Analytics views"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/rivals.js

```yaml
file: airline-web/public/javascripts/rivals.js
lang: JavaScript
role: "Rivals/comparison views"
size:
  lines_est: 520
  functions_est: 36
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/rivals/*","/search/*"] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "user-driven" }
invariants:
  - "Consistent metrics"
rewrite_notes:
  mapping: "Comparison module"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/search.js

```yaml
file: airline-web/public/javascripts/search.js
lang: JavaScript
role: "Search UI for airports/airlines/links"
size:
  lines_est: 1150
  functions_est: 85
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/search/*","/airports/*","/airlines/*","/links/*"] }
concurrency:
  pattern: "debounced input + XHR"
  shared_state: []
  timing: "user-driven"
invariants:
  - "Rate-limit & cap results"
rewrite_notes:
  mapping: "Indexed search + typed client"
  risks: ["heavy DOM on large lists"]
  confidence: "med"
```

#### airline-web/public/javascripts/settings.js

```yaml
file: airline-web/public/javascripts/settings.js
lang: JavaScript
role: "Global settings UI"
size:
  lines_est: 150
  functions_est: 12
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/settings/*"] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "user-driven" }
invariants:
  - "Key uniqueness"
rewrite_notes:
  mapping: "Config UI"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/signup.js

```yaml
file: airline-web/public/javascripts/signup.js
lang: JavaScript
role: "Sign-up form interactions"
size:
  lines_est: 40
  functions_est: 4
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/signup/*"] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "on submit" }
invariants:
  - "Email/password formats"
rewrite_notes:
  mapping: "Identity module"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/test-airport.js

```yaml
file: airline-web/public/javascripts/test-airport.js
lang: JavaScript
role: "Developer/testing hooks for airport"
size:
  lines_est: 150
  functions_est: 12
  classes_est: 0
public_api: {}
concurrency: { pattern: "N/A", shared_state: [], timing: "dev only" }
invariants: []
rewrite_notes:
  mapping: "Remove or guard behind dev flag"
  risks: ["Dev-only exposure"]
  confidence: "high"
```

#### airline-web/public/javascripts/tiles.js

```yaml
file: airline-web/public/javascripts/tiles.js
lang: JavaScript
role: "Dashboard tiles"
size:
  lines_est: 85
  functions_est: 7
  classes_est: 0
public_api: {}
external_io: { http_calls: ["/tiles/*","/stats/*"] }
concurrency: { pattern: "DOM + XHR", shared_state: [], timing: "on load" }
invariants: []
rewrite_notes:
  mapping: "Widget components"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/ui-theme.js

```yaml
file: airline-web/public/javascripts/ui-theme.js
lang: JavaScript
role: "Theme persistence & toggling"
size:
  lines_est: 35
  functions_est: 3
  classes_est: 0
public_api: {}
concurrency: { pattern: "localStorage + DOM", shared_state: ["theme"], timing: "on load/toggle" }
invariants:
  - "Accessible contrast"
rewrite_notes:
  mapping: "Theme provider"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/websocket.js

```yaml
file: airline-web/public/javascripts/websocket.js
lang: JavaScript
role: "WebSocket manager (reconnect, send/receive)"
size:
  lines_est: 110
  functions_est: 10
  classes_est: 0
public_api: {}
external_io:
  message_queues: ["WebSocket (reconnecting-websocket)"]
config:
  env_vars: []
  config_keys: ["websocket.url","websocket.reconnect"]
  feature_flags: []
concurrency:
  pattern: "WS event loop"
  shared_state: ["connection state"]
  timing: "app lifetime"
invariants:
  - "Backoff avoids tight loops"
error_handling:
  expected_errors: ["network/close"]
  retries_timeouts: "reconnect strategy"
security:
  authz: "session upstream"
  input_validation: "N/A"
  sensitive_ops: []
tests: { files: [], coverage_quality: "low" }
rewrite_notes:
  mapping: "Typed RT client SDK"
  risks: ["implicit globals"]
  confidence: "med"
```

#### airline-web/public/javascripts/departure-board/departure-board.css

```yaml
file: airline-web/public/javascripts/departure-board/departure-board.css
lang: CSS
role: "Split-flap board styles"
size:
  lines_est: 95
  functions_est: 0
  classes_est: 0
public_api: {}
concurrency: {}
invariants:
  - "Monospace grid alignment"
rewrite_notes:
  mapping: "Component-scoped styles"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/departure-board/departure-board.js

```yaml
file: airline-web/public/javascripts/departure-board/departure-board.js
lang: JavaScript
role: "Split-flap board animation"
size:
  lines_est: 180
  functions_est: 14
  classes_est: 0
public_api: {}
concurrency:
  pattern: "requestAnimationFrame + DOM"
  shared_state: []
  timing: "periodic updates"
invariants:
  - "Fixed column widths; smooth animation"
rewrite_notes:
  mapping: "Encapsulated UI component"
  risks: ["performance on low-end devices"]
  confidence: "med"
```

#### airline-web/public/javascripts/emojify/emojify.js

```yaml
file: airline-web/public/javascripts/emojify/emojify.js
lang: JavaScript
role: "Emoji parser (shortcodes → images)"
size:
  lines_est: 680
  functions_est: 45
  classes_est: 0
public_api: {}
concurrency:
  pattern: "DOM parse/replace"
  shared_state: []
  timing: "on render"
invariants:
  - "Consistent shortcode mapping"
rewrite_notes:
  mapping: "Prefer Unicode + native fonts"
  risks: ["asset dependency; perf"]
  confidence: "med"
```

#### airline-web/public/javascripts/emojify/emojify.min.css

```yaml
file: airline-web/public/javascripts/emojify/emojify.min.css
lang: CSS
role: "Emoji styles"
size:
  lines_est: 5
  functions_est: 0
  classes_est: 0
public_api: {}
rewrite_notes:
  mapping: "Fold into component styles"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/jemoji/LICENSE

```yaml
file: airline-web/public/javascripts/jemoji/LICENSE
lang: text
role: "License for vendored jemoji"
size:
  lines_est: 40
  functions_est: 0
  classes_est: 0
public_api: {}
rewrite_notes:
  mapping: "Ensure license compliance"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/jemoji/README.md

```yaml
file: airline-web/public/javascripts/jemoji/README.md
lang: Markdown
role: "jemoji docs"
size:
  lines_est: 250
  functions_est: 0
  classes_est: 0
public_api: {}
rewrite_notes:
  mapping: "Prefer managed dependency"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/jemoji/jemoji.css

```yaml
file: airline-web/public/javascripts/jemoji/jemoji.css
lang: CSS
role: "Emoji display styles"
size:
  lines_est: 120
  functions_est: 0
  classes_est: 0
public_api: {}
rewrite_notes:
  mapping: "Component CSS"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/jemoji/jemoji.js

```yaml
file: airline-web/public/javascripts/jemoji/jemoji.js
lang: JavaScript
role: "Emoji replacement/render runtime"
size:
  lines_est: 780
  functions_est: 52
  classes_est: 0
public_api: {}
concurrency: { pattern: "DOM parsing", shared_state: [], timing: "on render" }
rewrite_notes:
  mapping: "Use native emoji or webfont"
  risks: ["vendored drift"]
  confidence: "med"
```

#### airline-web/public/javascripts/jemoji/package.json

```yaml
file: airline-web/public/javascripts/jemoji/package.json
lang: JSON
role: "jemoji metadata"
size:
  lines_est: 30
  functions_est: 0
  classes_est: 0
public_api: {}
rewrite_notes:
  mapping: "Remove vendored copy; use package manager"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/lazysizes/lazysizes.min.js

```yaml
file: airline-web/public/javascripts/lazysizes/lazysizes.min.js
lang: JavaScript
role: "Lazy-load images"
size:
  lines_est: 220
  functions_est: 20
  classes_est: 0
public_api: {}
concurrency:
  pattern: "IntersectionObserver/fallback"
  shared_state: []
  timing: "on scroll"
rewrite_notes:
  mapping: "Prefer native loading='lazy' where possible"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/libs/highcharts.js

```yaml
file: airline-web/public/javascripts/libs/highcharts.js
lang: JavaScript
role: "Highcharts library (vendored)"
size:
  lines_est: 4200
  functions_est: 200
  classes_est: 0
public_api: {}
rewrite_notes:
  mapping: "Managed dependency; evaluate alternatives"
  risks: ["bundle size; license"]
  confidence: "med"
```

#### airline-web/public/javascripts/libs/jquery-2.1.4.js

```yaml
file: airline-web/public/javascripts/libs/jquery-2.1.4.js
lang: JavaScript
role: "jQuery (legacy) vendored"
size:
  lines_est: 6900
  functions_est: 350
  classes_est: 0
public_api: {}
rewrite_notes:
  mapping: "Remove jQuery; migrate to framework components"
  risks: ["widespread usage patterns"]
  confidence: "med"
```

#### airline-web/public/javascripts/libs/jquery-ui.min.js

```yaml
file: airline-web/public/javascripts/libs/jquery-ui.min.js
lang: JavaScript
role: "jQuery UI components (vendored)"
size:
  lines_est: 5600
  functions_est: 180
  classes_est: 0
public_api: {}
rewrite_notes:
  mapping: "Replace with accessible component library"
  risks: ["accessibility and styling gaps"]
  confidence: "med"
```

#### airline-web/public/javascripts/libs/jquery.cookie.js

```yaml
file: airline-web/public/javascripts/libs/jquery.cookie.js
lang: JavaScript
role: "Cookie helper"
size:
  lines_est: 85
  functions_est: 6
  classes_est: 0
public_api: {}
rewrite_notes:
  mapping: "Use modern cookie utilities"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/libs/reconnecting-websocket.min.js

```yaml
file: airline-web/public/javascripts/libs/reconnecting-websocket.min.js
lang: JavaScript
role: "WebSocket reconnect wrapper"
size:
  lines_est: 420
  functions_est: 28
  classes_est: 0
public_api: {}
rewrite_notes:
  mapping: "Use maintained package"
  risks: []
  confidence: "high"
```

#### airline-web/public/javascripts/splitting (folder)

```yaml
file: airline-web/public/javascripts/splitting
lang: JS/CSS (assumed)
role: "Minor assets/utilities (not enumerated)"
size:
  lines_est: 0
  functions_est: 0
  classes_est: 0
public_api: {}
rewrite_notes:
  mapping: "Revisit if needed during UI rewrite"
  risks: ["Unscanned due to API limits"]
  confidence: "low"
```

### File entries (notes)
- Vendor/minified libraries are referenced but should be externalized in the rewrite; keep their presence documented for parity.
- Several modules rely on DOM id/class conventions; when rebuilding as SPA components, map these to component props and state.

## Rollup for airline-web\public\javascripts

**Key responsibilities (top 5):**
1. Drive all front-end feature flows via REST (airlines, fleet, airports/assets, alliances, campaigns, loans/oil, events).
2. Provide real-time chat/broadcast UX using WebSockets and emoji rendering.
3. Render analytics (charts, heatmaps, link histories) through charting libraries.
4. Compose UI widgets, prompts, theming, and map styling for cohesive interaction patterns.
5. Bundle and glue vendor libraries supporting legacy DOM-centric patterns.

**Cross-module dependencies (top 10 by frequency):**
- airline-rest endpoints → invoked by most modules for data fetch/mutation.
- WebSocket actors (airline-web backend) → chat.js/websocket.js consume live events.
- Chart libs (Highcharts, FusionCharts) → used by plot-* and link-history.js for analytics.
- Emoji libs (emojify/jemoji) → message rendering enrichment.
- jQuery/jQuery UI → pervasive DOM manipulation and widgets.
- Heatmap/tiles/airport → depend on stats routes and map styling.
- Admin/settings/log → draw from configuration and logs endpoints.
- Prompt/gadgets/main → shared UX primitives (toasts, modals, loaders).
- Asset/images/stylesheets → referenced for icons/backgrounds in UI.
- Session/auth flows (profile/signup/account) → integrated across modules.

**High-leverage files to study first (top 10):**
- public/javascripts/main.js — global bootstrapping and shared HTTP/error handling.
- public/javascripts/airline.js — core airline operations and UI invariants.
- public/javascripts/airplane.js — fleet lifecycle and assignment rules.
- public/javascripts/link-history.js — time-series and chart data shapes.
- public/javascripts/search.js — performance and rate-limiting/UX patterns.
- public/javascripts/plot-fusioncharts.js — heavy chart adapter to replace/standardize.
- public/javascripts/websocket.js — reconnection policy and message handling semantics.
- public/javascripts/chat.js — real-time UX, emoji pipeline, and rate-limits.
- public/javascripts/airport.js — mapping overlays and stats integration.
- public/javascripts/alliance.js — membership and governance flows.

**Mermaid: high-level dependency sketch**
```mermaid
graph TD
  JS[public/javascripts] --> REST[airline-rest APIs]
  JS --> WS[WebSocket (chat/broadcast)]
  JS --> Charts[Highcharts/FusionCharts]
  JS --> Emoji[emojify/jemoji]
  REST --> DB[(DB via airline-data)]
```