# Common Code Snippets & Templates

Always base your generated code on these EXACT structures from the `com.codework.daily_english` project. Do NOT skip any layers.

## 1. MVI Contracts (State, Event, Effect)
```kotlin
// STATE (Uses BaseUiState for loadable data)
data class FeatureState(
    val mainDataState: BaseUiState<DomainModelUi> = BaseUiState(),
    val someOtherValue: Int = 0
)
```


```kotlin
//EVENT
sealed interface FeatureEvent {
    data class OnItemClick(val id: String) : FeatureEvent
    object OnRefresh : FeatureEvent
}
```


```kotlin
//  EFFECT (Must implement BaseEffect)
sealed interface FeatureEffect : BaseEffect {
    data class NavigateToDetail(val id: String) : FeatureEffect
}
```


## 2. ViewModel Template
```kotlin
@HiltViewModel
class FeatureViewModel @Inject constructor(
    private val doSomethingUseCase: DoSomethingUseCase
) : BaseViewModel<FeatureState, FeatureEvent, FeatureEffect>(
    initialState = FeatureState()
) {
    override fun onEvent(event: FeatureEvent) {
        when (event) {
            is FeatureEvent.OnItemClick -> {
                sendEffect(FeatureEffect.NavigateToDetail(event.id))
            }
            FeatureEvent.OnRefresh -> loadData()
        }
    }

    private fun loadData() {
        // 1. Set Loading state
        _state.update { 
            it.copy(mainDataState = it.mainDataState.copy(isLoading = true, error = null)) 
        }

        // 2. Call UseCase
        viewModelScope.launch {
            doSomethingUseCase()
                .onSuccess { result ->
                    // Map Domain to UI Model if needed here via .toUiModels()
                    _state.update { state ->
                        state.copy(
                            mainDataState = state.mainDataState.copy(
                                isLoading = false,
                                data = result // or result.toUiModels()
                            )
                        )
                    }
                }
                .onError { appError ->
                    _state.update { state ->
                        state.copy(
                            mainDataState = state.mainDataState.copy(
                                isLoading = false,
                                error = appError.asUiText()
                            )
                        )
                    }
                }
        }
    }
}
```
## 3. UseCase Template
```kotlin
class DoSomethingUseCase @Inject constructor(
    private val repository: FeatureRepository
) {
    suspend operator fun invoke(): AppResult<DomainModel> {
        return repository.getData()
    }
}
```
## 4. Data Layer (Api -> RemoteDataSource -> RepositoryImpl)
Notice: The project uses a dedicated RemoteDataSource layer before the Repository.
```Kotlin
// 4a. API Interface
interface FeatureApi {
    @GET("feature/data")
    suspend fun getData(): BaseResponse<FeatureDto>
}

// 4b. RemoteDataSource Interface & Impl (Returns AppResult<DTO>)
interface FeatureRemoteDataSource {
    suspend fun getData(): AppResult<FeatureDto>
}

class FeatureRemoteDataSourceImpl @Inject constructor(
    private val api: FeatureApi
) : FeatureRemoteDataSource {
    override suspend fun getData(): AppResult<FeatureDto> {
        return safeApiCall { api.getData() }.unwrap() 
        // Note: unwrap() or similar extension is used to extract BaseResponse body
    }
}


// 4c. RepositoryImpl (Calls DataSource, maps DTO -> Domain)
class FeatureRepositoryImpl @Inject constructor(
    private val remoteDataSource: FeatureRemoteDataSource
) : FeatureRepository {
    override suspend fun getData(): AppResult<DomainModel> {
        return remoteDataSource.getData().map { it.toDomain() }
    }
}

```
## 5. DI Module Binding Template
```Kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class FeatureModule {

    companion object {
        @Provides
        @Singleton
        fun provideFeatureApi(retrofit: Retrofit): FeatureApi {
            return retrofit.create(FeatureApi::class.java)
        }
    }

    @Binds
    @Singleton
    abstract fun bindFeatureRepository(
        impl: FeatureRepositoryImpl
    ): FeatureRepository

    @Binds
    @Singleton
    abstract fun bindFeatureRemoteDataSource(
        impl: FeatureRemoteDataSourceImpl
    ): FeatureRemoteDataSource
}
```

## 6.DTO data Class
```Kotlin
data class UserStatsDto(
    @SerializedName("streak")
    val streak: Int?,
    @SerializedName("total_exp")
    val totalExp: Int?,
    @SerializedName("lessons_done")
    val lessonsDone: Int?,
    @SerializedName("words_learned")
    val wordsLearned: Int?
)
```

## 7. Domain data Class
```Kotlin
data class UserStats(
val streak: Int,
val totalExp: Int,
val lessonsDone: Int,
val wordsLearned: Int
)
```

## 8. Mapper class
```Kotlin
fun CourseDto.toDomain() = Course(
id = id ?: "no_id_${System.currentTimeMillis()}",
title = title.orEmpty(),
description = description.orEmpty(),
cefr = cefr.orEmpty(),
lessons = lessons ?: 0,
progress = progress ?: 0f,
isLocked = isLocked ?: true,
code = code ?: "default"
)
```