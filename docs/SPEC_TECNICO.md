# Spec Técnico — LendlyApp
**Versión 2.0 — Mayo 2026**

---

## 1. Stack Tecnológico

| Capa | Tecnología | Versión |
|---|---|---|
| **Sistema Operativo** | Android SDK | Min: 24 (7.0 Nougat) / Target: 36 (Android 16) |
| **Lenguaje** | Kotlin | 1.9.22 |
| **UI Framework** | Jetpack Compose | Material 3 (BOM 2026.03.01) |
| **Inyección de Dependencias** | Dagger Hilt | 2.51.1 |
| **Networking** | Retrofit + OkHttp3 | 2.11.0 |
| **Serialización** | Gson | 2.10.1 |
| **Persistencia de Sesión** | Preferences DataStore | 1.1.1 |
| **Persistencia Local (Caché)** | Room Database | 2.6.1 |
| **Carga de Imágenes** | Coil (Compose + SVG) | 2.6.0 |
| **Concurrencia** | Kotlin Coroutines & Flows | 1.7.3 |

---

## 2. Estructura de Directorios

```
LendlyApp/
├── app/src/main/
│   ├── assets/                     ← Imágenes y SVGs (ver §2.3)
│   └── java/com/example/lendlyapp/
│       ├── LendlyApplication.kt
│       ├── MainActivity.kt
│       ├── auth/
│       │   ├── AuthRepository.kt
│       │   └── AuthRepositoryImpl.kt
│       ├── core/
│       │   ├── ApiConfig.kt
│       │   └── AuthInterceptor.kt
│       ├── data/
│       │   ├── local/
│       │   │   └── UserPreferences.kt
│       │   └── repository/
│       │       ├── LoanRepository.kt
│       │       ├── ProductRepository.kt
│       │       ├── TransactionRepository.kt
│       │       └── UserRepository.kt
│       ├── di/
│       │   └── AppModule.kt
│       ├── helpers/
│       ├── model/
│       │   ├── AuthResult.kt
│       │   ├── Loan.kt
│       │   ├── Product.kt
│       │   ├── Transaction.kt
│       │   └── User.kt
│       ├── navigation/
│       │   ├── AppNavigation.kt
│       │   └── NavigationKeys.kt
│       ├── shared/
│       │   ├── LendlyAlertDialog.kt
│       │   ├── LendlyBottomBar.kt
│       │   ├── LendlyLogo.kt
│       │   ├── LendlyPhoneInput.kt
│       │   ├── LendlyTextField.kt
│       │   ├── LendlyTopAppBar.kt
│       │   └── OtpInputRow.kt
│       ├── ui/
│       │   └── screens/
│       │       ├── auth/
│       │       │   ├── SplashScreen.kt
│       │       │   └── LoginScreen.kt
│       │       ├── register/
│       │       │   ├── VerifyPhoneScreen.kt
│       │       │   ├── SmsVerificationScreen.kt
│       │       │   ├── ProfileDetailScreen.kt
│       │       │   ├── CreatePasswordScreen.kt
│       │       │   └── DoneScreen.kt
│       │       ├── onboarding/
│       │       │   └── OnboardingScreen.kt
│       │       ├── home/
│       │       ├── loans/
│       │       ├── shop/
│       │       ├── history/
│       │       └── profile/
│       ├── theme/
│       │   ├── Color.kt
│       │   ├── Theme.kt
│       │   └── Type.kt
│       └── viewmodel/
│           ├── SplashViewModel.kt
│           ├── OnboardingViewModel.kt
│           ├── LoginViewModel.kt
│           ├── RegisterViewModel.kt
│           ├── HomeViewModel.kt
│           ├── LoanViewModel.kt
│           ├── ShopViewModel.kt
│           ├── TransactionHistoryViewModel.kt
│           └── ProfileViewModel.kt
├── docs/
│   ├── figma.json                  ← Árbol completo del diseño Figma
│   ├── SPEC_FUNCIONAL.md
│   └── SPEC_TECNICO.md
```

### Descripción de cada capa

| Carpeta | Qué va acá |
|---|---|
| `auth/` | Interfaz `AuthRepository` y su implementación. Todo lo de login/register a nivel de repositorio vive acá porque auth es transversal: toca red, sesión y navegación. |
| `core/` | Infraestructura técnica compartida: base URL, constantes de red, interceptor de OkHttp que añade los headers `Bearer` y `x-api-key`. Sin UI. |
| `data/local/` | Persistencia en el dispositivo. `UserPreferences.kt` con DataStore (`auth_token`, `has_seen_onboarding`). En el futuro puede incluir Room. |
| `data/repository/` | Repositorios de las features (préstamos, productos, usuarios, transacciones). Consumen el API service y exponen `Flow` o `suspend fun` al ViewModel. |
| `di/` | Módulos Hilt. Solo provee instancias, no contiene lógica de negocio. |
| `helpers/` | Funciones utilitarias puras: formateo de fechas, validaciones, extensiones de `String`, etc. |
| `model/` | Entidades del negocio y DTOs de la API. Data classes que Retrofit deserializa con Gson y que los ViewModels consumen para construir el UiState. |
| `navigation/` | Grafo de navegación (`AppNavigation.kt`) y rutas (`NavigationKeys.kt`). Centraliza toda la lógica de backstack. |
| `shared/` | Componentes Compose reutilizables entre pantallas. Solo reciben parámetros y emiten eventos via callbacks — sin lógica de negocio. |
| `ui/screens/` | Pantallas organizadas por feature. Cada `Screen` es un `@Composable` puro que observa el ViewModel y delega acciones. |
| `theme/` | Design system: tokens de color, tipografía y tema Material 3. Nunca usar hex hardcodeado fuera de este paquete. |
| `viewmodel/` | Todos los ViewModels. Cada uno expone un `StateFlow<UiState>` con sealed class y recibe dependencias por Hilt. No referencian clases de Android directamente. |

---

## 2.1 Convenciones de la capa `model/`

Existen tres tipos de "modelo" en el proyecto. Es importante no confundirlos:

| Tipo | Dónde vive | Qué es |
|---|---|---|
| **Entidad / DTO** | `model/` | Data class que representa una entidad del negocio o la respuesta cruda de la API. Ejemplo: `User.kt`, `Loan.kt`, `AuthResult.kt`. |
| **UiState** | Dentro del ViewModel de cada feature | Sealed class que representa los estados posibles de una pantalla (`Idle`, `Loading`, `Success`, `Error`). No va en `model/`. |
| **Estado de formulario** | Dentro del ViewModel | Data class con los campos del formulario en pantalla. Ejemplo: `RegisterState`. No va en `model/`. |

**Regla:** el ViewModel consume los modelos de `model/` y los mapea a `UiState` antes de exponerlos a la UI. La capa `ui/screens/` nunca accede directamente a los DTOs de red.

---

## 2.2 Tokens de Diseño (Color.kt)

Todos los colores del Figma están mapeados como tokens en `theme/Color.kt`.
**Regla:** nunca usar hex hardcodeado — siempre el token.

| Token Kotlin | Hex | Uso |
|---|---|---|
| `FigmaNeonGreen` | `#7BF179` | Botones primarios, dots activos, íconos de acción |
| `FigmaOliveGreen` | `#4C662B` | Links ("Change", "Forgot"), bordes activos |
| `FigmaDarkForest` | `#002203` | Fondo onboarding, fondos oscuros de alto contraste |
| `FigmaOliveSeed` | `#122300` | Superficies oscuras (tarjetas sobre fondo oscuro) |
| `FigmaOrangeAccent` | `#FD7E14` | Alertas y avisos |
| `FigmaLightBg` | `#FCF8F8` | Fondo tema claro, avatares |
| `FigmaLightSurface` | `#FFFFFF` | Tarjetas, modales, campos |
| `FigmaLightText` | `#171D1E` | Texto principal tema claro |
| `FigmaDarkBg` | `#0B0B0B` | Fondo modo oscuro general |
| `FigmaDarkText` | `#FCF8F8` | Texto sobre fondos oscuros |
| `FigmaMintSplash` | `#E5F5EA` | Fondo splash + subtítulos onboarding |

**Colores locales** (definidos por pantalla, no en Color.kt):

| Hex | Uso |
|---|---|
| `#B1D18A` | Títulos de pantallas de onboarding |
| `#EADDFF` | Dots inactivos del onboarding (lavender) |
| `#102000` | Texto sobre botón primario verde |
| `#454745` | Labels de campos de formulario |
| `#6A6C6A` | Subtítulos y placeholders |

---

## 2.3 Assets en `app/src/main/assets/`

| Archivo | Origen | Usado en |
|---|---|---|
| `logo_lendly.svg` | Vectores del frame `Frame 134` | SplashScreen, OnboardingScreen |
| `onboarding_1.png` | imageRef `30e61e1c...` | OnboardingScreen página 1 |
| `onboarding_2.png` | imageRef `6b63c4ac...` | OnboardingScreen página 2 |
| `onboarding_3.png` | imageRef `aedaddcc...` | OnboardingScreen página 3 |
| `avatar.png` | imageRef `d6c8bef7...` | Tarjetas flotantes del onboarding |
| `product_1.png` | imageRef `be53c076...` | Product-card onboarding 2 |
| `product_2.png` | imageRef `0f92556e...` | Product-card onboarding 2 |

Para agregar nuevas imágenes al proyecto, ver §2.5 (proceso de descarga desde la API de Figma).

---

## 2.4 Referencia Figma

| Dato | Valor |
|---|---|
| **File Key** | `BwGJCV0dSduKZ7wlGZGvuT` |
| **API Token** | Generado en Figma → Settings → Security → Personal access tokens | Pedir token para hacer la consulta
| **figma.json** | `docs/figma.json` (exportado con `GET /v1/files/{file_key}`) |

### Estructura del figma.json

```
document → children
├── Cover          (portada — ignorar)
├── Sitemap        (mapa de sitio — referencia)
├── Components     (componentes reutilizables del design system)
└── Pages          ← PANTALLAS DE LA APP
    ├── Splash Screen and Onboarding
    │   ├── Splash Screen
    │   ├── Onboarding 1
    │   ├── Onboarding 2
    │   └── Onboarding 3
    ├── Login and Register
    │   ├── Login Page
    │   └── Create-password page
    ├── Home
    ├── Loan
    ├── Shop
    ├── History
    └── Manage
```

---

## 2.5 Metodología Obligatoria: Figma → Compose

> ⚠️ **Esta sección es de cumplimiento obligatorio.** Implementar una pantalla sin seguir estos pasos produce resultados incorrectos (elementos faltantes, posiciones equivocadas, imágenes espejadas, colores y tipografías erróneos).

### Paso 1 — Inspeccionar el frame con posiciones absolutas

Ejecutar el siguiente script Python para obtener **todos** los nodos con sus coordenadas exactas relativas a la pantalla:

```python
import json, sys
sys.stdout.reconfigure(encoding='utf-8')

with open("docs/figma.json", encoding="utf-8") as f:
    data = json.load(f)

pages = next(p for p in data["document"]["children"] if p["name"] == "Pages")

def find_by_name(node, name):
    if node.get("name") == name: return node
    for child in node.get("children", []):
        r = find_by_name(child, name)
        if r: return r
    return None

def hex_fill(fills):
    if not fills: return None
    f = fills[0]
    if f.get("type") == "SOLID":
        c = f["color"]
        return "#{:02X}{:02X}{:02X}".format(int(c["r"]*255), int(c["g"]*255), int(c["b"]*255))
    if f.get("type") == "IMAGE":
        return "IMG:" + f.get("imageRef", "")[:12]
    return f.get("type", "")

def inspect(node, screen_x=0, screen_y=0, depth=0):
    bb  = node.get("absoluteBoundingBox") or {}
    rx  = int(bb.get("x", 0) - screen_x)
    ry  = int(bb.get("y", 0) - screen_y)
    w   = int(bb.get("width", 0))
    h   = int(bb.get("height", 0))
    fill = hex_fill(node.get("fills", []))
    chars = node.get("characters", "")
    style = node.get("style", {})
    # Detectar flip en imageTransform
    for f in node.get("fills", []):
        tr = f.get("imageTransform")
        if tr and tr[0][0] < 0:
            print("  " * depth + f"*** FLIP HORIZONTAL en '{node['name']}'")
    print("  " * depth +
          f"[{node['type']}] '{node['name']}' pos=({rx:+d},{ry:+d}) {w}x{h}"
          + (f" fill={fill}" if fill else "")
          + (f" TEXT='{chars[:40]}' {style.get('fontSize','')}sp fw={style.get('fontWeight','')}" if chars else ""))
    for child in node.get("children", []): inspect(child, screen_x, screen_y, depth+1)

frame = find_by_name(pages, "NOMBRE_DEL_FRAME")   # ← reemplazar
bb = frame["absoluteBoundingBox"]
inspect(frame, bb["x"], bb["y"])
```

### Paso 2 — Identificar y descargar imageRefs

En la salida del Paso 1, buscar líneas con `IMG:`. Cada una es una imagen que **debe descargarse** de la API de Figma antes de implementar la pantalla.
Si no se dispone del token, se debe pedir.
```python
# Obtener URLs de descarga (ejecutar una sola vez por imagen)
import requests

FILE_KEY = "BwGJCV0dSduKZ7wlGZGvuT"
TOKEN    = "<personal_access_token>"

r = requests.get(
    f"https://api.figma.com/v1/files/{FILE_KEY}/images",
    headers={"X-Figma-Token": TOKEN}
)
images = r.json()["meta"]["images"]
# images["<imageRef>"] → URL firmada de S3
```

Guardar cada imagen en `app/src/main/assets/<nombre_descriptivo>.png`.

### Paso 3 — Verificar transforms de imagen

Buscar en la salida del Paso 1 líneas con `*** FLIP HORIZONTAL`. Si existe, la imagen está espejada en el Figma y debe compensarse en Compose con:

```kotlin
Modifier.graphicsLayer { scaleX = -1f }
```

También verificar `scaleMode` en el fill:
- `STRETCH` → la imagen se estira para llenar el contenedor exacto
- `FILL` / `FIT` → usar el `ContentScale` equivalente

### Paso 4 — Mapear colores y tipografía

De la salida del Paso 1 extraer:

| Campo Figma | Propiedad en JSON | Uso en Compose |
|---|---|---|
| Color de fill SOLID | `fills[0].color` → convertir a hex | Usar token de `Color.kt` o definir local |
| `fontSize` | `style.fontSize` | `fontSize = X.sp` |
| `fontWeight` | `style.fontWeight` | `FontWeight(X)` — 400=Normal, 600=SemiBold, 700=Bold, 800=ExtraBold |
| `lineHeight` | `style.lineHeightPx` | `lineHeight = X.sp` |

### Paso 5 — Reconstruir el layout

**Reglas de posicionamiento:**

| Situación en Figma | Implementación en Compose |
|---|---|
| Frame con `layoutMode=VERTICAL` | `Column` con `verticalArrangement` y `horizontalAlignment` |
| Frame con `layoutMode=HORIZONTAL` | `Row` con `horizontalArrangement` y `verticalAlignment` |
| Frame sin `layoutMode` (absoluto) | `Box` como contenedor |
| Elemento con posición absoluta sobre otro | `Box` + `Modifier.offset(x.dp, y.dp)` |
| Imagen de fondo con elementos encima | `Box` con imagen primero, resto como capas sobre ella |

**Reglas para imágenes con `AsyncImage`:**

| Situación | `ContentScale` | `alignment` |
|---|---|---|
| Imagen llena el contenedor exactamente | `ContentScale.Crop` | `Alignment.Center` (default) |
| Imagen está desplazada al costado | `ContentScale.Crop` | `Alignment.CenterStart` o `Alignment.CenterEnd` |
| Imagen usa `scaleMode=STRETCH` en Figma | `ContentScale.FillBounds` | — |
| Imagen visible solo parcialmente (clipped) | Calcular el porcentaje visible y usar `fillMaxWidth(fraccion)` |

### Paso 6 — Regla de completitud

**Ningún elemento visible en el diseño Figma puede omitirse.** Esto incluye:
- Tarjetas flotantes con decoración (emojis, íconos, texto)
- Home Indicator (barra blanca al fondo)
- Logos y marcas de agua
- Gradientes de transición sobre imágenes
- Status bar decorativa

Si un elemento es muy complejo de implementar pixel-perfect, implementarlo aproximado pero **siempre presente**.

---

## 3. Persistencia: DataStore (`UserPreferences.kt`)

| Clave | Tipo | Descripción |
|---|---|---|
| `auth_token` | `String` | Token Bearer para autenticar peticiones API |
| `has_seen_onboarding` | `Boolean` | `false` en primera instalación → muestra Onboarding; `true` → salta a Login |

**Flujo de lectura en SplashViewModel:**
```kotlin
val token             = userPreferences.authToken.first()
val hasSeenOnboarding = userPreferences.hasSeenOnboarding.first()

destination = when {
    !token.isNullOrEmpty() -> SplashDestination.Home
    !hasSeenOnboarding     -> SplashDestination.Onboarding
    else                   -> SplashDestination.Login
}
```

---

## 4. Flujo de Navegación

```
App inicio
└── splash
      ├─ token válido       → home
      ├─ primera apertura   → onboarding
      │       ├─ "Sign up for free" → register → home
      │       └─ "Log In"           → login    → home
      └─ volvió sin token   → login
              ├─ login exitoso      → home
              └─ "Registrarse"      → register → home
```

Rutas en `AppNavigation.kt`: `splash`, `onboarding`, `login`, `register`, `home`.

**Regla de backstack:** al navegar desde splash u onboarding, siempre usar `popUpTo(...) { inclusive = true }` para que el usuario no pueda volver a esas pantallas con el botón Atrás.

---

## 5. API Endpoints

Base URL: `https://lendly-api.com/api/`
Headers obligatorios en todas las llamadas: `x-api-key: 123456789` + `Authorization: Bearer <token>` (excepto login y registro).

### 5.1 Autenticación

| Método | Endpoint | Body | Response |
|---|---|---|---|
| POST | `/auth/login` | `{email, password}` | `{token, userId, email}` |
| POST | `/auth/create` | `{firstName, lastName, dni, email, password}` | `{status, message}` |

### 5.2 Préstamos

| Método | Endpoint | Body | Response |
|---|---|---|---|
| GET | `/loans` | — | `List<LoanResponse>` |
| POST | `/loans/apply` | `{amount, installments}` | `{loanId, status, monthlyPayment}` |

### 5.3 Tienda

| Método | Endpoint | Body | Response |
|---|---|---|---|
| GET | `/products` | — | `List<ProductResponse>` |
| POST | `/purchases/create` | `{productId, installments}` | `{purchaseId, status}` |

### 5.4 Perfil y Transacciones

| Método | Endpoint | Body | Response |
|---|---|---|---|
| GET | `/users/{id}` | — | `{id, firstName, lastName, creditScore, balance, creditLimit}` |
| PUT | `/users/{id}` | `{email}` | `{status}` |
| POST | `/users/cash-in` | `{amount}` | `{newBalance}` |
| GET | `/transactions` | — | `List<TransactionResponse>` |

---

## 6. Arquitectura de ViewModels

Cada pantalla sigue el patrón `UiState` con clase sellada:

```kotlin
sealed class LoginUiState {
    object Idle    : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val token: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
```

| ViewModel | Pantalla | Responsabilidad principal |
|---|---|---|
| `SplashViewModel` | SplashScreen | Leer token + flag onboarding → emitir `SplashDestination` |
| `OnboardingViewModel` | OnboardingScreen | Persistir `has_seen_onboarding = true` antes de navegar |
| `LoginViewModel` | LoginScreen | Validar inputs → POST `/auth/login` → guardar token |
| `RegisterViewModel` | RegisterScreen | Validar campos → POST `/auth/create` |
| `HomeViewModel` | HomeScreen | GET `/users/{id}` → exponer score, balance, límite |
| `LoanViewModel` | LoanSimulatorScreen | Simulación local + POST `/loans/apply` |
| `ShopViewModel` | ShopScreen | GET `/products` → `StateFlow<List<Product>>` |
| `TransactionHistoryViewModel` | HistoryScreen | GET `/transactions` → filtrar por tipo |
| `ProfileViewModel` | ProfileScreen | GET+PUT `/users/{id}` + POST `/users/cash-in` + logout |