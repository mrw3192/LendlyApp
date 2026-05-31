# Plan de Implementación — Módulo de Préstamos (Loans)

## 1. Introducción y Objetivos
Este documento detalla la estrategia de desarrollo para el módulo de préstamos de **LendlyApp**. El objetivo es construir una solución robusta y escalable, aplicando los patrones de arquitectura y buenas prácticas de desarrollo Android moderno vistos en la cursada.

Se implementarán las siguientes 4 pantallas clave:
1. **Loan Screen — Info**: Información y condiciones.
2. **Loan Screen — Form**: Solicitud de crédito.
3. **Active Loan**: Gestión del préstamo en curso.
4. **Successful Transaction**: Feedback de éxito.

---

## 2. Arquitectura de Software
Se utilizará una arquitectura multicapa (Clean Architecture / MVVM) para garantizar el desacoplamiento de componentes:

*   **Capa de UI (Compose)**: Vistas declarativas que observan el estado del ViewModel.
*   **Capa de Presentación (ViewModel)**: Manejo de estados de UI con `mutableStateOf` y ejecución de corrutinas en `viewModelScope`.
*   **Capa de Dominio (Use Cases & Models)**: Lógica de negocio pura e interfaces de repositorios.
*   **Capa de Datos (Repository & Data Sources)**: Implementación de repositorios, consumo de API con **Retrofit** y mapeo de datos.

---

## 3. Plan de Trabajo por Capas

### Fase 1: Modelado y Datos (Data Layer)
*   **DTOs**: Creación de `LoanResponse` y `LoanRequest` para reflejar exactamente la estructura de la API Mock.
*   **Domain Models**: Creación de la clase `Loan` limpia de anotaciones externas.
*   **Mappers**: Implementación de funciones de extensión `toModel()` para transformar DTOs en modelos de dominio, desacoplando la lógica de la estructura de la API.
*   **Network**: Configuración de `LoanApiService` y el `NetworkModule` de Hilt para inyectar la dependencia de Retrofit.

### Fase 2: Lógica de Negocio (Domain Layer)
Se desarrollarán **Use Cases** específicos para cada acción del usuario:
*   `GetLoanConditionsUseCase`: Para la pantalla de Info.
*   `ApplyForLoanUseCase`: Para procesar el formulario de solicitud.
*   `GetActiveLoanUseCase`: Para recuperar la deuda y pagos pendientes.

### Fase 3: Estado y Reactividad (Presentation Layer)
*   **UiState**: Definición de `sealed classes` para representar los estados de la pantalla: `Loading`, `Success` y `Error`.
*   **ViewModel**: Uso de `Dispatchers.IO` para llamadas de red y manejo de errores mediante bloques `try-catch` para actualizar el estado de la UI.

### Fase 4: Interfaz de Usuario (UI Layer)
*   **Fidelidad Visual**: Maquetado en Compose respetando estrictamente los tokens de diseño de Figma (colores, tipografía Montserrat/Inter y espaciado).
*   **Componentes**: Uso de `Scaffold`, `TopBar` y componentes reutilizables como botones y tarjetas de información.
*   **UX**: Implementación de **Skeleton Screens** para mejorar la percepción de carga de datos en la pantalla de préstamo activo.

---

## 4. Buenas Prácticas y Estándares
Para cumplir con los criterios de evaluación, se asegura:
*   **Cero Hardcoding**: Todos los strings se gestionarán en `res/values/strings.xml`.
*   **Seguridad**: Variables sensibles como la API Key se manejarán mediante `local.properties`.
*   **Inyección de Dependencias**: Uso integral de **Hilt** para proveer servicios, repositorios y casos de uso.
*   **Asincronismo**: Uso correcto de Corrutinas para no bloquear el hilo principal (Main Thread).
*   **Navegación**: Implementación centralizada en `AppNavigation` mediante rutas y el `NavController`.
