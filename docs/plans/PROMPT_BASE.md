Implement the X flow following STRICTLY the project specifications, Figma implementation spec, and inspection workflow.

Before doing anything else, read and follow:
- SPEC_FUNCIONAL.md
- SPEC_TECNICO.md

These documents define the project's architecture, conventions, functional expectations, implementation rules, and UI guidelines. All generated code and decisions must align with those specifications.

# MANDATORY WORKFLOW

You MUST follow this workflow exactly.

Implementation is NOT allowed until the plan is explicitly approved.

# STEP 1 — READ PROJECT SPECS

Read and analyze:
- SPEC_FUNCIONAL.md
- SPEC_TECNICO.md

Extract:
- architecture rules
- UI conventions
- navigation patterns
- folder structure
- naming conventions
- state management rules
- reusable component guidelines
- functional requirements

Then STOP and summarize findings.

Ask for confirmation before continuing.

# STEP 2 — FIGMA ANALYSIS

Identify and inspect the relevant Figma nodes.

Use the inspect() workflow defined in the technical spec.

Before implementation, inspect figma.json extracting for EACH node used in the screen (NOT the base component definition):

- complete fills including alpha values
- effects (type + radius)
- strokes including color and opacity
- rotation
- imageFilters (saturation/contrast)
- node opacity
- number of instances used in the screen

Always use the values from the actual instances present in the screen, NOT the values from the original component definition.

Analyze:
- hierarchy
- layout structure
- spacing
- typography
- fills/colors
- auto-layout behavior
- overlays
- assets/images
- reusable patterns
- animations/interactions

Then STOP and present the analysis.

Ask for confirmation before continuing.

# STEP 3 — GENERATE FEATURE PLAN

Create a detailed implementation plan for the feature.

The plan MUST be saved inside:

docs/plans/<feature_name>/

Examples:
- docs/plans/login/
- docs/plans/onboarding/
- docs/plans/profile/
- docs/plans/settings/

Inside that folder, generate the corresponding markdown plan file.

Examples:
- docs/plans/login/login_plan.md
- docs/plans/onboarding/onboarding_plan.md

The folder name MUST represent the functionality being implemented.

The generated plan must include:

- feature scope
- involved screens
- Figma node references
- reusable components
- navigation flow
- state handling
- architecture decisions
- folder/file structure
- implementation phases
- risks/ambiguities
- validation checklist
- pending questions
- assets required

Do NOT generate implementation code yet.

After generating the plan:
- save it
- summarize it
- STOP
- ask for approval

# CRITICAL RULE

DO NOT IMPLEMENT ANYTHING until the plan is explicitly approved.

No Compose code, no ViewModels, no navigation code, no components, and no refactors are allowed before approval.

# STEP 4 — IMPLEMENTATION

ONLY AFTER approval:

1. Break implementation into incremental tasks
2. Implement step-by-step
3. After each major step:
   - explain what was implemented
   - explain architectural decisions
   - ask for confirmation before continuing

# FEATURE SCOPE

Implement:

1. Splash Screen
2. Onboarding flow/pages
3. Navigation between onboarding screens
4. CTA actions (Next / Skip / Get Started)
5. Pager indicators
6. Final navigation to Home/Login

# IMPLEMENTATION RULES

- Respect pixel-perfect implementation
- Preserve spacing and hierarchy from Figma
- Do NOT simplify layouts unless explicitly justified
- Reuse components whenever possible
- Avoid duplicated composables
- Use idiomatic Jetpack Compose practices
- Use Material 3
- Keep code modular and scalable
- Separate UI/state/navigation concerns
- Follow all conventions defined in SPEC_TECNICO.md
- Respect all functional requirements from SPEC_FUNCIONAL.md
- Do not hallucinate missing assets
- Explicitly mention ambiguities when design data is unclear
- Respect the existing project structure
- Prefer reusable design-system-driven components

# REQUIRED OUTPUT STRUCTURE

## 1. Specification Alignment
Summarize project rules and constraints.

STOP AND ASK FOR CONFIRMATION.

## 2. Feature Analysis
Present Figma/design analysis.

STOP AND ASK FOR CONFIRMATION.

## 3. Feature Plan
Generate and save the plan inside:
docs/plans/<feature_name>/

Then summarize the generated plan.

STOP AND ASK FOR APPROVAL.

## 4. Implementation
Only after approval.

Implement incrementally and request confirmation between major steps.

# VALIDATION

At the end include:
- visual validation checklist
- missing assets checklist
- responsive/adaptive considerations
- potential mismatches with Figma
- technical debt notes