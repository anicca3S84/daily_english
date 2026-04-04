# Project Structure Reference
Package: `com.codework.daily_english`

- `core/`
    - `base/` (BaseViewModel, BaseUiState, BaseEffect)
    - `dispatchers/` (Dispatcher)
    - `network/` (BaseResponse)
    - `result/` (AppResult)
- `data/`
    - `[feature_name]/`
        - `local/` (DataSource, Impl, Preferences)
        - `mapper/` (FeatureMapper)
        - `remote/` (DataSource, Impl, Dto, Api)
        - `repository/` (RepositoryImpl)
- `di/` (NetworkModule, FeatureModule)
- `domain/`
    - `[feature_name]/`
        - `model/` (Domain Models)
        - `repository/` (Repository Interfaces)
        - `usecase/` (ActionUseCases)
- `presentation/`
    - `screen/`
        - `[feature_name]/`
            - `components/` (UI Widgets)
            - `mvi/` (State, Event, Effect)
            - `FeatureScreen.kt`
            - `FeatureViewModel.kt`
- `theme/` (CourseThemeProvider)
- `utils/` (UiText)

Naming Convention:
- DTO: `XxxDto`
- Mapper: `XxxMapper`
- RepositoryImpl: `XxxRepositoryImpl`
- UseCase: `ActionUseCase`
- ViewModel: `XxxViewModel`