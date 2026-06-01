# Especificación Funcional — LendlyApp

**Versión 3.0 — Mayo 2026**

---

# 1. Objetivo

**LendlyApp** es una aplicación móvil Android orientada a la gestión de microcréditos, compras financiadas y control financiero personal.

La aplicación busca ofrecer una experiencia 100 % móvil, simple y rápida, permitiendo:

* Solicitar préstamos.
* Consultar límites de crédito.
* Comprar productos financiados.
* Visualizar movimientos financieros.
* Gestionar perfil y saldo.

La aplicación reemplaza procesos manuales y simulaciones externas mediante una experiencia unificada centrada en el usuario.

---

# 2. Objetivos del Producto

## 2.1 Objetivos de negocio

* Incrementar la accesibilidad a préstamos personales.
* Facilitar compras financiadas desde la aplicación.
* Centralizar información financiera del usuario.
* Incentivar el uso recurrente mediante experiencia simple y rápida.

## 2.2 Objetivos de experiencia

* Navegación intuitiva.
* Flujo de onboarding corto.
* Acciones principales accesibles desde Home.
* Baja fricción en login y solicitud de préstamos.
* Feedback visual claro ante éxito, carga o error.

---

# 3. Alcance

## 3.1 Dentro del alcance

| Módulo            | Descripción                             |
| ----------------- | --------------------------------------- |
| Onboarding & Auth | Splash, onboarding, login y registro (incluyendo captura simulada de firma, ID y biometría) |
| Home              | Dashboard principal y accesos rápidos   |
| Préstamos         | Simulación y solicitud de préstamos     |
| Shop              | Catálogo de productos financiables      |
| Historial         | Consulta de movimientos y transacciones |
| Perfil            | Datos personales, cash in y logout      |

---

## 3.2 Fuera del alcance

* Integración con bancos reales.
* Pasarelas de pago reales.
* Transferencias bancarias.
* Emisión de tarjetas físicas.
* Verificación por SMS o WhatsApp (envío real de mensajes).
* Sistema antifraude avanzado.
* Firma digital con validez legal (la firma se captura de manera puramente visual).

---

# 4. Roles de Usuario

## 4.1 Usuario autenticado

Puede:

* Solicitar préstamos.
* Comprar productos.
* Consultar historial.
* Gestionar perfil.
* Realizar cash in.

---

## 4.2 Usuario no autenticado

Puede:

* Ver onboarding.
* Registrarse.
* Iniciar sesión.

No puede acceder a funcionalidades financieras.

---

# 5. Navegación General

## 5.1 Flujo principal

```text
Splash
 ├─ Usuario autenticado → Home
 ├─ Primera apertura → Onboarding
 │      ├─ Registro → Home
 │      └─ Login → Home
 └─ Usuario no autenticado → Login
```

---

## 5.2 Navegación inferior principal

La aplicación posee una Bottom Navigation con 5 secciones:

| Tab     | Función                |
| ------- | ---------------------- |
| Home    | Dashboard principal    |
| Loans   | Solicitud de préstamos |
| Shop    | Catálogo de productos  |
| History | Historial financiero   |
| Manage  | Perfil y configuración |

---

# 6. Módulos Funcionales

---

# 6.1 Splash

## Objetivo

Validar el estado inicial de sesión y determinar el flujo de navegación.

---

## Comportamiento

La pantalla debe:

* Mostrar branding inicial.
* Validar si el usuario posee sesión activa.
* Verificar si ya visualizó el onboarding.
* Redireccionar automáticamente.

---

## Reglas

| Condición           | Resultado           |
| ------------------- | ------------------- |
| Usuario autenticado | Navega a Home       |
| Primera apertura    | Navega a Onboarding |
| Sin sesión          | Navega a Login      |

---

# 6.2 Onboarding

## Objetivo

Introducir los beneficios principales de la aplicación.

---

## Páginas

| Página | Mensaje principal      |
| ------ | ---------------------- |
| 1      | Préstamos rápidos      |
| 2      | Productos financiables |
| 3      | Seguimiento y pagos    |

---

## Acciones

| Acción           | Resultado                 |
| ---------------- | ------------------------- |
| Continuar        | Avanza al siguiente slide |
| Sign up for free | Navega a registro         |
| Log In           | Navega a login            |

---

## Reglas

* El onboarding solo debe mostrarse en la primera apertura.
* Una vez completado, no debe volver a aparecer automáticamente.

---

# 6.3 Login

## Objetivo

Permitir autenticación del usuario.

---

## Datos solicitados

| Campo    | Obligatorio |
| -------- | ----------- |
| Email    | Sí          |
| Password | Sí          |

---

## Validaciones

| Validación               | Resultado                                 |
| ------------------------ | ----------------------------------------- |
| Email inválido           | Mostrar error                             |
| Password vacía           | Mostrar error                             |
| Credenciales incorrectas | Mostrar mensaje de autenticación inválida |

---

## Resultado esperado

* Login exitoso → Home.
* Persistencia de sesión.

---

# 6.4 Registro

## Objetivo

Permitir creación de cuenta.

---

## Campos requeridos

| Campo              | Regla               |
| ------------------ | ------------------- |
| Nombre             | Obligatorio         |
| Apellido           | Obligatorio         |
| DNI                | Numérico            |
| Email              | Formato válido      |
| Password           | Mínimo 9 caracteres (1 letra y 1 número) |
| Confirmar password | Debe coincidir      |

---

## Pasos de verificación adicionales (Mock)

Durante el flujo de registro, se presentan pasos adicionales obligatorios/opcionales para fines de UX:
1. **Firma Digital**: Pantalla tipo canvas para que el usuario dibuje su firma.
2. **Escaneo de ID**: Utiliza la cámara (CameraX) trasera para simular el escaneo del documento en un marco específico.
3. **Reconocimiento Facial**: Utiliza la cámara frontal (CameraX) en un marco ovalado para simular la verificación biométrica.

---

## Resultado esperado

* Registro exitoso.
* Inicio automático de sesión.
* Navegación a Home.

---

# 6.5 Home

## Objetivo

Centralizar información financiera principal del usuario.

---

## Información mostrada

| Elemento           | Descripción                  |
| ------------------ | ---------------------------- |
| Credit Score       | Nivel crediticio del usuario |
| Balance disponible | Saldo actual                 |
| Límite de crédito  | Monto máximo disponible      |
| Accesos rápidos    | Acciones principales         |

---

## Accesos rápidos

| Acción       | Resultado               |
| ------------ | ----------------------- |
| Request Loan | Navega a préstamos      |
| Shop         | Navega a tienda         |
| Cash In      | Navega a carga de saldo |

---

# 6.6 Préstamos

## Objetivo

Permitir simulación y solicitud de préstamos.

---

## Funcionalidades

* Selección de monto.
* Selección de cuotas.
* Simulación de intereses.
* Visualización del total a devolver.
* Confirmación de solicitud.

---

## Datos mostrados

| Dato             | Descripción        |
| ---------------- | ------------------ |
| Monto solicitado | Capital solicitado |
| Tasa             | Interés aplicado   |
| Cuota mensual    | Valor mensual      |
| Total a devolver | Monto final        |

---

## Resultado esperado

* Solicitud exitosa.
* Actualización de balance e historial.

---

# 6.7 Shop

## Objetivo

Permitir compras financiadas desde la aplicación.

---

## Funcionalidades

* Visualización de catálogo.
* Consulta de detalle de producto.
* Selección de cuotas.
* Compra financiada.

---

## Información de producto

| Campo  | Descripción              |
| ------ | ------------------------ |
| Imagen | Imagen del producto      |
| Nombre | Nombre comercial         |
| Precio | Valor contado            |
| Cuotas | Opciones de financiación |

---

## Restricciones

* No puede superarse el límite disponible.
* Si el usuario no posee crédito suficiente, la compra debe bloquearse.

---

# 6.8 Historial

## Objetivo

Permitir consulta de movimientos financieros.

---

## Tipos de movimientos

| Tipo      |
| --------- |
| Préstamos |
| Compras   |
| Ingresos  |
| Cash In   |

---

## Funcionalidades

* Lista cronológica.
* Filtros por categoría.
* Visualización de detalle.

---

# 6.9 Perfil

## Objetivo

Gestionar datos del usuario y configuración básica.

---

## Funcionalidades

| Funcionalidad | Descripción                    |
| ------------- | ------------------------------ |
| Ver perfil    | Consulta de datos personales   |
| Editar perfil | Modificación de datos          |
| Cash In       | Simulación de ingreso de saldo |
| Logout        | Cierre de sesión               |

---

## Resultado esperado

* Logout elimina sesión activa.
* Usuario vuelve al flujo de autenticación.

---

# 7. Reglas de Negocio

## 7.1 Límites por score crediticio

| Score   | Límite   | Tasa mensual |
| ------- | -------- | ------------ |
| < 400   | $15.000  | 12 %         |
| 400–699 | $50.000  | 8 %          |
| ≥ 700   | $150.000 | 5 %          |

---

## 7.2 Restricción de compras

Una compra no puede aprobarse si supera el límite disponible del usuario.

---

## 7.3 Autenticación requerida

Todas las funcionalidades financieras requieren sesión activa.

---

## 7.4 Persistencia de sesión

La sesión debe mantenerse activa hasta logout explícito.

---

# 8. Estados de Sistema

## 8.1 Estados de carga

Todas las acciones asincrónicas deben contemplar:

* Estado inicial.
* Estado de carga.
* Estado exitoso.
* Estado de error.

---

## 8.2 Manejo de errores

La aplicación debe informar:

* Errores de red.
* Validaciones inválidas.
* Fallos de autenticación.
* Operaciones rechazadas.

---

# 9. Requerimientos No Funcionales

## 9.1 Usabilidad

* Navegación intuitiva.
* Flujo simple.
* Acciones principales visibles.

---

## 9.2 Performance

* Navegación fluida.
* Carga rápida de pantallas.
* Respuesta inmediata en interacciones.

---

## 9.3 Responsividad

La aplicación debe adaptarse correctamente a:

* pantallas pequeñas,
* medianas,
* orientación vertical.

---

## 9.4 Persistencia

El estado de sesión y preferencias del usuario deben persistirse entre aperturas.

---

## 9.5 Fidelidad visual

La interfaz debe respetar los diseños definidos en Figma y mantener consistencia visual entre pantallas.

---

# 10. Dependencias Externas

| Dependencia  | Uso                     |
| ------------ | ----------------------- |
| Backend API  | Operaciones financieras |
| Diseño Figma | Referencia visual       |

---

# 11. Criterios de Aceptación Generales

## La aplicación se considera funcional cuando:

* El usuario puede registrarse e iniciar sesión.
* Puede solicitar préstamos.
* Puede realizar compras financiadas.
* Puede visualizar historial.
* Puede gestionar perfil.
* La navegación funciona correctamente.
* La sesión persiste.
* Las reglas de negocio se respetan.
* Los errores son manejados adecuadamente.