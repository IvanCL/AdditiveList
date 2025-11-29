# AdditiveList - Aplicación para Android

`AdditiveList` es una aplicación para Android diseñada para ayudar a los usuarios, especialmente a aquellos que siguen una dieta vegana, a identificar rápidamente el origen de los aditivos alimentarios.

La aplicación permite a los usuarios buscar aditivos por su número E o por su nombre, y proporciona información detallada sobre su origen y posibles efectos secundarios.

## Características Principales

- **Lista Completa de Aditivos:** La aplicación descarga y almacena una lista completa de aditivos alimentarios la primera vez que se ejecuta.
- **Búsqueda Rápida:** Permite buscar aditivos de forma instantánea por su nombre o número E (ej: "E120" o "Cochinilla").
- **Identificación por Colores:** Utiliza un sistema de colores intuitivo para una rápida identificación del origen del aditivo:
    - **Verde:** Origen vegano.
    - **Amarillo:** Origen dudoso (puede ser animal o vegetal).
    - **Rojo:** Origen no vegano.
- **Vista de Detalle:** Ofrece una pantalla de detalle para cada aditivo con información sobre su función, descripción y posibles efectos secundarios.
- **Tutorial de Bienvenida:** Guía a los nuevos usuarios en su primera ejecución, explicando las funcionalidades clave.

## Arquitectura y Tecnologías

El proyecto sigue las prácticas modernas de desarrollo de Android y ha sido actualizado para usar las siguientes tecnologías:

- **Lenguaje:** 100% [Kotlin](https://kotlinlang.org/).
- **Interfaz de Usuario (UI):**
    - [Material Design 3](https://m3.material.io/) para un aspecto moderno y consistente.
    - Soporte automático para **Tema Claro y Oscuro**.
    - **ViewBinding** para una interacción segura y eficiente con las vistas.
    - Componentes modernos de AndroidX como `MaterialCardView` y `MaterialToolbar`.
- **Listas Eficientes:**
    - `RecyclerView` con `ListAdapter` y `DiffUtil` para un rendimiento óptimo y animaciones automáticas al mostrar y filtrar la lista de aditivos.
- **Persistencia de Datos:**
    - **SharedPreferences** para almacenar datos de forma local.
    - **Gson** para serializar y deserializar la lista de aditivos de forma robusta, reemplazando el antiguo sistema manual.
- **Testing:**
    - **Tests Unitarios (JUnit):** Para validar la lógica de negocio (filtrado, asignación de colores, etc.) de forma rápida y aislada.
    - **Tests de UI (Espresso):** Para validar flujos de usuario completos, como el tutorial de bienvenida. Se utiliza un **`IdlingResource`** para sincronizar los tests con las tareas en segundo plano (como la descarga de datos), garantizando su fiabilidad.

## Cómo Compilar y Ejecutar

1.  Clona o descarga el repositorio.
2.  Abre el proyecto en la última versión de Android Studio.
3.  Sincroniza el proyecto con los ficheros de Gradle.
4.  Ejecuta la aplicación en un emulador o dispositivo físico.