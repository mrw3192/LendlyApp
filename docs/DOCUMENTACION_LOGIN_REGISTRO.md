# Documentación: Flujo de Login y Registro (LendlyApp)

Esta documentación detalla la implementación del flujo de **Inicio de Sesión (Login)** y **Registro (Register)** en la aplicación LendlyApp, alineado con las especificaciones técnicas y funcionales del proyecto.

---

## 1. Resumen de Pantallas Implementadas (★ Prioridad)

Se han implementado las **6 pantallas prioritarias** descritas en el Figma de manera pixel-perfect, utilizando Jetpack Compose, Material 3 y la paleta de colores del sistema de diseño:

1. **Login Page** (`LoginScreen`): Permite al usuario iniciar sesión mediante Email y Contraseña. Se comunica con el `LoginViewModel`.
2. **Verify Phone Number** (`VerifyPhoneScreen`): Primer paso del registro. Solicita el código de país y el número de teléfono móvil del usuario.
3. **SMS Verification** (`SmsVerificationScreen`): Segundo paso del registro. Permite ingresar el código OTP de 6 dígitos que fue enviado al móvil (simulado).
4. **Profile Detail Form** (`ProfileDetailScreen`): Tercer paso del registro. Formulario completo para ingresar los datos personales (Nombre completo, Fecha de nacimiento, Dirección, Ciudad, Código Postal, etc.).
5. **Create Password** (`CreatePasswordScreen`): Cuarto paso del registro. Permite definir una contraseña robusta con requisitos de seguridad visibles.
6. **Done Page** (`DoneScreen`): Pantalla final del registro en tema oscuro con una ilustración de éxito ("ALL DONE!") para redirigir al Dashboard principal.

---

## 2. Validaciones de Formulario y Alertas

Se ha implementado un sistema robusto de validaciones que evalúa el estado del formulario cuando el usuario presiona el botón **"Next"** o **"Send Code"**. Si alguna validación falla, se muestra un diálogo de alerta personalizado (`LendlyAlertDialog`) con un mensaje claro:

| Pantalla | Reglas de Validación | Mensaje de Alerta |
|---|---|---|
| **Verify Phone** | El número de teléfono ingresado debe tener al menos 6 dígitos. | *"Please enter a valid phone number"* |
| **SMS Verification** | Se deben ingresar los 6 dígitos del código de verificación. | *"Please enter all 6 digits"* |
| **Profile Detail** | **Todos los campos del formulario son obligatorios:** Primer nombre, apellido, fecha de nacimiento (día, mes, año), dirección, ciudad, código postal y teléfono. | Muestra el campo específico que falta (ej: *"Address is required"*, *"Date of birth is required"*). |
| **Create Password** | La contraseña debe tener al menos **9 caracteres** y contener al menos **una letra** y **un número**. | *"Password must be at least 9 characters"* o *"Password must contain a letter and a number"*. |

---

## 3. Arquitectura y Estructura de Archivos

### Componentes Reutilizables (`ui/shared/`)
* **[LendlyLogo.kt](file:///c:/Users/-Batt0/Desktop/Parcial%20TP3/LendlyApp/app/src/main/java/com/example/lendlyapp/ui/shared/LendlyLogo.kt)**: Renderiza el logo corporativo SVG del proyecto y la barra del indicador del sistema.
* **[LendlyTopAppBar.kt](file:///c:/Users/-Batt0/Desktop/Parcial%20TP3/LendlyApp/app/src/main/java/com/example/lendlyapp/ui/shared/LendlyTopAppBar.kt)**: Barra de herramientas superior con botones de regreso e información.
* **[LendlyTextField.kt](file:///c:/Users/-Batt0/Desktop/Parcial%20TP3/LendlyApp/app/src/main/java/com/example/lendlyapp/ui/shared/LendlyTextField.kt)**: Campo de texto de entrada estándar con label, bordes de color del Figma y visibilidad de contraseña opcional.
* **[LendlyPhoneInput.kt](file:///c:/Users/-Batt0/Desktop/Parcial%20TP3/LendlyApp/app/src/main/java/com/example/lendlyapp/ui/shared/LendlyPhoneInput.kt)**: Entrada combinada para código de país y número de teléfono.
* **[OtpInputRow.kt](file:///c:/Users/-Batt0/Desktop/Parcial%20TP3/LendlyApp/app/src/main/java/com/example/lendlyapp/ui/shared/OtpInputRow.kt)**: Fila de 6 campos individuales para OTP con avance y retroceso automático del foco.
* **[LendlyBottomBar.kt](file:///c:/Users/-Batt0/Desktop/Parcial%20TP3/LendlyApp/app/src/main/java/com/example/lendlyapp/ui/shared/LendlyBottomBar.kt)**: Barra inferior con el botón primario ovalado verde y el indicador de inicio.
* **[LendlyAlertDialog.kt](file:///c:/Users/-Batt0/Desktop/Parcial%20TP3/LendlyApp/app/src/main/java/com/example/lendlyapp/ui/shared/LendlyAlertDialog.kt)**: Cuadro de diálogo de alerta estilizado para informar de fallos en las validaciones.

### Pantallas y Lógica de Negocio
* **Registro compartido**: Las 5 pantallas de registro se estructuran a través de un único `RegisterViewModel` inyectado a nivel de navegación (`AppNavigation.kt`). Esto permite compartir el estado del formulario (`RegisterState`) durante todo el proceso de registro de manera sencilla y persistente.
* **Sesión (DataStore)**: Una vez finalizado el registro o login de manera exitosa (simulados con un retardo de red de 600-800ms), se genera un token ficticio y se persiste en `UserPreferences` utilizando Jetpack Preferences DataStore. La pantalla de Splash redirige automáticamente al usuario autenticado al Home.

---

## 4. Instrucciones para Pruebas en Emulador

Dado que el flujo de autenticación persiste la sesión de manera local mediante DataStore, sigue estos pasos para probar el flujo completo desde cero:

### Paso 1: Limpiar los Datos de Sesión
Para forzar al sistema a pensar que no hay ningún usuario logueado, abre una consola de terminal (o la pestaña Terminal de Android Studio) y ejecuta:

```bash
adb shell pm clear com.example.lendlyapp
```

*(O puedes hacerlo manualmente manteniendo presionado el icono de la aplicación en el emulador → Información de la aplicación → Almacenamiento y caché → Limpiar almacenamiento).*

### Paso 2: Ejecutar y Probar Validaciones
1. Al abrir la app, verás la Splash Screen y luego el Onboarding. Presiona "Sign up for free".
2. **Prueba 1 (Verify Phone)**: Deja el campo vacío o escribe menos de 6 números y presiona "Send Code". Se mostrará el diálogo de alerta.
3. **Prueba 2 (SMS Code)**: Presiona "Next" sin completar los 6 dígitos. Se mostrará el diálogo.
4. **Prueba 3 (Profile Form)**: Deja algún campo vacío (por ejemplo, el año de nacimiento o la ciudad) y presiona "Next". El sistema te alertará indicando el campo faltante.
5. **Prueba 4 (Create Password)**: Ingresa una contraseña simple como `12345` o que solo tenga números. El diálogo te alertará sobre los requerimientos de seguridad mínimos (al menos 9 caracteres, combinando letras y números).
6. Al ingresar una contraseña válida y presionar "Next", el sistema registrará al usuario y mostrará la pantalla DonePage.
