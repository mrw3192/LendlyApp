# Plan de Implementación — Shop Flow
**Fecha:** Junio 2026
**Branch:** `feature/shop-screens`

---

## 1. Contexto

La pantalla Shop (Tienda) es la tercera sección de la navegación inferior de LendlyApp.  
Su objetivo es mostrar un catálogo de productos financiables y permitir al usuario explorar el detalle de cada uno para iniciar una compra a cuotas.

Estado actual: `ShopScreen.kt` es un placeholder de `Box` + `Text("Shop")`.

Pantallas a implementar:
1. **Shop Page** — catálogo principal con barra de búsqueda, tarjeta promocional, categorías, marcas y productos
2. **Product-screen** — detalle de producto con imagen, precio, secciones expandibles y CTA de compra

---

## 2. Análisis de Diseño Figma

### 2.1 Pantallas y dimensiones

| Frame | Tamaño | Ubicación en figma.json |
|---|---|---|
| `Shop page` | 393×1430dp | Pages → Shop (SECTION) |
| `Product-screen` | 393×1913dp | Pages → Shop (SECTION) |
| `Search screen` | 393×1004dp | Pages → Shop (SECTION) _(fuera de alcance v1)_ |
| `Filter Screen` | 393×1004dp | Pages → Shop (SECTION) _(fuera de alcance v1)_ |

### 2.2 Shop Page — jerarquía de layout

```
FRAME 'Shop page'  fill=#FFFFFF
└── FRAME 'Frame 262'  layout=VERTICAL  gap=30dp
    ├── FRAME 'Frame 260'  layout=VERTICAL
    │   ├── Status Bar (sistema)
    │   └── FRAME 'Frame 63'  layout=VERTICAL  gap=16dp
    │       ├── AppBar  (Lendly Logo + trailing icons)
    │       └── FRAME 'Frame 62'  layout=VERTICAL  gap=32dp  ← contenido scrollable
    │           ├── Header-advertisement  (search bar + promo card)   gap=16dp
    │           ├── Shop_category-SC      (título + chips de categoría)
    │           ├── Popular_brands-SC     (título + tarjetas de marca)
    │           ├── Recommended-SC        (título + tarjetas de producto)
    │           └── Recommended-SC        (Best Sellers)
    └── FRAME 'Frame 261'  (Navigation Bar)
```

### 2.3 Especificaciones visuales — Shop Page

#### AppBar
- Lendly Logo centrado: `Frame 134` → 58×20dp, usa LendlyLogo existente
- Leading icon (back): 48×48, container 40×40 r=100
- Trailing icon row: 48×48 × 3 (functionally decorativo en v1)

#### Search Bar
- Tamaño: 361×56dp — `r=8` — stroke `#E5E2E1 w=1`
- Placeholder: "Search for product" — Input text dentro de Content frame
- Trailing: botón 48×48 `fill=#7BF179 r=4` (icono filtro)

#### Promotional Card
- Tamaño: 361×250dp — `fill=#102000` — `r=16`
- Imagen hero: 209×188dp — imageRef `87cc7e6ae36438af` → **`img_87cc7e6ae36438af.png`** ✅
- Decoración verde: Rectangle 2 (`117×117dp`, `fill=#7BF179`) + sombras blur
- Título: `"The New Shoes"` — 28sp fw=600 `#FFFFFF`
- Subtítulo: `"Shop this season's Top Silhouette"` — 16sp fw=400 `#FFFFFF`
- Botón "Shop Now": 118×32dp — `fill=#7BF179 r=8` — texto 14sp fw=600 `#102000`
- Indicadores: 3 puntos blancos (`6×6dp`) — gap=6dp — offset=`(40, 222)`

#### Sección "Shop By Category"
- Título: `"Shop By Category"` — 22sp fw=600 `#171D1E`
- Chip "See All": `94×32dp` — `r=1000` — layout=HORIZONTAL
- Category items row: layout=HORIZONTAL gap=8dp (scrollable horizontal)
  - Cada ítem: 100×124dp — card interior: 100×96dp `fill=#FCF8F8 r=12` pad=(8,16,8,16) + label
  - Labels: `"Phone"`, `"Headphones"`, `"Apparel"` — 14sp fw=600 `#454745`

#### Sección "Popular Brands"
- Título: `"Popular Brands"` — 22sp fw=600 `#171D1E`
- Brand cards: 150×130dp — `fill=#FCF8F8 r=12` — horizontal scroll
  - Image area: 150×96dp — fill=IMG (brand logo)
  - Label area: 150×34dp — layout=HORIZONTAL pad=(8,8,8,8) gap=10
  - Assets: `img_c6d61f7f62e4010f.png` (Apple), `img_a2fef0980c1cf4a7.png` (Jordan), `img_34bbdb89fdac37a8.png` (Adidas) ✅

#### Sección "Recommended For You"
- Título: `"Recommended For You"` — 22sp fw=600 `#171D1E`
- Product cards: 132×145dp — `fill=#FCF8F8 r=12` — pad=(16,16,16,16) gap=8
  - Imagen: 85×65dp — fill=IMG
  - Frame 22: 100×40dp — nombre + precio
  - Nombre: "iPhone 12 Pro..." — texto corto
  - Precio: "₱1,200 x 24 mo"
  - Assets: `img_1d9e70f017321361.png`, `img_f4ce8621a75bef92.png`, `img_4212ff9d8c27fb90.png` ✅

#### Sección "Best Sellers"
- Estructura idéntica a "Recommended For You"
- Productos: Surface Laptop, iPhone, PS4, iPhone
- Assets: `product_1.png` (be53c076), `img_84afada02ee9075f.png`, `img_0feafd9ff159fc89.png` ✅

#### Navigation Bar (bottom)
- Delegado a `BottomNavBar` existente — ✅ ya implementado

---

### 2.4 Product-screen — jerarquía de layout

```
FRAME 'Product-screen'  fill=#FFFFFF  layout=VERTICAL
└── FRAME 'Frame 265'  layout=VERTICAL
    └── FRAME 'Frame 91'  layout=VERTICAL  gap=32dp
        ├── FRAME 'Frame 90'  layout=VERTICAL  gap=16dp
        │   ├── FRAME 'Frame 81'  (TopAppBar + green banner)  layout=VERTICAL
        │   │   ├── FRAME 'Frame 78'  (StatusBar + TopAppBar)
        │   │   └── FRAME 'Frame 79'  fill=#7BF179  48h  (feature chips)
        │   └── FRAME 'Frame 85'  layout=VERTICAL  gap=8dp
        │       ├── FRAME 'Frame 82'  (product image + counter chip)
        │       └── FRAME 'Frame 84'  layout=VERTICAL  gap=16dp
        │           ├── FRAME 'Frame 32'  (price + name)
        │           ├── Divider (#E5F5EA w=8)
        │           ├── Dropdown 1: WHERE DO YOU WANT TO SHOP?
        │           ├── Divider
        │           ├── Dropdown 2: MARKETPLACE PARTNER MERCHANTS
        │           ├── Divider
        │           ├── Dropdown 3: FEATURES
        │           ├── Divider
        │           └── Dropdown 4: PRODUCT SPECIFICATIONS
        └── FRAME 'Navigation bar - checkout'  (bottom bar con precio + CTA)
```

### 2.5 Especificaciones visuales — Product-screen

#### TopAppBar
- Height: 64dp — layout=HORIZONTAL pad=(8,4,8,4) gap=6
- Leading icon: back arrow — 48×48 container r=100
- Headline: `"Apple iPhone 12 Pro Max"` — 16sp fw=500 `#1D1B20`
- Trailing icon: favorite — 48×48 container r=100

#### Green Feature Banner (Frame 79)
- Height: 48dp — fill=`#7BF179`
- Inner Frame 80: 361×28dp — layout=HORIZONTAL gap=8
- 3 chips con ícono + texto, gap=8 entre cada uno:
  - `mood` icon + `"Low interest"` — 11sp fw=500 `#1D192B`
  - `sell` icon + `"0% Installment"` — 11sp fw=500 `#1D192B`
  - `package_2` icon + `"Easy pick-up"` — 11sp fw=500 `#1D192B`

#### Product Image Area (Frame 82)
- Container: 393×219dp
- Imagen: 178×225dp — imageRef `0f92556e1c058f3ae249` → **`product_2.png`** ✅ — offset (+108, 0) 
- Counter chip "1/4": 42×28dp — `r=1000` — stroke `#6A6C6A w=1` — 11sp fw=500 `#6A6C6A`
  - Posición absoluta: offset=`(+335, +175)` desde la imagen

#### Price & Name Section (Frame 32)
- pad=(t=,r=16,b=,l=16) gap=8
- `"From as low as"` — 12sp fw=500 `#002203` (= `FigmaDarkForest`)
- Price row (gap=8):
  - `"$1,200"` — 28sp fw=600 `#1F3701` ← nuevo token `ShopPriceGreen`
  - `"per month"` — 11sp fw=500 `#6A6C6A` (= `SubtitleGray`)
- Product name: `"Apple iPhone 15 Pro Max 256GB, Rose Gold"` — 16sp fw=600 `#000000`

#### Dividers
- `stroke=#E5F5EA w=8` — div horizontal full-width — usar `HorizontalDivider(thickness=8.dp, color=FigmaMintSplash)`

#### Dropdown 1: WHERE DO YOU WANT TO SHOP?
- Header row: input text `"WHERE DO YOU WANT TO SHOP?"` (12sp fw=500 #000000) + Step label `"Step 3"` tag (#E5F5EA bg)
- Content card: 361×56dp `r=8`:
  - Location icon (`location_on`, fill=`#122300`)
  - `"Davao City, Davao del Sur"` — 16sp fw=600 `#102000`
  - Chevron down icon (`keyboard_arrow_down`, fill=`#1F3701`)

#### Dropdown 2: MARKETPLACE PARTNER MERCHANTS
- 3 merchant rows (361×136dp each, r=8):
  - Avatar: 40×40dp (imageRef)
  - Merchant name (e.g. "Power Max Center")
  - Availability chip: `fill=#E5F5EA r=4`
  - Price: "From $1,200 x 12 months" + "$1,800 total price" + "65% Downpayment"
  - Chevron down icon
- Merchants: Power Max Center (`img_d8472cb9ce98068d.png`), The Loop (`img_0125681952ba44a5.png`), I-Mac Center (`img_adec3785fe24bfc2.png`)

#### Dropdown 3: FEATURES
- Section header "FEATURES" + chevron
- Content:
  - "How To Apply For A Loan" + instructions
  - "Disclaimer" + text

#### Dropdown 4: PRODUCT SPECIFICATIONS
- Section header "PRODUCT SPECIFICATIONS" + chevron
- Content:
  - "Chip" + spec details
  - "Camera" + spec details

#### Bottom Bar (Navigation bar - checkout)
- 393×80dp — fill=`#FFFFFF` — stroke=`#E5E2E1 w=1`
- Left side (242×56dp): "From as low as" + "$1,200" (24sp fw=600 #1F3701) + "per month"
- Right side: "Continue" button (111×48dp) — `fill=#7BF179 r=100` — text `#102000` 14sp fw=600

---

## 3. Arquitectura

### 3.1 Archivos a crear

| Archivo | Descripción |
|---|---|
| `model/Product.kt` | Data class entidad producto |
| `model/ShopModels.kt` | DTOs API (ProductResponse, PurchaseRequest, PurchaseResponse) |
| `data/repository/ProductRepository.kt` | Interfaz del repositorio |
| `data/repository/ProductRepositoryImpl.kt` | Implementación con Retrofit + fallback mock |
| `shared/LendlyApiService.kt` | Interfaz Retrofit con endpoints de productos y compras |
| `viewmodel/ShopViewModel.kt` | ViewModel con StateFlow<ShopUiState> |
| `ui/screens/shop/ShopUiState.kt` | Sealed class de estados (Idle/Loading/Success/Error) |
| `ui/screens/shop/ProductDetailScreen.kt` | Pantalla de detalle de producto |

### 3.2 Archivos a modificar

| Archivo | Cambio |
|---|---|
| `ui/screens/shop/ShopScreen.kt` | Reemplazar placeholder con implementación completa |
| `di/AppModule.kt` | Añadir bindings de LendlyApiService y ProductRepository |
| `navigation/MainScaffold.kt` | Añadir estado shopProduct para navegación interna |
| `ui/theme/Color.kt` | Añadir `ShopPriceGreen = Color(0xFF1F3701)` |

### 3.3 Modelos de datos

```kotlin
// model/Product.kt
data class Product(
    val id: String,
    val name: String,
    val shortName: String,
    val imageAsset: String,      // nombre en assets/
    val monthlyPayment: Double,
    val totalPrice: Double,
    val category: String,
    val brand: String,
)

// model/ShopModels.kt
data class ProductResponse(
    val id: String,
    val name: String,
    @SerializedName("monthly_payment") val monthlyPayment: Double,
    @SerializedName("total_price") val totalPrice: Double,
    val category: String,
    val brand: String,
)

data class PurchaseRequest(
    @SerializedName("productId") val productId: String,
    val installments: Int,
)

data class PurchaseResponse(
    @SerializedName("purchaseId") val purchaseId: String,
    val status: String,
)
```

### 3.4 API Service

```kotlin
// shared/LendlyApiService.kt
interface LendlyApiService {
    @GET("products")
    suspend fun getProducts(): Response<List<ProductResponse>>

    @POST("purchases/create")
    suspend fun createPurchase(@Body request: PurchaseRequest): Response<PurchaseResponse>
}
```

### 3.5 ViewModel

```kotlin
// viewmodel/ShopViewModel.kt
@HiltViewModel
class ShopViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ShopUiState>(ShopUiState.Loading)
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()

    init { loadProducts() }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = ShopUiState.Loading
            productRepository.getProducts()
                .onSuccess { _uiState.value = ShopUiState.Success(it) }
                .onFailure { _uiState.value = ShopUiState.Error(it.message ?: "Error") }
        }
    }
}
```

### 3.6 UiState

```kotlin
// ui/screens/shop/ShopUiState.kt
sealed class ShopUiState {
    data object Idle    : ShopUiState()
    data object Loading : ShopUiState()
    data class Success(val products: List<Product>) : ShopUiState()
    data class Error(val message: String) : ShopUiState()
}
```

### 3.7 Navegación interna (Shop → ProductDetail)

Dado que `MainScaffold` usa un tab switcher simple (sin NavBackStack por tab), la navegación entre Shop y ProductDetail se maneja con estado local en `MainScaffold`:

```kotlin
// MainScaffold.kt — nuevo estado
var shopSelectedProduct by remember { mutableStateOf<Product?>(null) }

// Shop tab content:
BottomNavTab.Shop -> {
    if (shopSelectedProduct != null) {
        ProductDetailScreen(
            product = shopSelectedProduct!!,
            onBack = { shopSelectedProduct = null },
        )
    } else {
        ShopScreen(
            onProductClick = { shopSelectedProduct = it },
        )
    }
}
```

El back del sistema es manejado con `BackHandler` dentro de `ProductDetailScreen`.

---

## 4. Estructura de Composables

### ShopScreen.kt
```
ShopScreen(viewModel, onProductClick)
├── Scaffold
│   └── LazyColumn (scrollable content)
│       ├── ShopAppBar()
│       ├── SearchBar()
│       ├── PromotionalCard()
│       ├── CategorySection()         ← LazyRow
│       ├── BrandsSection()           ← LazyRow
│       ├── ProductSection("Recommended For You") ← LazyRow de ProductCard
│       └── ProductSection("Best Sellers")        ← LazyRow de ProductCard
└── (BottomNavBar manejado por MainScaffold)
```

### ProductDetailScreen.kt
```
ProductDetailScreen(product, onBack, viewModel)
├── Scaffold
│   ├── topBar: ProductTopAppBar(title, onBack)
│   └── content: Column + VerticalScroll
│       ├── GreenFeatureBanner()
│       ├── ProductImageArea()
│       ├── PriceSection()
│       ├── HorizontalDivider
│       ├── LocationDropdown()
│       ├── HorizontalDivider
│       ├── MerchantsDropdown()
│       ├── HorizontalDivider
│       ├── FeaturesDropdown()
│       ├── HorizontalDivider
│       └── SpecificationsDropdown()
│   └── bottomBar: CheckoutBottomBar(onContinue)
```

---

## 5. Assets necesarios

| Asset | imageRef Figma (12 chars) | Estado |
|---|---|---|
| `img_87cc7e6ae36438af.png` | `87cc7e6ae364...` | ✅ en assets/ |
| `img_c6d61f7f62e4010f.png` | `c6d61f7f62e4...` | ✅ en assets/ (Apple) |
| `img_a2fef0980c1cf4a7.png` | `a2fef0980c1c...` | ✅ en assets/ (Jordan) |
| `img_34bbdb89fdac37a8.png` | `34bbdb89fdac...` | ✅ en assets/ (Adidas) |
| `img_1d9e70f017321361.png` | `1d9e70f01732...` | ✅ en assets/ |
| `img_f4ce8621a75bef92.png` | `f4ce8621a75b...` | ✅ en assets/ |
| `img_4212ff9d8c27fb90.png` | `4212ff9d8c27...` | ✅ en assets/ |
| `product_1.png` | `be53c076...` | ✅ en assets/ |
| `img_84afada02ee9075f.png` | `84afada02ee9...` | ✅ en assets/ |
| `img_0feafd9ff159fc89.png` | `0feafd9ff159...` | ✅ en assets/ |
| `product_2.png` | `0f92556e...` | ✅ en assets/ (product detail) |
| `img_d8472cb9ce98068d.png` | `d8472cb9ce98...` | ✅ en assets/ (merchant 1) |
| `img_0125681952ba44a5.png` | `0125681952ba...` | ✅ en assets/ (merchant 2) |
| `img_adec3785fe24bfc2.png` | `adec3785fe24...` | ✅ en assets/ (merchant 3) |

> Todos los assets ya están descargados en `app/src/main/assets/`

---

## 6. Tokens de Color Nuevos

```kotlin
// Color.kt — añadir
/** #1F3701 — Precio principal en Shop y Product Detail */
val ShopPriceGreen = Color(0xFF1F3701)
```

Tokens existentes reutilizados:
- `FigmaLightBg` (#FCF8F8) → fondo tarjetas categoría/producto
- `FigmaLightText` (#171D1E) → títulos de sección
- `FigmaNeonGreen` (#7BF179) → search trailing, promo button, feature banner
- `OnPrimaryGreen` (#102000) → texto sobre fondo verde
- `FigmaDarkForest` (#002203) → "From as low as" label
- `SubtitleGray` (#6A6C6A) → "per month", labels secundarios
- `FormLabel` (#454745) → category/brand labels
- `FigmaMintSplash` (#E5F5EA) → dividers, availability chips

---

## 7. Fases de Implementación

### Fase 1 — Data Layer
1. Crear `model/Product.kt` y `model/ShopModels.kt`
2. Crear `shared/LendlyApiService.kt`
3. Crear `data/repository/ProductRepository.kt` (interfaz)
4. Crear `data/repository/ProductRepositoryImpl.kt` (con fallback mock)
5. Actualizar `di/AppModule.kt`

### Fase 2 — ViewModel
1. Crear `ui/screens/shop/ShopUiState.kt`
2. Crear `viewmodel/ShopViewModel.kt`

### Fase 3 — ShopScreen (catálogo)
1. Actualizar `ui/theme/Color.kt` (token ShopPriceGreen)
2. Implementar `ShopScreen.kt` con todos los composables

### Fase 4 — ProductDetailScreen
1. Crear `ui/screens/shop/ProductDetailScreen.kt`

### Fase 5 — Navegación y Wiring
1. Actualizar `navigation/MainScaffold.kt`

---

## 8. Datos mock locales (estáticos)

Las secciones de **categorías** y **marcas** son contenido editorial fijo del diseño, no provienen de la API.  
Los **productos** (Recommended y Best Sellers) intentarán cargarse del endpoint `/products`; en caso de error, usarán una lista mock local con los assets ya descargados.

```kotlin
// Categorías (estáticas)
val shopCategories = listOf(
    ShopCategory("Phone",       "comp_category_items.svg"),
    ShopCategory("Headphones",  "comp_category_items.svg"),
    ShopCategory("Apparel",     "comp_category_items.svg"),
)

// Marcas (estáticas)
val popularBrands = listOf(
    Brand("Apple",   "img_c6d61f7f62e4010f.png"),
    Brand("Jordan",  "img_a2fef0980c1cf4a7.png"),
    Brand("Adidas",  "img_34bbdb89fdac37a8.png"),
)

// Productos mock (fallback)
val mockProducts = listOf(
    Product("1", "Apple iPhone 12 Pro", "iPhone 12 Pro", "img_1d9e70f017321361.png", 1200.0, 28800.0, "Phone", "Apple"),
    Product("2", "Apple iPhone 12 Pro", "iPhone 12 Pro", "img_f4ce8621a75bef92.png", 1200.0, 28800.0, "Phone", "Apple"),
    Product("3", "Apple iPhone 12 Pro", "iPhone 12 Pro", "img_4212ff9d8c27fb90.png", 1200.0, 28800.0, "Phone", "Apple"),
    Product("4", "Surface Laptop", "Surface Laptop", "product_1.png",               1200.0, 28800.0, "Electronics", "Microsoft"),
    Product("5", "Apple iPhone 12", "iPhone 12",      "img_84afada02ee9075f.png",  1200.0, 28800.0, "Phone", "Apple"),
    Product("6", "PS4 PlayStation",  "PS4",            "img_0feafd9ff159fc89.png",  1200.0, 28800.0, "Gaming", "Sony"),
)
```

---

## 9. Ambigüedades e Issues Detectados

| Ítem | Descripción | Decisión |
|---|---|---|
| Iconos de categorías | Las category cards tienen imágenes internas que no tienen imageRef en el Figma (son componentes SVG) | Usar `comp_category_items.svg` o `Icons.Default.Phone/Headphones/ShoppingBag` como placeholder |
| Sistema de back button | `MainScaffold` no tiene backstack por tab — el back del sistema no retrocede dentro del tab | Implementar con `BackHandler` en `ProductDetailScreen` |
| Precio moneda | Figma usa tanto `$` como `₱` para los mismos productos | Usar `$` (dólar) como se muestra en Product-screen |
| Datos API vacíos | El mock server puede no retornar productos (lista vacía) | Mostrar fallback con `mockProducts` locales |
| Dropdown expandible | Los dropdowns del product detail se muestran expandidos en Figma | Implementar como acordeones con `AnimatedVisibility`, todos expandidos por defecto |
| Scroll del contenido del product detail | El frame mide 1913dp (muy alto) — implica scroll vertical | Usar `Scaffold` + `Column` con `verticalScroll` para el cuerpo |

---

## 10. Checklist de Validación

### Visual
- [ ] Background blanco (#FFFFFF) en toda la pantalla Shop
- [ ] Search bar con borde gris y botón verde trailing
- [ ] Promotional Card con fondo oscuro (#102000), imagen, título, botón y 3 dots
- [ ] Categorías con tarjetas blancas redondeadas y label debajo
- [ ] Marcas con imagen de logo y nombre en tarjeta blanca
- [ ] Tarjetas de producto con imagen centrada y precio
- [ ] Product detail: banner verde con 3 chips de features
- [ ] Imagen del producto centrada con chip contador "1/4"
- [ ] Precio en verde oscuro (#1F3701) "From as low as"
- [ ] 4 secciones expandibles con divisor verde menta entre ellas
- [ ] Bottom bar con precio + botón "Continue" verde pill

### Funcional
- [ ] ShopScreen muestra estado de carga
- [ ] ShopScreen muestra lista de productos de API o fallback mock
- [ ] Tap en producto navega a ProductDetailScreen
- [ ] Botón back en ProductDetailScreen regresa a ShopScreen
- [ ] Back button del sistema regresa desde ProductDetailScreen
- [ ] Botón "Continue" en Product Detail (puede ser noop en v1)

### Técnico
- [ ] Sigue patrón UiState sealed class (SPEC_TECNICO §6)
- [ ] ViewModel inyectado con Hilt (@HiltViewModel)
- [ ] Repositorio provisto en AppModule
- [ ] Sin hex hardcodeados (todo via tokens de Color.kt)
- [ ] Sin duplicación de composables con los existentes
- [ ] LazyRow para scrolls horizontales (no Row fijo)

---

## 11. Deuda Técnica Conocida

- **Search/Filter screens** no implementadas en v1 (el search bar es decorativo)
- **Purchase flow** (POST /purchases/create) no implementado en v1 — el botón "Continue" será un noop con Toast
- **Categorías con íconos reales** requieren SVGs dedicados por categoría
- **Back system button** se maneja con `BackHandler`; una solución más robusta usaría Navigation Compose dentro del tab
