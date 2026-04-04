# Android Navigation Skill (MVI + Typed Route + Compose)

## PURPOSE
This skill enforces a strict, scalable navigation architecture:

- MVI-based navigation (Effect-driven)
- Typed routes (no string routes)
- Single navigation source (NavGraph)
- Clear separation of concerns
- Fully AI-optimized for consistency

---

## ARCHITECTURE FLOW

ViewModel (emit Effect)
↓
UI (collect Effect)
↓
NavGraph (handle navigation)
↓
NavController (execute)

---

## CORE RULES (MANDATORY)

- NEVER use navController inside ViewModel
- NEVER navigate directly from UI (except system back)
- ALWAYS emit navigation via Effect
- ALWAYS use typed Route (sealed interface)
- ALWAYS use composableScreen()
- NavGraph is the ONLY place calling navController.navigate()

---

## ROUTE DEFINITION

```kotlin
interface Route

@Serializable
data object ProfileScreen : Route

@Serializable
data class CourseDetailScreen(
    val courseId: String,
    val courseCode: String
) : Route

```

## EFFECT (NAVIGATION TRIGGER)
```kotlin
sealed interface HomeEffect : BaseEffect {

    data object NavigateToProfile : HomeEffect

    data class NavigateToCourseDetail(
        val id: String,
        val code: String
    ) : HomeEffect
}
```

## GLOBAL NAVIGATION (OPTIONAL – USING globalEffect)
```kotlin
//show toast, snack bar
override fun onEvent(event: CourseEvent) {
    when (event) {
        is CourseEvent.OnCourseClick -> {
            if (event.isLocked) {
                sendGlobalEffect(ShowToast(UiText.StringResource(R.string.noti_course_looked)))
            } else {
                sendEffect(NavigateToCourseDetails(event.courseId, event.courseCode))
            }
        }
        CourseEvent.OnRefreshData -> loadDashboardData()
    }
}
```

## VIEWMODEL (EMIT EFFECT ONLY)
```kotlin
fun onProfileClick() {
    sendEffect(HomeEffect.NavigateToProfile)
}

fun onCourseClick(id: String, code: String) {
    sendEffect(HomeEffect.NavigateToCourseDetail(id, code))
}

```

## UI (COLLECT EFFECT – NO NAVIGATION LOGIC)
```kotlin
CollectEffect(viewModel.effect) { effect ->
    onNavigate(effect)
}

```

## NAVGRAPH (ONLY PLACE TO NAVIGATE)
```kotlin
when (effect) {

    is HomeEffect.NavigateToProfile -> {
        navController.navigate(ProfileScreen)
    }

    is HomeEffect.NavigateToCourseDetail -> {
        navController.navigate(
            CourseDetailScreen(
                courseId = effect.id,
                courseCode = effect.code
            )
        )
    }
}

```

## REGISTER SCREEN (STANDARD PATTERN)
```kotlin
composableScreen<
    ProfileScreen,
    ProfileViewModel,
    ProfileState,
    ProfileEvent,
    ProfileEffect
>(
    onNavigate = { effect ->
        when (effect) {
            // handled in NavGraph
        }
    }
) { state, onEvent ->
    ProfileScreen(state, onEvent)
}

```

## COMMON PATTERNS
```kotlin
1. Bottom Navigation
navController.navigate(item.route) {
    popUpTo(HomeScreen) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}
2. Navigation With Arguments
navController.navigate(
    CourseDetailScreen(
        courseId = id,
        courseCode = code
    )
)
3. Back Navigation
navController.popBackStack()
4. Clear Back Stack (Auth to Home)
navController.navigate(HomeScreen) {
    popUpTo(0)
}
```


## ANTI-PATTERNS (STRICTLY FORBIDDEN)
//  ViewModel navigation
navController.navigate("profile")

//  String routes
navController.navigate("profile_screen")

//  UI triggers navigation directly
Button(onClick = {
    navController.navigate(ProfileScreen)
})

//  Multiple navigation sources
// (Navigation must ONLY exist in NavGraph)