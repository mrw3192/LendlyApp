Plan de Implementación: Módulo Manage

Este documento detalla los pasos para implementar las 4 pantallas del módulo Manage (Perfil y Configuración), siguiendo la arquitectura del proyecto y los diseños de Figma.

Objetivo

Desarrollar un flujo completo de gestión de perfil que permita al usuario ver su información, editar sus datos personales, consultar su Credit Score y recibir confirmación de cambios.

Fase 1: Infraestructura de Datos (Data & Domain)

1.Modelos:
◦Reutilizar y extender la data class User.kt en la carpeta model/.
◦Asegurar que el modelo tenga campos como creditScore, address, birthDate, etc.

2.API Service:
◦Utilizar LendlyApiService.kt (carpeta shared/) para los endpoints:
▪GET users/{id}: Obtener perfil.
▪POST users/{id}: Actualizar perfil.

3.Repositorio:
◦Crear UserRepository.kt en data/repository/ para centralizar las llamadas de perfil.

4.Use Cases:
◦GetUserProfileUseCase.kt: Obtener datos del usuario logueado.
◦UpdateUserProfileUseCase.kt: Guardar cambios del formulario.

Fase 2: Gestión de Estado (ViewModel)

1.ProfileViewModel:
◦Inyectar Use Cases de perfil.
◦Manejar ProfileUiState (Loading, Success, Error).
◦Variables de estado para el formulario de edición (Name, Address, Phone, etc.) con validación en tiempo real.
◦Lógica para calcular el progreso del arco en la pantalla de Credit Score.

Fase 3: Interfaz de Usuario (UI - Compose)
3.1 Pantalla: Manage Overview (ProfileScreen)
•Componente: ProfileScreen.kt (actualizar placeholder).
•Elementos:
◦TopBar con Logo y Notificaciones.
◦Card de usuario con botón "Edit".
◦Lista de navegación (Account details, Settings, Credit Score, etc.).
◦Botón "Log Out".

3.2 Pantalla: Personal Details (EditProfileScreen)
•Componente: EditProfileScreen.kt.
•Elementos:
◦Formulario con OutlinedTextFields.
◦Campos específicos: Full name, Date of Birth (Day/Month/Year), Address, Phone.
◦Botón "Save" fijo en la parte inferior.

3.3 Pantalla: Credit Score (CreditScoreScreen)
•Componente: CreditScoreScreen.kt.
•Elementos:
◦Gráfico de Arco (Canvas) con gradiente de color (Rojo a Verde).
◦Indicador de Score (ej: 720) y categoría (Good).
◦Sección informativa "¿Qué es el Credit Score?".

3.4 Pantalla: Confirmation (ProfileSuccessScreen)
•Componente: ProfileSuccessScreen.kt.
•Elementos:
◦Ilustración de check verde grande.
◦Texto "ALL DONE!".
◦Botón "Done" para volver al perfil.

Fase 4: Navegación e Integración

1.NavigationKeys: Agregar las nuevas rutas a AppDestination.
2.AppNavigation: Registrar las pantallas en el NavHost principal.
3.MainScaffold: Asegurar que la pestaña "Manage" apunte a la nueva ProfileScreen.