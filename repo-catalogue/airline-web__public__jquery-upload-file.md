<!-- CATALOG:PATH="airline-web\public\jquery-upload-file" SLUG="airline-web__public__jquery-upload-file" -->

# Repo Catalogue — airline-web\public\jquery-upload-file

*Slug:* `airline-web__public__jquery-upload-file`  
*Commit:* `6160503`  
*Generated:* `2025-11-13T01:00:09Z`

**Summary (2–4 sentences):** Vendored client-side assets for the jQuery Upload File plugin used to render and manage AJAX file uploads in the legacy UI. Provides a DOM widget with drag-and-drop support, multiple file selection, progress display, and callbacks for success/error. This is a browser-only component; all validation and persistence are performed by server endpoints the widget posts to.

**Contents overview**
- Files: 2  |  Subfolders: 0  |  Languages: JavaScript (~88%), CSS (~12%)
- Notable responsibilities:
  - Present a reusable upload UI component with progress and cancel controls.
  - Configure client-side constraints (file types, sizes, counts) and endpoints.
  - Emit XHR POSTs to server upload endpoints and invoke callbacks on completion/failure.

### File entries

#### airline-web/public/jquery-upload-file/jquery.uploadfile.min.js

```yaml
file: airline-web/public/jquery-upload-file/jquery.uploadfile.min.js
lang: JavaScript (minified, vendored)
role: "jQuery Upload File plugin providing AJAX multi-file uploads with progress and callbacks"
size:
  lines_est: 400
  functions_est: 20
  classes_est: 0
public_api:
  routes: []
  exports:
    - name: $.fn.uploadFile
      kind: fn
      summary: "Initializes an upload widget on a selected element with options (url, fileName, multiple, allowedTypes, formData, onSuccess, onError, onProgress)."
data_model:
  tables_read: []
  tables_written: []
  migrations: []
  entities: []
queries:
  sql: []
  orm_calls: []
external_io:
  http_calls:
    - "POST <configured url> — multipart/form-data with selected file(s) and optional formData"
  message_queues: []
  files_read: []
  files_written: []
config:
  env_vars: []
  config_keys:
    - "options.url"
    - "options.fileName"
    - "options.multiple"
    - "options.allowedTypes"
    - "options.maxFileSize"
    - "options.maxFileCount"
    - "options.dragDrop"
    - "options.sequential"
    - "options.onSubmit/onSuccess/onError/onProgress"
  feature_flags: []
concurrency:
  pattern: "Browser event loop + XHR; optional sequential queueing"
  shared_state: ["Widget instance state per DOM element"]
  timing: "User-initiated; progress callbacks during upload"
invariants:
  - "Percent progress reported within [0, 100]"
  - "Client constraints (types/sizes/counts) enforced before issuing POST"
  - "Sequential mode ensures one in-flight upload per queue"
error_handling:
  expected_errors: ["Network failure", "Server 4xx/5xx", "Client-side validation failure (type/size/count)"]
  retries_timeouts: "No built-in automatic retry (typical) — onError callback available"
security:
  authz: "Server-side; client cannot enforce ownership"
  input_validation: "Client-only preflight; MUST be duplicated on server"
  sensitive_ops: ["File content upload; potential PII/media"]
tests:
  files: []
  coverage_quality: "low"
  golden_seeds: []
similar_or_duplicate_files: []
rewrite_notes:
  mapping: "Replace with a modern component (React/Vue/Svelte) using Fetch/XMLHttpRequest, drag-and-drop, and chunked uploads as needed; prefer signed URLs (e.g., S3) and server-side virus scanning."
  risks:
    - "Reliance on jQuery and a minified vendor copy complicates maintenance"
    - "Client-side-only validation; missing CSRF/size/type checks server-side would be unsafe"
    - "No standardized progress/error telemetry"
  confidence: "med"
```

Example usage pattern (illustrative):
```js
$("#fileuploader").uploadFile({
  url: "/api/upload/logo",
  fileName: "file",
  multiple: true,
  allowedTypes: "png,jpg,jpeg,svg",
  maxFileSize: 5 * 1024 * 1024,
  onSuccess: (files, response, xhr, pd) => {/* refresh UI */},
  onError: (files, status, errMsg, pd) => {/* show error */},
});
```

#### airline-web/public/jquery-upload-file/uploadfile.css

```yaml
file: airline-web/public/jquery-upload-file/uploadfile.css
lang: CSS
role: "Styling for the jQuery Upload File widget (container, buttons, progress, states)"
size:
  lines_est: 90
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
  pattern: "N/A (static styles)"
  shared_state: []
  timing: "Applied to DOM during widget initialization"
invariants:
  - "Progress bar and file rows align and remain clickable across states"
  - "Drag-and-drop area retains visible focus/hover states"
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
similar_or_duplicate_files:
  - "airline-web/public/progress-bar/progress-bar.css (progress visuals overlap)"
rewrite_notes:
  mapping: "Fold widget styles into design system tokens/components; ensure consistent focus states and accessibility (contrast, hit targets)."
  risks:
    - "Global selectors may collide with other components"
  confidence: "high"
```

## Rollup for airline-web\public\jquery-upload-file

**Key responsibilities (top 5):**
1. Provide a browser-side widget to upload one or more files with progress feedback.
2. Offer client-side validation hooks for size/type/count before POSTing to the server.
3. Support drag-and-drop interactions and sequential upload queues.
4. Expose callback hooks to integrate upload lifecycle with UI (refresh lists, show toasts).
5. Style the upload UI consistently with the rest of the app.

**Cross-module dependencies (top 10 by frequency):**
- airline-web/app/views — pages instantiate the widget and include these assets.
- airline-web/app/controllers/LogoApplication.scala — probable target for logo uploads.
- airline-web/app/controllers/LiveryUtil.scala — probable target for livery/image uploads.
- airline-web/app/controllers/ProfileApplication.scala — possible avatar/profile uploads.
- airline-web/public/stylesheets/main.css — base styles the widget inherits.
- airline-web/public/progress-bar — overlapping UX patterns for progress feedback.
- airline-web/public/javascripts/main.js — generic AJAX/error handling interop in pages using the widget.
- airline-rest endpoints — server-side upload handlers that store files and apply validations (not shown here).
- Session/auth middleware — governs access to upload endpoints (server-side).
- CDN/object storage (future rewrite) — recommended destination via presigned URLs.

**High-leverage files to study first (top 10):**
- public/jquery-upload-file/jquery.uploadfile.min.js — determine all used options and required server behaviors (size/type limits, endpoints).
- public/jquery-upload-file/uploadfile.css — copy visual requirements into the new design system.
- app controllers handling uploads (LogoApplication.scala, LiveryUtil.scala, ProfileApplication.scala) — confirm expected request shape (field name, auth, limits) and response schema.
