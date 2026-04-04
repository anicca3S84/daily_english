# Domain to UI Mapper Rules

## 🚫 DO NOT CREATE UI MODEL WHEN:
- The Domain model is already UI-ready.
- There is no formatting, no combination of fields, and no UI-only state (like `isSelected`).
- The screen is simple (e.g., just displaying static profile info).
- **Action:** Prefer using the Domain model directly in the `State` to reduce complexity and file count.

## ✅ MUST CREATE UI MODEL (Domain -> UI) WHEN:
1. **Display Logic:** Domain has `progress = 0.7f`, UI needs `"70%"`.
2. **Field Combination:** Domain has `firstName` + `lastName`, UI needs `fullName`.
3. **UI-Specific State:** UI needs `isExpanded`, `isLoadingItem` flags (Domain MUST NOT have these).
4. **Localization/Formatting:** Domain has `price = 1000`, UI needs `"1,000 đ"`.
5. **Shared Domain Model:** Multiple different screens use the same Domain model but require completely different fields.

**Rule:** DTO -> Domain mapping is ALWAYS required. Domain -> UI mapping is CONDITIONAL.