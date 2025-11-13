<!-- CATALOG:PATH="airline-web\public\html" SLUG="airline-web__public__html" -->

# Repo Catalogue — airline-web\public\html

*Slug:* `airline-web__public__html`  
*Commit:* `6160503`  
*Generated:* `2025-11-13T01:02:13Z`

**Summary (2–4 sentences):** Static HTML fragments/templates used by the legacy UI for announcements, notices, tooltips, and the in-app tutorial. These files are likely fetched or inlined by client-side scripts to render consistent UI blocks (e.g., modal content, popovers, help overlays) without server-side rendering. They encapsulate UX copy and structure, serving as content sources for modules such as prompts/notifications and onboarding.

**Contents overview**
- Files: 4  |  Subfolders: 1  |  Languages: HTML (100%)
- Notable responsibilities:
  - Provide reusable HTML snippets for prompt/notice/announcement display.
  - Host the in-app tutorial content (steps, sections) as a static page fragment.
  - Supply tooltip content for specific concepts (e.g., capital gains/losses).
  - Decouple UX copy/layout from controller code and JS logic.

### File entries

#### airline-web/public/html/announcement.html

```yaml
file: airline-web/public/html/announcement.html
lang: HTML
role: "Announcement banner/modal content snippet used by the UI"
size:
  lines_est: 50
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
config: { env_vars: [], config_keys: [], feature_flags: [] }
concurrency:
  pattern: "N/A (static content)"
  shared_state: []
  timing: "Included or fetched by client when needed"
invariants:
  - "Semantics suitable for screen readers (headings/landmarks)"
  - "No inline script reliance"
error_handling: {}
security:
  authz: "N/A"
  input_validation: "N/A"
  sensitive_ops: []
tests:
  files: []
  coverage_quality: "low"
  golden_seeds: []
similar_or_duplicate_files:
  - "airline-web/public/html/notice.html"
rewrite_notes:
  mapping: "Map to a notification/announcement component with i18n strings"
  risks: ["Hard-coded copy; lack of i18n"]
  confidence: "high"
```

#### airline-web/public/html/notice.html

```yaml
file: airline-web/public/html/notice.html
lang: HTML
role: "Generic notice template for inline prompts/banners"
size:
  lines_est: 45
  functions_est: 0
  classes_est: 0
public_api: { routes: [], exports: [] }
data_model: { tables_read: [], tables_written: [], migrations: [], entities: [] }
queries: { sql: [], orm_calls: [] }
external_io: {}
config: {}
concurrency: { pattern: "N/A (static content)", shared_state: [], timing: "used by prompt system" }
invariants:
  - "Clear style hooks (classes/ids) for JS/CSS to target"
error_handling: {}
security: { authz: "N/A", input_validation: "N/A", sensitive_ops: [] }
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files:
  - "airline-web/public/html/announcement.html"
rewrite_notes:
  mapping: "Notification component with variants (info/warn/error/success) and i18n"
  risks: ["Copy duplication with announcement"]
  confidence: "high"
```

#### airline-web/public/html/tutorial.html

```yaml
file: airline-web/public/html/tutorial.html
lang: HTML
role: "In-app tutorial content and structure (multi-section onboarding/help)"
size:
  lines_est: 500
  functions_est: 0
  classes_est: 0
public_api: { routes: [], exports: [] }
data_model: { tables_read: [], tables_written: [], migrations: [], entities: [] }
queries: { sql: [], orm_calls: [] }
external_io: {}
config: { env_vars: [], config_keys: [], feature_flags: [] }
concurrency:
  pattern: "N/A (static content)"
  shared_state: []
  timing: "Displayed on tutorial routes or overlays"
invariants:
  - "Headings and anchors reflect a logical navigation structure"
  - "Images/assets referenced exist and are lazy-loaded if heavy"
error_handling: {}
security: { authz: "N/A", input_validation: "N/A", sensitive_ops: [] }
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Port to a guided tour/help center with i18n, per-step telemetry, and accessibility"
  risks: ["Large static copy; hard to maintain and localize"]
  confidence: "med"
```

#### airline-web/public/html/tooltip/capital-gain-loss.html

```yaml
file: airline-web/public/html/tooltip/capital-gain-loss.html
lang: HTML
role: "Tooltip content explaining capital gain/loss concepts"
size:
  lines_est: 18
  functions_est: 0
  classes_est: 0
public_api: { routes: [], exports: [] }
data_model: { tables_read: [], tables_written: [], migrations: [], entities: [] }
queries: { sql: [], orm_calls: [] }
external_io: {}
config: {}
concurrency:
  pattern: "N/A (static tooltip)"
  shared_state: []
  timing: "Injected by tooltip UI on hover/focus"
invariants:
  - "Short, scannable, accessible"
error_handling: {}
security: { authz: "N/A", input_validation: "N/A", sensitive_ops: [] }
tests: { files: [], coverage_quality: "low" }
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Use a tooltip component with ARIA, keyboard support, and i18n"
  risks: ["Static copy drift; duplication if similar tooltips proliferate"]
  confidence: "high"
```

## Rollup for airline-web\public\html

**Key responsibilities (top 5):**
1. Supply static HTML snippets for announcements, notices, and tooltips to keep UI copy/layout centralized.
2. Provide a comprehensive tutorial/help content block for onboarding and reference.
3. Serve content for client-side modules to inject into modals/popovers/overlays without server rendering.
4. Ensure consistent structure and hooks for styling and JS behavior across the app.
5. Act as the default source of truth for UX copy related to announcements, notices, and specific tooltips.

**Cross-module dependencies (top 10 by frequency):**
- airline-web/public/javascripts/prompt.js — displays notices/announcements using these snippets.
- airline-web/public/javascripts/gadgets.js — modal/popup/tooltip scaffolding leveraging these HTML fragments.
- airline-web/app/controllers/TutorialApplication.scala — routes/pages that surface tutorial content.
- airline-web/public/stylesheets/* — styles applied to blocks defined here (about, prompt, tooltip).
- airline-web/app/controllers/NoticeApplication.scala / AlertApplication.scala — systems that drive content shown in notice/announcement UIs.
- airline-web/public/images/tutorial/* — assets referenced by tutorial.html.
- airline-web/public/stylesheets/tooltip.css — visual treatment for tooltip content.
- airline-web/public/javascripts/search.js / airline.js — may surface inline tooltips or notices via these templates.
- airline-web/public/javascripts/main.js — global handlers that may load/insert announcement/notice fragments.
- airline-web/public/javascripts/chat.js — could reuse notice UI for status banners (assumed).

**High-leverage files to study first (top 10):**
- public/html/tutorial.html — scope and structure of onboarding/help to preserve and redesign.
- public/html/announcement.html — canonical announcement layout for future components.
- public/html/notice.html — base notice template that should become a reusable component.
- public/html/tooltip/capital-gain-loss.html — example tooltip content; define a pattern and i18n strategy for similar definitions.
