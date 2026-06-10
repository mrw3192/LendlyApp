# Plan: Corregir los problemas identificados en la sección Manage

## Context

Durante el análisis de la sección Manage (tab de perfil) se detectaron dos problemas corregibles:

1. **`userId` hardcodeado como `"1"`** en `ProfileViewModel.kt` — siempre carga el perfil del usuario con ID 1, sin importar quién esté logueado.
2. **Fecha de nacimiento hardcodeada** en `EditProfileScreen.kt` — muestra siempre "08 / 12 / 1997" aunque el modelo `User` tiene el campo `birthDate` proveniente de la API.

El stub de `updateUserProfile` **no se toca**: la API no expone un endpoint de actualización en los requisitos del proyecto, por lo que es intencional.

---

## Problema 1: `userId` hardcodeado

### Análisis

`ProfileViewModel.kt:55`:
```kotlin
getUserProfileUseCase("1")  // siempre id=1
```

`UserPreferences` ya tiene `rememberedUserId: Flow<Int?>` que se guarda durante el login en `AuthRepositoryImpl`. Es el mecanismo correcto para obtener el ID del usuario actual.

### Cambios

**`viewmodel/ProfileViewModel.kt`**
- Agregar `UserPreferences` al constructor (Hilt ya lo provee como `@Singleton`).
- En `loadProfile()`, leer el ID desde `userPreferences.rememberedUserId.firstOrNull()` antes de llamar al use case.
- Si el ID es null (no hay sesión), emitir `ProfileUiState.Error`.

Agregar imports: `kotlinx.coroutines.flow.firstOrNull` y `com.example.lendlyapp.data.local.UserPreferences`.

---

## Problema 2: Fecha de nacimiento hardcodeada

### Análisis

`EditProfileScreen.kt` muestra tres campos de texto con valores fijos `"08"`, `"12"`, `"1997"`.  
El modelo `User` tiene `birthDate: String` proveniente de la API.  
`ProfileViewModel.initializeForm(user)` inicializa los campos del formulario pero no incluye `birthDate`.

### Cambios

**`viewmodel/ProfileViewModel.kt`**
- Agregar estado `birthDateInput = mutableStateOf("")` con `private set`.
- En `initializeForm(user)`, asignar `birthDateInput.value = user.birthDate`.

**`ui/screens/profile/EditProfileScreen.kt`**
- Observar `viewModel.birthDateInput`.
- Reemplazar los valores hardcodeados `"08"`, `"12"`, `"1997"` por el valor del ViewModel.

---

## Orden de ejecución

1. `ProfileViewModel.kt` — inyectar `UserPreferences`, corregir `loadProfile()`, agregar `birthDateInput`
2. `EditProfileScreen.kt` — mostrar `birthDateInput` en lugar de strings fijos

---

## Archivos a modificar

| Archivo | Cambio |
|---------|--------|
| `viewmodel/ProfileViewModel.kt` | Inyectar `UserPreferences`, leer userId real, agregar `birthDateInput` |
| `ui/screens/profile/EditProfileScreen.kt` | Mostrar `birthDateInput` en lugar de strings fijos |

## Archivos que NO se modifican

| Archivo | Motivo |
|---------|--------|
| `profile/ProfileRepositoryImpl.kt` | El stub de update es intencional (API no tiene endpoint) |
| `profile/usecase/UpdateUserProfileUseCase.kt` | Sin cambios |
| `navigation/AppNavigation.kt` | Sin cambios |
| `di/ProfileModule.kt` | Sin cambios (Hilt inyecta `UserPreferences` automáticamente) |

---

## Verificación

- Compilar el proyecto sin errores.
- Hacer login con un usuario → ir a Manage → confirmar que el nombre mostrado corresponde al usuario logueado (no siempre el ID 1).
- Abrir EditProfile → confirmar que la fecha de nacimiento muestra el valor real de la API en lugar de "08 / 12 / 1997".
- Guardar el perfil → confirmar que navega a la pantalla de éxito (el stub sigue funcionando).
