<!-- CATALOG:PATH="airline-web\app\views" SLUG="airline-web__app__views" -->

# Repo Catalogue — airline-web\app\views

*Slug:* `airline-web__app__views`  
*Commit:* `6160503`  
*Generated:* `2025-11-13T00:31:43Z`

**Summary (2–4 sentences):** Play Framework Twirl view templates that render the web UI pages for the airline application. This includes the main single-page interface (index.scala.html), authentication flows (signup, password reset, forgot ID), auxiliary pages (about, summary, test), and a custom form field constructor. These templates embed HTML/CSS/JS, invoke Play helpers, and likely bootstrap client scripts that call airline-rest endpoints and open websockets for live updates.

**Contents overview**
- Files: 10  |  Subfolders: 0  |  Languages: Twirl/Scala HTML (~100%)
- Notable responsibilities:
  - Render the primary front-end UI and initialize client-side behavior.
  - Provide server-rendered pages for user onboarding and credential recovery.
  - Define a reusable field constructor for consistent form rendering and validation display.
  - Structure static layout and placeholders that client-side code fills via REST/WebSocket.

### File entries

#### airline-web/app/views/about.scala.html
```yaml
file: airline-web/app/views/about.scala.html
lang: Twirl (Scala HTML)
role: "About page; information, credits, links, version notes"
size:
  lines_est: 180
  functions_est: 0
  classes_est: 0
public_api:
  routes: []
  exports:
    - name: about.scala.html
      kind: template
      summary: "Renders About/Info content"
data_model:
  tables_read: []
  tables_written: []
  migrations: []
  entities: []
queries: { sql: [], orm_calls: [] }
external_io:
  http_calls: []
  message_queues: []
  files_read: []
  files_written: []
config: { env_vars: [], config_keys: [], feature_flags: [] }
concurrency:
  pattern: "Server-side render on request"
  shared_state: []
  timing: "on page request"
invariants:
  - "Static content or minimal dynamic includes"
error_handling: { expected_errors: [], retries_timeouts: "N/A" }
security: { authz: "none", input_validation: "N/A", sensitive_ops: [] }
tests: { files: [], coverage_quality: "low", golden_seeds: [] }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Static content page in new web stack (e.g., SPA route)"
  risks: ["None"]
  confidence: "high"
```

#### airline-web/app/views/checkEmail.scala.html
```yaml
file: airline-web/app/views/checkEmail.scala.html
lang: Twirl (Scala HTML)
role: "Prompt page instructing user to check email for a verification/reset link"
size:
  lines_est: 45
  functions_est: 0
  classes_est: 0
public_api: { routes: [], exports: [{name: checkEmail.scala.html, kind: template, summary: "Renders confirmation to check inbox"}] }
data_model: { tables_read: [], tables_written: [], migrations: [], entities: [] }
queries: { sql: [], orm_calls: [] }
external_io: { http_calls: [], message_queues: [], files_read: [], files_written: [] }
config: { env_vars: [], config_keys: [], feature_flags: [] }
concurrency: { pattern: "Render on request", shared_state: [], timing: "auth flow" }
invariants:
  - "Displays target email or contextual message from server"
error_handling: { expected_errors: [], retries_timeouts: "N/A" }
security: { authz: "none", input_validation: "server sets message content", sensitive_ops: [] }
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files:
  - "forgotPassword.scala.html"
rewrite_notes:
  mapping: "SPA route after action (email sent); use toast/alert"
  risks: []
  confidence: "high"
```

#### airline-web/app/views/forgotId.scala.html
```yaml
file: airline-web/app/views/forgotId.scala.html
lang: Twirl (Scala HTML)
role: "Form/page for retrieving forgotten user ID"
size:
  lines_est: 70
  functions_est: 0
  classes_est: 0
public_api: { routes: [], exports: [{name: forgotId.scala.html, kind: template, summary: "Form to request user ID reminder"}] }
data_model: { tables_read: [], tables_written: [], migrations: [], entities: [] }
queries: { sql: [], orm_calls: [] }
external_io: { http_calls: [], message_queues: [], files_read: [], files_written: [] }
config: { env_vars: [], config_keys: [], feature_flags: [] }
concurrency: { pattern: "Render on request; POST roundtrip via controller", shared_state: [], timing: "auth flow" }
invariants:
  - "Validates email or recovery info client/server side"
error_handling: { expected_errors: ["Invalid input"], retries_timeouts: "N/A" }
security: { authz: "none", input_validation: "Play form helpers", sensitive_ops: ["email address (PII)"] }
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files:
  - "forgotPassword.scala.html"
rewrite_notes:
  mapping: "Consolidate recovery flows; use API + front-end form"
  risks: ["PII exposure in messages"]
  confidence: "med"
```

#### airline-web/app/views/forgotPassword.scala.html
```yaml
file: airline-web/app/views/forgotPassword.scala.html
lang: Twirl (Scala HTML)
role: "Form/page to initiate password reset (request reset email)"
size:
  lines_est: 85
  functions_est: 0
  classes_est: 0
public_api: { routes: [], exports: [{name: forgotPassword.scala.html, kind: template, summary: "Render reset request form"}] }
data_model: { tables_read: [], tables_written: [], migrations: [], entities: [] }
queries: { sql: [], orm_calls: [] }
external_io: { http_calls: [], message_queues: [], files_read: [], files_written: [] }
config: { env_vars: [], config_keys: [], feature_flags: [] }
concurrency: { pattern: "Render + submit to controller", shared_state: [], timing: "auth flow" }
invariants:
  - "Email format validated"
error_handling: { expected_errors: ["Invalid email","Rate limit exceeded"], retries_timeouts: "N/A" }
security: { authz: "none", input_validation: "Play form helpers", sensitive_ops: ["PII entry"] }
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files:
  - "passwordReset.scala.html"
rewrite_notes:
  mapping: "Use API endpoint + front-end validation; add rate-limit UX"
  risks: ["Brute-force if not rate-limited"]
  confidence: "med"
```

#### airline-web/app/views/index.scala.html
```yaml
file: airline-web/app/views/index.scala.html
lang: Twirl (Scala HTML)
role: "Main application page; loads the primary UI (maps, dashboards, interactions)"
size:
  lines_est: 6000
  functions_est: 0
  classes_est: 0
public_api:
  routes: []
  exports:
    - name: index.scala.html
      kind: template
      summary: "Bootstrap page for the client app; includes scripts, styles, and placeholders"
data_model: { tables_read: [], tables_written: [], migrations: [], entities: [] }
queries: { sql: [], orm_calls: [] }
external_io:
  http_calls: ["airline-rest endpoints (via embedded scripts)"]
  message_queues: []
  files_read: []
  files_written: []
config:
  env_vars: []
  config_keys: ["front-end feature toggles embedded at render?"]
  feature_flags: []
concurrency:
  pattern: "Server renders static shell; client JS drives async interactions"
  shared_state: []
  timing: "on page load; then client polls/streams"
invariants:
  - "DOM placeholders match client JS expectations"
  - "Asset paths correct; CORS/WebSocket endpoints reachable"
error_handling:
  expected_errors: ["Missing assets","JS runtime failures (client)"]
  retries_timeouts: "Client JS handles retries/backoff"
security:
  authz: "likely session-based (upstream middleware)"
  input_validation: "N/A (mostly output)"
  sensitive_ops: []
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Rebuild as SPA shell (React/Vue/Svelte) with typed API clients and WebSocket/SSE"
  risks: ["Large template with embedded logic; tight coupling to legacy assets/IDs"]
  confidence: "med"
```

#### airline-web/app/views/myFieldConstructorTemplate.scala.html
```yaml
file: airline-web/app/views/myFieldConstructorTemplate.scala.html
lang: Twirl (Scala HTML)
role: "Custom Play field constructor for form inputs (layout, error display)"
size:
  lines_est: 15
  functions_est: 0
  classes_est: 0
public_api: { routes: [], exports: [{name: myFieldConstructorTemplate.scala.html, kind: template, summary: "Reusable field renderer"}] }
data_model: { tables_read: [], tables_written: [], migrations: [], entities: [] }
queries: { sql: [], orm_calls: [] }
external_io: { http_calls: [], message_queues: [], files_read: [], files_written: [] }
config: { env_vars: [], config_keys: [], feature_flags: [] }
concurrency: { pattern: "N/A", shared_state: [], timing: "render-time helper" }
invariants:
  - "Displays validation errors consistently"
error_handling: { expected_errors: [], retries_timeouts: "N/A" }
security: { authz: "N/A", input_validation: "N/A", sensitive_ops: [] }
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Componentize as front-end form field with validation states"
  risks: ["Legacy Play helper specifics"]
  confidence: "high"
```

#### airline-web/app/views/passwordReset.scala.html
```yaml
file: airline-web/app/views/passwordReset.scala.html
lang: Twirl (Scala HTML)
role: "Password reset form page (token-driven) to set a new password"
size:
  lines_est: 90
  functions_est: 0
  classes_est: 0
public_api: { routes: [], exports: [{name: passwordReset.scala.html, kind: template, summary: "New password entry form"}] }
data_model: { tables_read: [], tables_written: [], migrations: [], entities: [] }
queries: { sql: [], orm_calls: [] }
external_io: { http_calls: [], message_queues: [], files_read: [], files_written: [] }
config: { env_vars: [], config_keys: [], feature_flags: [] }
concurrency: { pattern: "Render + submit to controller", shared_state: [], timing: "auth flow" }
invariants:
  - "Password complexity rules surfaced"
error_handling: { expected_errors: ["Invalid/expired token","Weak password"], retries_timeouts: "N/A" }
security: { authz: "token-gated (server validates)", input_validation: "server & client side", sensitive_ops: ["credential handling"] }
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files:
  - "forgotPassword.scala.html"
rewrite_notes:
  mapping: "Modern reset flow with API + frontend; include strength meter"
  risks: ["Token exposure if logged"]
  confidence: "med"
```

#### airline-web/app/views/signup.scala.html
```yaml
file: airline-web/app/views/signup.scala.html
lang: Twirl (Scala HTML)
role: "User registration page"
size:
  lines_est: 120
  functions_est: 0
  classes_est: 0
public_api: { routes: [], exports: [{name: signup.scala.html, kind: template, summary: "Signup form and UX"}] }
data_model: { tables_read: [], tables_written: [], migrations: [], entities: [] }
queries: { sql: [], orm_calls: [] }
external_io: { http_calls: [], message_queues: [], files_read: [], files_written: [] }
config: { env_vars: [], config_keys: [], feature_flags: [] }
concurrency: { pattern: "Render + submit to controller", shared_state: [], timing: "onboarding flow" }
invariants:
  - "Required fields enforced"
  - "Terms/privacy acknowledgment captured"
error_handling: { expected_errors: ["Duplicate username","Weak password"], retries_timeouts: "N/A" }
security: { authz: "none", input_validation: "Play forms + server", sensitive_ops: ["PII collection"] }
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Implement as SPA form; server validation via API; CAPTCHA/rate-limit"
  risks: ["Spam signups without protections"]
  confidence: "med"
```

#### airline-web/app/views/summary.scala.html
```yaml
file: airline-web/app/views/summary.scala.html
lang: Twirl (Scala HTML)
role: "Lightweight summary page (e.g., status/overview)"
size:
  lines_est: 10
  functions_est: 0
  classes_est: 0
public_api: { routes: [], exports: [{name: summary.scala.html, kind: template, summary: "Short summary view"}] }
data_model: { tables_read: [], tables_written: [], migrations: [], entities: [] }
queries: { sql: [], orm_calls: [] }
external_io: { http_calls: [], message_queues: [], files_read: [], files_written: [] }
config: { env_vars: [], config_keys: [], feature_flags: [] }
concurrency: { pattern: "Render on request", shared_state: [], timing: "N/A" }
invariants: []
error_handling: { expected_errors: [], retries_timeouts: "N/A" }
security: { authz: "none", input_validation: "N/A", sensitive_ops: [] }
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Optional SPA route; fetch via REST"
  risks: []
  confidence: "high"
```

#### airline-web/app/views/test.scala.html
```yaml
file: airline-web/app/views/test.scala.html
lang: Twirl (Scala HTML)
role: "Test/demo page (development only)"
size:
  lines_est: 75
  functions_est: 0
  classes_est: 0
public_api: { routes: [], exports: [{name: test.scala.html, kind: template, summary: "Developer testing view"}] }
data_model: { tables_read: [], tables_written: [], migrations: [], entities: [] }
queries: { sql: [], orm_calls: [] }
external_io: { http_calls: [], message_queues: [], files_read: [], files_written: [] }
config: { env_vars: [], config_keys: [], feature_flags: [] }
concurrency: { pattern: "Render on request", shared_state: [], timing: "dev usage" }
invariants: []
error_handling: { expected_errors: [], retries_timeouts: "N/A" }
security: { authz: "dev-only (should be protected)", input_validation: "N/A", sensitive_ops: [] }
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Remove or gate in production; replace with Storybook/docs"
  risks: ["Exposing dev pages publicly"]
  confidence: "high"
```

### File entries (notes)
- The Twirl templates likely embed links to REST and WebSocket endpoints; confirm exact endpoints in airline-rest/conf/routes and airline-web controllers.
- The index page is very large and may contain inline JS; in the rewrite, shift to modular front-end assets and typed API clients.

## Rollup for airline-web\app\views

**Key responsibilities (top 5):**
1. Render the main application shell and static pages for the web UI.
2. Provide server-rendered views for user registration and credential recovery flows.
3. Define consistent form rendering via a custom field constructor template.
4. Bootstrap client-side logic (scripts, initial state) for dynamic behavior that calls the REST/WebSocket backends.
5. Present auxiliary informational pages (about, summary) and dev/test views.

**Cross-module dependencies (top 10 by frequency):**
- airline-rest controllers/routes — endpoints consumed by client code initialized here.
- airline-web websocket actors — real-time features launched from the main index page.
- Play Framework helpers (forms, field constructors) — used in auth/recovery views.
- Application configuration — may inject feature flags or env-dependent values into templates.
- Authentication/session middleware — governs access to main UI and forms (outside these files).

**High-leverage files to study first (top 10):**
- airline-web/app/views/index.scala.html — central UI shell; understand embedded JS and endpoint expectations.
- airline-web/app/views/signup.scala.html — onboarding constraints and validation messaging.
- airline-web/app/views/passwordReset.scala.html — credential reset UX and constraints.
- airline-web/app/views/forgotPassword.scala.html — reset initiation flow (rate limiting).
- airline-web/app/views/myFieldConstructorTemplate.scala.html — form error display conventions.
- airline-web/app/views/about.scala.html — statements of scope, credits, links (user guidance).
- airline-web/app/views/forgotId.scala.html — user ID recovery edge cases.
- airline-web/app/views/checkEmail.scala.html — notification UX after email actions.
- airline-web/app/views/summary.scala.html — example of lightweight pages for quick porting.
- airline-web/app/views/test.scala.html — review and remove or restrict.
