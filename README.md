# LendlyApp - Parcial Domiciliario

LendlyApp es una aplicación fintech orientada a Android, desarrollada como evaluación para el curso de **Desarrollo de Aplicaciones Móviles**. Facilita la gestión de préstamos, pagos, compras en un shop integrado y la administración del perfil y puntaje crediticio de los clientes de Software ORT.

## 👥 Equipo de Trabajo
- **[Tu Nombre/Apellido]** - Legajo: [Tu Legajo]
- **[Compañero 2]** - Legajo: [Su Legajo]
- **[Compañero 3]** - Legajo: [Su Legajo]
- **[Compañero 4]** - Legajo: [Su Legajo]

## 🛠️ Stack Tecnológico

Este proyecto fue inicializado siguiendo los requerimientos del parcial:
- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Arquitectura:** MVVM + StateFlow/LiveData
- **Networking:** Retrofit + Gson (Header de seguridad integrado)
- **Inyección de Dependencias:** Hilt
- **Navegación:** Navigation Component para Compose
- **Persistencia Local:** Room & DataStore
- **Imágenes:** Coil

## 📁 Estructura del Proyecto (Arquitectura Limpia)

El código fuente está estructurado de manera modular dentro del paquete `com.example.lendlyapp` para evitar conflictos:
- `di/`: Módulos de inyección de dependencias de Hilt.
- `data/`: Acceso a datos. Dividido en `network` (APIs, Retrofit) y `local` (Room, DataStore).
- `domain/`: Lógica de negocio, modelos y casos de uso.
- `presentation/`: Interfaz de usuario dividida por feature (ej: `auth`, `navigation`, `theme`).

## 🚀 Cómo empezar (Para los Colaboradores)

1. Clonen el repositorio en su entorno local:
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   ```
2. Abran el proyecto en **Android Studio**.
3. El proyecto ya cuenta con la estructura base y todas las dependencias en `libs.versions.toml` y `build.gradle.kts`. Sincronicen Gradle.
4. **Importante:** Trabajen en sus respectivas ramas (ej. `feature/modulo-3-home`) y hagan Pull Requests hacia `main`.

## 📋 División de Tareas

El trabajo se ha dividido equitativamente para que podamos avanzar en paralelo:

- **Persona 1:** Módulo Core (Setup, Arquitectura) y Módulo de Autenticación (Splash, Login, Registro).
- **Persona 2:** Módulo Préstamos (Home Dashboard, Lista de préstamos, Solicitud).
- **Persona 3:** Módulo Tienda (Catálogo de productos) y Transacciones (Historial).
- **Persona 4:** Módulo Perfil (Manage, actualización de perfil, operaciones, cerrar sesión).

## 🤖 Uso de IA Generativa
Este proyecto ha hecho uso de herramientas de IA generativa para establecer la arquitectura base, dependencias y planificación del proyecto, cumpliendo con la declaración de uso solicitada.
