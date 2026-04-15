# Feature Creation Workflow

## 1. FEATURE COMPLEXITY RULE (CLASSIFY FIRST)
Before generating code, you MUST mentally classify the feature:

- **Simple (1 API, no logic):** Skip UI model. Use Domain model directly in UI. Minimal layers (e.g., straight to ViewModel if UseCase is overkill, though UseCase is preferred).
- **Medium (API + logic):** Full flow (DTO -> Domain -> Repository -> UseCase -> ViewModel).
- **Complex (Multiple APIs, complex state):** Full architecture + UI model (`XxxUi`) + comprehensive `BaseUiState`.

## 2. Execution Order
- **Step 1 (Domain):** Define Models, Repository Interface, UseCase.
- **Step 2 (Data):** Define DTO, API interface, Mapper (DTO -> Domain), RepositoryImpl.
- **Step 3 (Presentation):** Read `mvi_mapper_rules.md`. Define State, Event, Effect, ViewModel, UI.
- **Step 4 (DI):** Update Hilt `Module`.