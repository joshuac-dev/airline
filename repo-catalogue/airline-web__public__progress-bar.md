<!-- CATALOG:PATH="airline-web\public\progress-bar" SLUG="airline-web__public__progress-bar" -->

# Repo Catalogue — airline-web\public\progress-bar

*Slug:* `airline-web__public__progress-bar`  
*Commit:* n/a  
*Generated:* 2025-11-13T00:41:27Z

**Summary (2–4 sentences):** Static front-end asset bundle for a reusable progress bar component used across the airline-web UI. The CSS defines the look-and-feel (shape, colors, animations, themes), while the JavaScript provides imperative APIs to create, update, and complete/destroy progress bars within pages. There are no backend calls; the script manipulates DOM elements (likely with minimal or no dependencies) to reflect incremental progress during long-running operations.

**Contents overview**
- Files: 2  |  Subfolders: 0  |  Languages: CSS (~88%), JavaScript (~12%)
- Notable responsibilities:
  - Style definitions for progress bars (container, bar fill, stripes/animation, labels).
  - A small JavaScript helper for progress bar lifecycle (init/update/complete, optional message updates).
  - No server interaction; used by controllers/views to give UI feedback during async tasks.

### File entries

#### airline-web/public/progress-bar/progress-bar.css

```yaml
file: airline-web/public/progress-bar/progress-bar.css
lang: CSS
role: "Styles for the progress bar component (layout, colors, animations)"
size:
  lines_est: 350
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
  pattern: "N/A (static stylesheet)"
  shared_state: []
  timing: "applies on page load"
invariants:
  - "Progress bar fill width and label remain visually aligned"
  - "Animation performs smoothly (uses CSS transitions/animations)"
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
  mapping: "Convert to component-scoped styles in the new front-end (CSS modules/Tailwind/Styled Components)"
  risks: ["Global CSS selectors may collide with other components"]
  confidence: "high"
```

#### airline-web/public/progress-bar/progress-bar.js

```yaml
file: airline-web/public/progress-bar/progress-bar.js
lang: JavaScript
role: "Imperative helper to create/update progress bar elements in the page"
size:
  lines_est: 120
  functions_est: 6
  classes_est: 0
public_api:
  routes: []
  exports:
    - name: createProgressBar
      kind: fn
      summary: "Insert a progress bar into a target container with options (label, initial percent)"
    - name: setProgress
      kind: fn
      summary: "Update the bar to a given percent (0–100), optionally updating label"
    - name: complete
      kind: fn
      summary: "Mark as complete, set to 100%, and optionally remove/hide after a delay"
    - name: destroy
      kind: fn
      summary: "Remove the bar and free DOM references"
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
  config_keys: ["progressBar.defaults.*? (assumed options like duration/labels)"]
  feature_flags: []
concurrency:
  pattern: "Single-threaded DOM updates (browser event loop)"
  shared_state: ["In-memory references to created DOM nodes (per bar instance)"]
  timing: "On-demand during long-running UI operations (uploads, computations)"
invariants:
  - "Percent clamped to [0, 100]"
  - "DOM exists before updates; missing nodes are no-ops"
error_handling:
  expected_errors: ["Missing container selector", "Invalid percent values"]
  retries_timeouts: "N/A (caller re-invokes on next UI tick)"
security:
  authz: "N/A"
  input_validation: "Clamp and validate numeric inputs"
  sensitive_ops: []
tests:
  files: []
  coverage_quality: "low"
  golden_seeds: []
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Wrap as a UI component (e.g., React/Vue/Svelte) with props for value, label, and status; expose typed events"
  risks: ["Direct DOM manipulation outside a framework may conflict with virtual DOM tools later"]
  confidence: "med"
```

Example usage (illustrative):
```html
<div id="upload-status"></div>
<script>
  // initialize
  const pb = createProgressBar('#upload-status', { label: 'Uploading…', percent: 0 });

  // update over time
  let pct = 0;
  const timer = setInterval(() => {
    pct += 10;
    pb.setProgress(pct, `Uploading… ${pct}%`);
    if (pct >= 100) {
      clearInterval(timer);
      pb.complete({ autoHideMs: 800 });
    }
  }, 300);
</script>
```

## Rollup for airline-web\public\progress-bar

**Key responsibilities (top 5):**
1. Provide a consistent, reusable progress bar visual for long-running UI tasks.
2. Offer a minimal imperative API to create, update, and complete/destroy progress indicators.
3. Encapsulate CSS animations and transitions for a smooth progress experience.
4. Avoid server coupling; pure client-side behavior that integrates with existing pages.
5. Serve as a building block for future componentized UI rewrites.

**Cross-module dependencies (top 10 by frequency):**
- airline-web/app/views — templates include/initialize this asset to show progress during operations.
- airline-web/app/controllers — endpoints that kick off long tasks may instruct the UI to show a progress bar.
- airline-web/app/websocket — real-time status streams can drive setProgress updates (indirect).
- None (no direct code imports from back-end; static assets only).

**High-leverage files to study first (top 10):**
- airline-web/public/progress-bar/progress-bar.js — define the future component API surface (props/events).
- airline-web/public/progress-bar/progress-bar.css — identify classes/variables to replicate in the design system.
