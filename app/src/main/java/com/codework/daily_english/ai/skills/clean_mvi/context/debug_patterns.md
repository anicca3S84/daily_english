# Common Bugs & Debugging Patterns

When asked to debug or fix an issue, immediately check these common patterns before writing any code:

## 1. API returns null or parsing fails
**Symptom:** Screen is empty, or JSON parsing exception occurs.
**Action:**
- Check DTO nullable fields (`String?`, `Int?`).
- Verify `@SerializedName` matches the backend JSON keys perfectly.
- Ensure the Mapper safely handles nulls (e.g., `title = dto.title.orEmpty()`).

## 2. UI is not updating
**Symptom:** User clicks a button, API call succeeds, but UI remains unchanged.
**Action:**
- Check ViewModel's StateFlow update mechanism.
- Ensure `_state.update { it.copy(...) }` is used.
- Verify the Compose UI is actually observing the state properties properly and not mutating variables outside of State.

## 3. Crash on Navigation or Toast (Effect handling)
**Symptom:** App crashes when trying to navigate or show a Snackbar/Toast.
**Action:**
- Check if the Effect is being collected in the UI layer (e.g., inside a `LaunchedEffect`).
- Ensure Effects are one-time events and not stored in the State.

## 4. Unhandled Coroutine Exceptions
**Symptom:** App crashes during a network request.
**Action:**
- Ensure the API call is wrapped in a safe call block that returns an `AppResult.Error`.
- Verify no raw Exceptions (like `HttpException`, `IOException`) are leaking from the Repository layer to the ViewModel.