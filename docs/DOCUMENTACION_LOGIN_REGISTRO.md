# Documentación: Flujo de Login y Registro (LendlyApp)

Esta documentación detalla la implementación del flujo de **Inicio de Sesión (Login)** y **Registro (Register)** en la aplicación LendlyApp, alineado con las especificaciones técnicas y funcionales del proyecto actualizadas.

---

## 1. Resumen de Pantallas Implementadas

Se han implementado las pantallas descritas en el Figma de manera pixel-perfect, utilizando Jetpack Compose, Material 3 y la paleta de colores del sistema de diseño:

1. **Login Page** (`LoginScreen`): Permite al usuario iniciar sesión mediante Email y Contraseña.
2. **Verify Phone Number** (`VerifyPhoneScreen`): Primer paso del registro. Solicita el código de país y el número de teléfono móvil del usuario.
3. **SMS Verification** (`SmsVerificationScreen`): Segundo paso del registro. Permite ingresar el código OTP de 6 dígitos enviado al móvil (simulado).
4. **Profile Detail Form** (`ProfileDetailScreen`): Tercer paso del registro. Formulario para ingresar los datos personales.
5. **Create Password** (`CreatePasswordScreen`): Cuarto paso del registro. Permite definir una contraseña robusta.
6. **Signature Page** (`SignatureScreen`): Pantalla para capturar la firma digital del usuario dibujando en la pantalla.
7. **ID Verification** (`IdVerificationScreen`): Pantalla que utiliza **CameraX** (cámara trasera) para simular el escaneo del documento de identidad.
8. **Face Recognition** (`FaceRecognitionScreen`): Pantalla que utiliza **CameraX** (cámara frontal) para simular el reconocimiento facial mediante un marco ovalado.
9. **Done Page** (`DoneScreen`): Pantalla final del registro en tema oscuro con una ilustración de éxito ("ALL DONE!").

---

## 2. Validaciones de Formulario (Validación Inline)

El sistema utiliza **Validación Inline** (en tiempo real) que evalúa el estado de los campos una vez que el usuario termina de escribir en ellos y pierde el foco (`onFocusLost`), o al presionar "Next". Los mensajes de error se muestran en texto rojo (`Color.Red`) debajo de cada campo correspondiente:

| Pantalla | Reglas de Validación | Mensaje de Error Inline |
|---|---|---|
| **Verify Phone** | El teléfono debe tener al menos 6 dígitos. | *"Please enter a valid phone number"* |
| **SMS Verification** | Se deben ingresar los 6 dígitos del código OTP. | *"Please enter all 6 digits"* |
| **Profile Detail** | Todos los campos son obligatorios (Nombre, fecha, dirección, etc). | *"First name is required"*, *"Address is required"*, etc. |
| **Create Password** | Mínimo **9 caracteres**, incluyendo al menos **una letra** y **un número**. Las contraseñas deben coincidir. | *"Password must be at least 9 characters"*, *"Passwords do not match"* |

---

## 3. Arquitectura y Estructura de Archivos

### Componentes Reutilizables (`ui/shared/`)
* **LendlyLogo.kt**: Renderiza el logo corporativo SVG del proyecto.
* **LendlyTopAppBar.kt**: Barra superior con botón de regreso.
* **LendlyTextField.kt**: Campo de texto estándar que soporta validación de errores en línea.
* **LendlyPhoneInput.kt**: Entrada combinada para código de país y número de teléfono con soporte de errores.
* **OtpInputRow.kt**: Fila de 6 campos para OTP con avance/retroceso automático del foco.
* **CameraPreview.kt**: Componente wrapper de **CameraX** (`PreviewView`) que vincula el ciclo de vida de Compose con el proveedor de la cámara (soporta cámara frontal y trasera).

*Nota: La `LendlyBottomBar` fue removida de estas pantallas para respetar la experiencia inmersiva dictada por Figma.*

### Pantallas y Lógica de Negocio
* **Registro compartido**: Las pantallas se estructuran a través de un único `RegisterViewModel` inyectado a nivel de navegación, compartiendo el `RegisterState` durante todo el flujo.
* **Cámara Dinámica**: Las pantallas de ID y Face Recognition solicitan permisos de cámara (`Manifest.permission.CAMERA`) en tiempo de ejecución.

---

## 4. Instrucciones para Pruebas en Emulador

Dado que el flujo persiste la sesión mediante DataStore, sigue estos pasos para probar el flujo desde cero:

### Paso 1: Limpiar los Datos de Sesión
Abre una terminal y ejecuta:
```bash
adb shell pm clear com.example.lendlyapp
```

### Paso 2: Ejecutar y Probar Validaciones y Cámara
1. Abre la app, pasa el Onboarding y presiona "Sign up for free".
2. **Prueba Validación Inline**: Toca el campo de teléfono, escribe 2 números, y luego toca en cualquier otra parte de la pantalla (o dale al check del teclado). Verás aparecer el texto rojo *"Please enter a valid phone number"*.
3. Completa los datos requeridos para avanzar en las pantallas.
4. En **Profile Form**, avanza por los campos. Verás errores si dejas alguno vacío al pasar al siguiente.
5. En **Create Password**, escribe contraseñas que no coincidan para probar el mensaje *"Passwords do not match"*.
6. Al llegar a **ID Verification** o **Face Recognition**, la app solicitará permisos de Cámara. Acéptalos y comprueba la simulación de la cámara mediante **CameraX** (asegúrate de que tu emulador tenga una cámara virtual configurada).
7. Al finalizar, llegarás al **DonePage**.
