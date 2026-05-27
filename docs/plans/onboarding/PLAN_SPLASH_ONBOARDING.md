# Plan de Implementación — Splash Screen & Onboarding
**Fecha:** Mayo 2026  
**Branch:** `feature/set-up`

---

## 1. Feature Analysis

### Pantallas involucradas

| Pantalla | Figma Node ID | Tamaño |
|---|---|---|
| Splash Screen | `35:1097` | 393×852dp |
| Onboarding 1 | `121:1311` | 393×852dp |
| Onboarding 2 | `189:3550` | 393×852dp |
| Onboarding 3 | `189:3591` | 393×852dp |

### Datos de diseño extraídos del Figma

**Splash Screen**
- Background: `#E5F5EA` (`FigmaMintSplash`)
- Logo (Frame 134): `242.8×83.3dp`, 3 rectángulos en cascada cada uno `213.8×47.1dp`, `cornerRadius=3.62dp`
  - Rect 3 (frente): offset `(0,0)`, fill `#7BF179`, opacity=1.0
  - Rect 4 (medio): offset `(14.5, 18.1)`, fill `#7BF179`, **opacity=0.34**
  - Rect 5 (fondo/sombra): offset `(29, 36.2)`, fill `#163300`, opacity=1.0
- Home Indicator: `393×34dp`, píldora negra, fondo de pantalla

**Onboarding Screens (estructura compartida)**
- Background: `#002203` (`FigmaDarkForest`)
- Área de ilustración superior: `698×481dp` (desborda el ancho de pantalla → se clipea a 393)
  - Gradiente backdrop (Frame 219, `674×333dp` at `(24,148)`): `#8FFF85→#39A0FF`
  - Imagen hero: posicionada en y=148 desde tope de pantalla
  - Tarjetas decorativas flotantes (emoji bubbles, balance cards, ícono circular)
  - Overlay de status bar + logo Lendly (`116×40dp`) en el tope
- Sección de contenido inferior: `393×339dp` desde y=513

**Page Indicators (Frame 9): `52×12dp`**
- 3 elipses de `12×12dp` cada una, gap=8dp entre bordes
- Dot activo: `#7BF179` opacity=1.0
- Dot inactivo: `#EADDFF` **opacity=0.16**

**Botones CTA:**
- Width: `361dp` (padding horizontal 16dp en pantalla 393dp), height: `48dp`, cornerRadius: `100dp`
- Primario: fill `#7BF179`, texto `#102000`, Inter SemiBold 14sp
- Secundario ("Log In"): sin fill (transparente), texto `#FFFFFF`, Inter SemiBold 14sp

**Contenido por página:**

| Página | Título | Subtítulo | Gap outer | Gap inner |
|---|---|---|---|---|
| 0 | "Quick loans" | "Trusted for easy,\nfast loan approvals." | 81dp | 32dp |
| 1 | "Loan Product\nIn-App" | "Many products to loan." | 87dp | 24dp |
| 2 | "Track & Pay\nEasily" | — (vacío) | 71dp | — (2 botones) |

### Componentes reutilizables
- `LendlyLogo` — dibujado en Canvas, escala proporcionalmente (splash 243×83, onboarding 116×40)
- `PageIndicatorDots` — 3 dots, color state-driven
- `LendlyPrimaryButton` — relleno verde, forma de píldora
- `LendlySecondaryButton` — transparente con borde, forma de píldora
- `HomeIndicatorBar` — barra píldora inferior (negra en fondo claro, blanca en fondo oscuro)
- `OnboardingIllustration` — área superior específica por página
- `OnboardingBottomContent` — sección inferior data-driven

### Assets necesarios

| Asset | imageRef Figma | Usado en |
|---|---|---|
| `onboarding_1.png` | `30e61e1c...` | Onboarding página 0 |
| `onboarding_2.png` | `6b63c4ac...` | Onboarding página 1 |
| `onboarding_3.png` | `aedadd...` | Onboarding página 2 |
| `avatar.png` | `d6c8be...` | Decoración tarjeta flotante |
| `product_1.png` | `be53c0...` | Onboarding 2 tarjeta producto |
| `product_2.png` | `0f9255...` | Onboarding 2 tarjeta producto |

> ✅ **Todos descargados** a `app/src/main/assets/` via Figma API.

### Fuentes

| Archivo | R.font | Uso |
|---|---|---|
| `montserrat_extrabold.ttf` | `R.font.montserrat_extrabold` | Títulos onboarding 32sp |
| `montserrat_bold.ttf` | `R.font.montserrat_bold` | — |
| `montserrat_semibold.ttf` | `R.font.montserrat_semibold` | — |
| `montserrat_regular.ttf` | `R.font.montserrat_regular` | — |
| `inter_semibold.ttf` | `R.font.inter_semibold` | Labels de botones 14sp |
| `inter_medium.ttf` | `R.font.inter_medium` | Texto secundario |
| `inter_regular.ttf` | `R.font.inter_regular` | Subtítulos onboarding 22sp |
| `inter_bold.ttf` | `R.font.inter_bold` | — |

> ✅ **Todos descargados** a `app/src/main/res/font/` desde Google Fonts.

### Flujo de navegación

```
SplashRoute (auto-navega después de 1.5s)
  ├─ token válido            → HomeRoute      (limpia stack)
  ├─ !hasSeenOnboarding      → OnboardingRoute (limpia stack)
  └─ sin sesión              → LoginRoute     (limpia stack)

OnboardingRoute (HorizontalPager, 3 páginas)
  ├─ Páginas 0+1: "Get Started" → pagerState.animateScrollToPage(+1)
  └─ Página 2: "Log In"         → LoginRoute (limpia stack)
               "Sign up for free" → RegisterRoute (limpia stack)
```

### State management
- `SplashViewModel`: lee DataStore → emite `SplashDestination` (sealed class)
- `OnboardingViewModel`: persiste `has_seen_onboarding=true` al salir

---

## 2. Compose Architecture

```
presentation/
├── navigation/
│   └── AppNavigation.kt         — NavDisplay, NavKeys, grafo de rutas
├── auth/
│   ├── SplashScreen.kt          — Branding + LaunchedEffect auto-navega
│   └── SplashViewModel.kt       — Lee DataStore, emite SplashDestination
└── onboarding/
    ├── OnboardingPage.kt        — Modelo de datos + lista de 3 páginas
    ├── OnboardingScreen.kt      — HorizontalPager + toda la UI
    └── OnboardingViewModel.kt   — Persiste has_seen_onboarding

theme/
├── Color.kt    — Tokens de color Figma completos
└── Type.kt     — MontserratFamily + InterFamily (fuentes locales)

data/local/
└── UserPreferences.kt           — DataStore: auth_token + has_seen_onboarding

di/
└── AppModule.kt                 — @Provides UserPreferences
```

---

## 3. Tareas de implementación (orden ejecutado)

1. ✅ Actualizar `theme/Color.kt` — tokens de color Figma
2. ✅ Actualizar `app/build.gradle.kts` — agregar `material-icons-extended` y `foundation`
3. ✅ Actualizar `theme/Type.kt` — MontserratFamily + InterFamily con TTFs locales
4. ✅ Crear `data/local/UserPreferences.kt` — DataStore wrapper
5. ✅ Crear `di/AppModule.kt` — módulo Hilt
6. ✅ Actualizar `NavigationKeys.kt` — 5 rutas: Splash, Onboarding, Login, Register, Home
7. ✅ Crear `presentation/navigation/AppNavigation.kt` — grafo de navegación completo
8. ✅ Actualizar `MainActivity.kt` — `@AndroidEntryPoint`, llama `AppNavigation()`
9. ✅ Crear `presentation/auth/SplashViewModel.kt`
10. ✅ Crear `presentation/auth/SplashScreen.kt` — logo en Canvas, auto-navega, `HomeIndicatorBar`
11. ✅ Crear `presentation/onboarding/OnboardingPage.kt` — data model + `OnboardingLayout` sealed class
12. ✅ Crear `presentation/onboarding/OnboardingViewModel.kt`
13. ✅ Crear `presentation/onboarding/OnboardingScreen.kt` — `HorizontalPager` completo
14. ✅ Descargar imágenes hero via Figma API (`assets/`)
15. ✅ Descargar fuentes TTF de Google Fonts (`res/font/`)

---

## 4. Archivos creados / modificados

| Archivo | Estado |
|---|---|
| `theme/Color.kt` | ✅ Modificado |
| `theme/Type.kt` | ✅ Modificado |
| `theme/Theme.kt` | ✅ Modificado |
| `app/build.gradle.kts` | ✅ Modificado |
| `data/local/UserPreferences.kt` | ✅ Nuevo |
| `di/AppModule.kt` | ✅ Nuevo |
| `NavigationKeys.kt` | ✅ Modificado |
| `presentation/navigation/AppNavigation.kt` | ✅ Nuevo |
| `MainActivity.kt` | ✅ Modificado |
| `presentation/auth/SplashViewModel.kt` | ✅ Nuevo |
| `presentation/auth/SplashScreen.kt` | ✅ Nuevo |
| `presentation/onboarding/OnboardingPage.kt` | ✅ Nuevo |
| `presentation/onboarding/OnboardingViewModel.kt` | ✅ Nuevo |
| `presentation/onboarding/OnboardingScreen.kt` | ✅ Nuevo |
| `res/font/*.ttf` (×8) | ✅ Nuevo |
| `assets/onboarding_*.png` (×3) | ✅ Nuevo |
| `assets/avatar.png`, `product_*.png` | ✅ Nuevo |

---

## 5. Checklist de validación visual

| Elemento | Spec Figma | Implementación |
|---|---|---|
| Splash background | `#E5F5EA` | `FigmaMintSplash` ✅ |
| Logo size splash | 242.8×83.3dp | `DpSize(243.dp, 83.dp)` ✅ |
| Logo rect opacity mid | 0.34 | `color.copy(alpha = 0.34f)` ✅ |
| Onboarding background | `#002203` | `FigmaDarkForest` ✅ |
| Gradiente ilustración | `#8FFF85→#39A0FF` | `IllustrationGradientStart/End` ✅ |
| Dot activo | `#7BF179` opacity=1.0 | `FigmaNeonGreen` ✅ |
| Dot inactivo | `#EADDFF` opacity=0.16 | `OnboardingDotInactive.copy(0.16f)` ✅ |
| Dot size | 12×12dp, gap=8dp | `size(12.dp)` + `spacedBy(8.dp)` ✅ |
| Botón primario | fill `#7BF179`, texto `#102000` | `FigmaNeonGreen` / `OnPrimaryGreen` ✅ |
| Botón size | 361×48dp | `fillMaxWidth()` + `height(48.dp)` ✅ |
| Botón corner | 100dp (píldora completa) | `RoundedCornerShape(100.dp)` ✅ |
| Título font | Montserrat 32sp ExtraBold | `MontserratFamily` + `ExtraBold` ✅ |
| Subtítulo font | Inter 22sp Regular lh=28sp | `InterFamily` + `Normal` ✅ |
| Gap OB1 | outer=81, inner=32 | `Arrangement.spacedBy(81/32.dp)` ✅ |
| Gap OB2 | outer=87, inner=24 | `Arrangement.spacedBy(87/24.dp)` ✅ |
| Gap OB3 | outer=71 | `Arrangement.spacedBy(71.dp)` ✅ |
| Overflow ilustración | 698dp clipeado a 393 | `clipToBounds()` ✅ |
| Pager swipe | entre 3 páginas | `HorizontalPager` ✅ |
| "Get Started" avanza | p0→p1→p2 | `animateScrollToPage(+1)` ✅ |
| Página 3 doble CTA | "Log In" + "Sign up" | `OnboardingLayout.DoubleCta` ✅ |
| Persiste onboarding visto | antes de navegar | `OnboardingViewModel.onNavigateAway()` ✅ |
| No vuelve atrás | Splash/Onboarding | `navigateClearingStack()` ✅ |

---

## 6. Riesgos conocidos / pendientes

| Riesgo | Severidad | Nota |
|---|---|---|
| Posiciones de tarjetas flotantes | Media | Aproximadas visualmente; extraer de `absoluteBoundingBox` si se requiere precisión |
| Dirección del gradiente | Baja | Implementado como diagonal TL→BR; verificar contra Figma si es incorrecto |
| Status bar icon colour | Baja | `enableEdgeToEdge()` lo maneja automáticamente; puede necesitar `WindowInsetsController` explícito |
| Pantallas Login/Register/Home | — | Rutas registradas en `AppNavigation.kt`, muestran placeholder hasta implementar |
