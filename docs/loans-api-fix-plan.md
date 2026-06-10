# Plan: Corregir los 3 problemas identificados en la sección Loans

## Context

Durante el análisis de la sección de Loans se detectaron tres problemas:
1. `GET /users/{id}` está duplicado en dos interfaces de Retrofit con tipos de retorno distintos.
2. `ActiveLoanScreen` muestra datos hardcodeados en lugar de los provenientes de `GET /loans`.
3. `LoanSuccessScreen` muestra datos hardcodeados en lugar de los provenientes de `POST /loans/apply`, y además existe un bug de timing: la navegación a la pantalla de éxito ocurre **antes** de que la API responda.

---

## Problema 1: Eliminar el `GET /users/{id}` duplicado

**Archivos a modificar:**

**`shared/AuthApi.kt`**  
Eliminar los métodos `getUser()` (líneas 26–29). Solo debe tener `login` y `register`.

**`auth/AuthRepositoryImpl.kt`**  
- Agregar `LendlyApiService` como nuevo parámetro del constructor (Hilt lo inyecta automáticamente, no necesita cambios en los módulos DI).
- Reemplazar el cuerpo de `getUser(id: Int)`: llamar a `lendlyApiService.getUser(id.toString())` que devuelve `UserApiResponse` (contiene `User`).
- Mapear `User → UserDto` para preservar el contrato de `AuthRepository` y no romper `LoginViewModel`:
  ```kotlin
  UserDto(id=user.id, fullName=user.fullName, phone=user.phone,
          email=user.email, avatar=user.avatar.ifEmpty{null},
          creditScore=user.creditScore, availableBalance=user.availableBalance,
          memberSince=user.memberSince)
  ```
- El `fallbackToRoom` y el caché en Room no cambian.

> `HomeViewModel` ya usa `LendlyApiService.getUser()` directamente → no necesita cambios.

---

## Problema 2: Conectar `ActiveLoanScreen` a la API real

**Archivos a modificar:**

**`ui/screens/loans/ActiveLoanScreen.kt`**  
- Reemplazar los parámetros `activeLoans` y `recentLoans` por `viewModel: LoanViewModel = hiltViewModel()`.
- Observar `viewModel.uiState` y derivar las listas:
  - `status == "ACTIVE"` → `ActiveLoanItem` (usando `companyName`, `nextPaymentDate`, `purpose`, `amount`, `lenderLogo`)
  - `status != "ACTIVE"` → `RecentLoanItem` (usando `nextPaymentDate`, `companyName`, `purpose`, `status`)
- En estado `Loading` o `Error` → mostrar listas vacías.
- Mantener `sampleActiveLoans()` y `sampleRecentLoans()` solo para el `@Preview`.

**`navigation/AppNavigation.kt`** (bloque `LoanActive`)  
- Agregar `val viewModel: LoanViewModel = hiltViewModel()` y pasarlo a `ActiveLoanScreen`.

> El ViewModel ya tiene `init { fetchLoans() }`, así que los datos se cargan automáticamente al entrar a la pantalla.

---

## Problema 3: Conectar `LoanSuccessScreen` a la respuesta real del API y corregir el bug de timing

La solución usa **navigation arguments** (primitivos en la ruta), que es el patrón ya utilizado en la sección CashIn del proyecto.

**Archivos a modificar (en orden):**

**`data/repository/LoanRepository.kt`**  
Cambiar `applyForLoan()` para devolver `LoanApplyLoan?` en lugar de `List<Loan>`:
```kotlin
suspend fun applyForLoan(amount: Double, installments: Int): LoanApplyLoan? {
    val response = api.applyLoan(LoanApplyRequest(amount, installments))
    return if (response.success) response.loan else null
}
```

**`usecase/ApplyForLoanUseCase.kt`**  
Actualizar el tipo de retorno a `LoanApplyLoan?` delegando al repositorio.

**`viewmodel/LoanViewModel.kt`**  
- Agregar estados:
  ```kotlin
  private val _appliedLoan = mutableStateOf<LoanApplyLoan?>(null)
  val appliedLoan: State<LoanApplyLoan?> = _appliedLoan

  private val _navigateToSuccess = mutableStateOf(false)
  val navigateToSuccess: State<Boolean> = _navigateToSuccess

  fun onSuccessNavigated() { _navigateToSuccess.value = false }
  ```
- En `applyForLoan()`: guardar el resultado en `_appliedLoan`, luego activar `_navigateToSuccess = true`. Limpiar `amountInput` como antes.

**`ui/screens/loans/LoanFormScreen.kt`**  
- Corregir el bug de timing: reemplazar el `onSuccess()` inmediato dentro del `onClick` por un `LaunchedEffect` que observa `navigateToSuccess`:
  ```kotlin
  val navigateToSuccess by viewModel.navigateToSuccess
  LaunchedEffect(navigateToSuccess) {
      if (navigateToSuccess) {
          viewModel.onSuccessNavigated()
          onSuccess()
      }
  }
  // onClick solo llama:
  onClick = { viewModel.applyForLoan() }
  ```

**`navigation/NavigationKeys.kt`**  
- Actualizar la ruta de `LoanSuccess` para incluir los 7 campos de `LoanApplyLoan` como query parameters.
- Agregar `fun createRoute(loan: LoanApplyLoan)` que construye la URL con `Uri.encode()` para los Strings.

**`navigation/AppNavigation.kt`**  
- Bloque `LoanForm`: el `onSuccess` lambda lee `viewModel.appliedLoan.value` y navega via `AppDestination.LoanSuccess.createRoute(loan)`.
- Bloque `LoanSuccess`: declarar `navArguments`, extraer los valores del `backStackEntry`, construir el `TransactionDetails` real y pasarlo a `LoanSuccessScreen`.

> `LoanSuccessScreen` no cambia su firma; solo el llamador en `AppNavigation` pasa datos reales en vez del objeto hardcodeado.

---

## Orden de ejecución

Para evitar errores de compilación, aplicar en este orden:

1. `LoanRepository.kt`
2. `ApplyForLoanUseCase.kt`
3. `LoanViewModel.kt`
4. `AuthApi.kt`
5. `AuthRepositoryImpl.kt`
6. `NavigationKeys.kt`
7. `ActiveLoanScreen.kt`
8. `LoanFormScreen.kt`
9. `AppNavigation.kt`

---

## Verificación

- Compilar el proyecto sin errores.
- Abrir la app → Loans → completar el formulario → verificar que el botón "Get This Loan" queda bloqueado hasta que la API responde.
- Confirmar que `LoanSuccessScreen` muestra el monto real, la cuota mensual, el interés y el número de transacción provenientes de la API.
- Ir a `ActiveLoanScreen` y confirmar que los préstamos activos y recientes son los datos reales de `GET /loans` (no el "iPhone 15 Pro Max" hardcodeado).
- Hacer login con un usuario guardado y confirmar que el perfil del returning user sigue cargando correctamente (verifica que `AuthRepositoryImpl` funciona con la nueva fuente).