# Execution Workflow

Always determine if the user wants to CREATE, REFACTOR, or DEBUG before proceeding.

## REFACTOR MODE (CRITICAL)
When the user asks to fix, refactor, or update existing code:
- **Do NOT rewrite the entire file** unless explicitly told to.
- **Only fix the problematic parts.**
- Keep the original structure, imports, and logic if they are valid.
- Optimize for minimal token output.

## CREATE MODE
When generating a new feature, read `context/feature_maker.md` FIRST to classify the feature complexity. Then follow standard creation execution.

## Information Gathering
- Check `context/project_structure.md` for proper routing.
- Check `context/snippets.md` for boilerplate.