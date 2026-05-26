# LendlyApp - Parcial Domiciliario

LendlyApp es una aplicación fintech orientada a Android, desarrollada como evaluación para el curso de **Desarrollo de Aplicaciones Móviles**. Facilita la gestión de préstamos, pagos, compras en un shop integrado y la administración del perfil y puntaje crediticio de los clientes de Software ORT.

## 👥 Equipo de Trabajo
- **Marcelo Wainschenker** (GitHub: [mrw3192](https://github.com/mrw3192))
- **Agustina Salatino** (GitHub: [agussalatino](https://github.com/agussalatino))
- **Zaida Martinez** (GitHub: [zaimartinezj](https://github.com/zaimartinezj))
- **Santino Lamberti** (GitHub: [santinolamberti](https://github.com/santinolamberti))

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

## 📋 Especificaciones del Proyecto

Para guiar el desarrollo de todo el equipo y asegurar el cumplimiento de las pautas del parcial, hemos redactado especificaciones formales:
* **[Especificación Funcional](docs/SPEC_FUNCIONAL.md):** Describe el alcance de la app, las historias de usuario, las reglas de negocio (límites de crédito por Score) y la división detallada del backlog de tareas para cada integrante del grupo.
* **[Especificación Técnica](docs/SPEC_TECNICO.md):** Contiene el mapa del stack, los esquemas de base de datos local y DataStore, la estructura de directorios y los endpoints específicos de la API REST que cada módulo debe consumir.
* **Diseño Figma:** Toda la implementación visual se rige por el archivo local `Loan Management and Fintech Mobile Design Kit (Community).fig`.

## 🚀 Cómo empezar (Para los Colaboradores)

1. Clonen el repositorio en su entorno local:
   ```bash
   git clone https://github.com/mrw3192/LendlyApp.git
   ```
2. Abran el proyecto en **Android Studio**.
3. El proyecto ya cuenta con la estructura base y todas las dependencias en `libs.versions.toml` y `build.gradle.kts`. Sincronicen Gradle.
4. **Importante:** Trabajen en sus respectivas ramas (ej. `feature/modulo-3-home`) y hagan Pull Requests hacia `master`.

## 📋 División de Tareas

El trabajo se ha dividido equitativamente para que podamos avanzar en paralelo:

- **Marcelo Wainschenker:** Módulo Core (Setup, Arquitectura) y Módulo de Autenticación (Splash, Login, Registro).
- **Agustina Salatino:** Módulo Préstamos (Home Dashboard, Lista de préstamos, Solicitud).
- **Zaida Martinez:** Módulo Tienda (Catálogo de productos) y Transacciones (Historial).
- **Santino Lamberti:** Módulo Perfil (Manage, actualización de perfil, operaciones, cerrar sesión).

## 🤖 Uso de IA Generativa
Este proyecto ha hecho uso de herramientas de IA generativa para establecer la arquitectura base, dependencias y planificación del proyecto, cumpliendo con la declaración de uso solicitada.
