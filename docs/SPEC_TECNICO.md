# Spec Técnico — LendlyApp
**Versión 1.0 — Mayo 2026**

---

## 1. Stack Tecnológico

| Capa | Tecnología | Versión |
|---|---|---|
| **Sistema Operativo** | Android SDK | Min: 24 (7.0 Nougat) / Target: 36 (Android 16) |
| **Lenguaje** | Kotlin | 1.9.22 |
| **UI Framework** | Jetpack Compose | Material 3 (BOM 2024.02.01) |
| **Inyección de Dependencias** | Dagger Hilt | 2.50 |
| **Networking** | Retrofit + OkHttp3 | 2.9.0 |
| **Serialización** | Gson | 2.10.1 |
| **Persistencia de Sesión** | Preferences DataStore | 1.0.0 |
| **Persistencia Local (Caché)** | Room Database | 2.6.1 |
| **Carga de Imágenes** | Coil (Compose extension) | 2.6.0 |
| **Concurrencia** | Kotlin Coroutines & Flows | 1.7.3 |
| **CI/CD** | GitHub Actions | JDK 17 (Temurin) |

---

## 2. Estructura de Directorios

El código de la aplicación se divide en capas siguiendo los principios de **Clean Architecture** y **MVVM**:

```
LendlyApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/lendlyapp/
│   │   │   │   ├── LendlyApplication.kt      # Clase Application (inicia Hilt)
│   │   │   │   ├── MainActivity.kt           # Entrypoint de la App
│   │   │   │   ├── data/                     # CAPA DE DATOS (Data)
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── UserPreferences.kt # DataStore Manager
│   │   │   │   │   │   └── database/          # Room DB (Entities, DAOs)
│   │   │   │   │   ├── network/
│   │   │   │   │   │   ├── AuthApi.kt         # Retrofit endpoints
│   │   │   │   │   │   ├── model/             # DTOs (Request / Response)
│   │   │   │   │   │   └── Interceptors.kt    # API Key injection
│   │   │   │   │   └── repository/
│   │   │   │   │       ├── AuthRepositoryImpl.kt
│   │   │   │   │       ├── LoanRepositoryImpl.kt
│   │   │   │   │       └── ShopRepositoryImpl.kt
│   │   │   │   ├── domain/                   # CAPA DE NEGOCIO (Domain)
│   │   │   │   │   ├── model/                 # Entidades puras de dominio
│   │   │   │   │   ├── repository/            # Interfaces de repositorios
│   │   │   │   │   └── usecase/               # Casos de uso (Lógica)
│   │   │   │   ├── presentation/             # CAPA DE VISTA (Presentation)
│   │   │   │   │   ├── auth/                  # Splash, Login, Registro
│   │   │   │   │   ├── dashboard/             # Home, Score Gauge
│   │   │   │   │   ├── loans/                 # Préstamos, Simulador
│   │   │   │   │   ├── shop/                  # Catálogo, Detalle
│   │   │   │   │   ├── history/               # Historial de transacciones
│   │   │   │   │   ├── profile/               # Perfil, Cash In
│   │   │   │   │   ├── navigation/            # AppNavigation, BottomBar
│   │   │   │   │   └── theme/                 # Colores, Tipografías, Material 3
│   │   │   │   └── di/                        # MÓDULOS DE INYECCIÓN DE HILT
│   │   │   │       ├── AppModule.kt           # Proveedores generales (DataStore)
│   │   │   │       ├── NetworkModule.kt       # Proveedores de Retrofit
│   │   │   │       └── RepositoryModule.kt    # Binding de Interfaces a Impls
└── .github/
    └── workflows/
        └── android-ci.yml                    # Pipeline de Integración Continua
```

---

## 3. Modelo de Datos y Persistencia

### 3.1 Preferences DataStore (`UserPreferences.kt`)
Se utiliza para guardar los datos básicos de la sesión activa del usuario.

| Clave (Key) | Tipo | Descripción |
|---|---|---|
| `auth_token` | Preferences.Key<String> | Token Bearer devuelto por la API para autenticar peticiones. |
| `user_email` | Preferences.Key<String> | Email del usuario logueado para autocompletar formularios. |
| `user_id` | Preferences.Key<Int> | ID del usuario para consultas de perfil. |

### 3.2 Entidades Locales de Room (Caché local opcional)
*Nota: Si se requiere persistencia local de transacciones o préstamos sin conexión, se implementarán las siguientes tablas:*

#### `loans` (Tabla de Préstamos)
| Campo | Tipo | Notas |
|---|---|---|
| `id` | Int PK | Identificador único del préstamo |
| `amount` | Double | Monto total solicitado |
| `installments` | Int | Número de cuotas pactadas (3, 6, 12) |
| `interestRate` | Double | Tasa mensual (0.05 a 0.12) |
| `status` | String | `PENDING`, `APPROVED`, `REJECTED`, `PAID` |
| `createdAt` | Long | Timestamp de creación |

#### `transactions` (Historial financiero)
| Campo | Tipo | Notas |
|---|---|---|
| `id` | String PK | UUID autogenerado |
| `amount` | Double | Monto del movimiento |
| `type` | String | `CASH_IN`, `LOAN_DISBURSEMENT`, `LOAN_REPAYMENT`, `SHOP_PURCHASE` |
| `description` | String | Detalle de la transacción |
| `timestamp` | Long | Fecha de ejecución |

---

## 4. API Endpoints (Módulo de Red)

Todas las llamadas se dirigen a `https://lendly-api.com/api/` (URL mock o la que provea la cátedra). Se inyecta automáticamente el header `x-api-key: 123456789` en un interceptor de OkHttp.

### 4.1 Autenticación (AuthApi)
* **Login:** `POST /auth/login`
  * Request: `{ "email": "user@test.com", "password": "123" }`
  * Response: `{ "token": "jwt_token_here", "userId": 12, "email": "user@test.com" }`
* **Registro:** `POST /auth/create`
  * Request: `{ "firstName": "John", "lastName": "Doe", "dni": "12345", "email": "user@test.com", "password": "123" }`
  * Response: `{ "status": "success", "message": "User created successfully" }`

### 4.2 Préstamos (LoanApi)
* **Obtener Préstamos:** `GET /loans` (Requiere Auth Header)
  * Response: `List<LoanResponse>`
* **Solicitar Préstamo:** `POST /loans/apply` (Requiere Auth Header)
  * Request: `{ "amount": 25000.0, "installments": 6 }`
  * Response: `{ "loanId": 101, "status": "APPROVED", "monthlyPayment": 4833.33 }`

### 4.3 Tienda y Catálogo (ShopApi)
* **Obtener Catálogo:** `GET /products`
  * Response: `List<ProductResponse>` (`{ "id": 1, "name": "Tablet", "price": 45000.0, "imageUrl": "url" }`)
* **Comprar a Crédito:** `POST /purchases/create` (Requiere Auth Header)
  * Request: `{ "productId": 1, "installments": 3 }`
  * Response: `{ "purchaseId": 204, "status": "SUCCESS" }`

### 4.4 Perfil y Transacciones (UserApi)
* **Detalle de Perfil:** `GET /users/{id}` (Requiere Auth Header)
  * Response: `{ "id": 12, "firstName": "John", "lastName": "Doe", "creditScore": 750, "balance": 18500.0, "creditLimit": 150000.0 }`
* **Modificar Perfil:** `PUT /users/{id}` (Requiere Auth Header)
  * Request: `{ "email": "newemail@test.com" }`
  * Response: `{ "status": "success" }`
* **Cargar Saldo (Cash In):** `POST /users/cash-in` (Requiere Auth Header)
  * Request: `{ "amount": 5000.0 }`
  * Response: `{ "newBalance": 23500.0 }`
* **Historial de Movimientos:** `GET /transactions` (Requiere Auth Header)
  * Response: `List<TransactionResponse>`

---

## 5. Componentes Frontend (Arquitectura UI)

Cada vista utiliza un patrón de estados reactivos unificados (`UiState`) modelados mediante data classes o clases selladas en Kotlin:

```kotlin
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val token: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
```

### 5.1 Pantallas y ViewModels
1. **`SplashScreen` + `SplashViewModel`**
   * Observa el flujo de `UserPreferences.authToken`. Dispara la navegación según su presencia.
2. **`LoginScreen` + `LoginViewModel`**
   * Mapea inputs de texto. Llama al `AuthRepository` para ejecutar el login y maneja el estado de carga y error en la UI.
3. **`RegisterScreen` + `RegisterViewModel`**
   * Controla validaciones regex de email y contraseñas robustas antes de llamar al servicio.
4. **`HomeScreen` + `HomeViewModel`**
   * Llama a `GET /users/{id}`. Posee la lógica para animar el arco del Credit Score Gauge según el valor recibido.
5. **`LoanSimulatorScreen` + `LoanViewModel`**
   * Almacena valores de simulador temporalmente y despacha el `POST /loans/apply`.
6. **`ShopScreen` + `ShopViewModel`**
   * Expone un `StateFlow<List<Product>>` mapeado desde el API client, cargando imágenes con Coil.
7. **`TransactionHistoryScreen` + `TransactionHistoryViewModel`**
   * Expone las transacciones filtradas por la selección del chip de UI.

---

## 6. Pipeline de Integración Continua (CI)

La verificación automática de la rama `feature/auth` y el Pull Request hacia `master` se procesa mediante el archivo `.github/workflows/android-ci.yml`.

### Procesos Automatizados:
1. **Verificación de Estilo y Errores (Lint):** Se ejecuta `./gradlew lintDebug` para buscar problemas de accesibilidad, seguridad o sintaxis incorrecta.
2. **Pruebas Unitarias:** `./gradlew testDebugUnitTest` corre los tests unitarios creados para verificar ViewModels y Repositorios.
3. **Compilación de Integridad:** Se ejecuta `./gradlew assembleDebug` para compilar completamente el código fuente a nivel binario.
