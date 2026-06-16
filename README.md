# AdditiveList - Aplicación para Android

`AdditiveList` es una aplicación para Android diseñada para ayudar a los usuarios, especialmente a aquellos que siguen una dieta vegana, a identificar rápidamente el origen de los aditivos alimentarios.

La aplicación permite a los usuarios buscar aditivos por su número E o por su nombre, y proporciona información detallada sobre su origen y posibles efectos secundarios.

## Características Principales

- **Lista Completa de Aditivos:** La aplicación descarga y almacena una lista completa de aditivos alimentarios la primera vez que se ejecuta.
- **Búsqueda Rápida:** Permite buscar aditivos de forma instantánea por su nombre o número E.
- **Identificación por Colores:** Utiliza un sistema de colores intuitivo para una rápida identificación del origen del aditivo.
- **Vista de Detalle:** Ofrece una pantalla de detalle para cada aditivo con información sobre su función y descripción.
- **Tutorial de Bienvenida:** Guía a los nuevos usuarios en su primera ejecución.
- **Diseño Moderno:** Interfaz de usuario basada en Material 3 con soporte para tema claro/oscuro.

---

## Arquitectura y Tecnologías

El proyecto ha sido refactorizado para seguir una arquitectura moderna **MVVM (Model-View-ViewModel)**, que separa las responsabilidades y mejora la robustez y escalabilidad de la aplicación.

### Diagrama de Flujo (MVVM)

El flujo de datos en la pantalla de búsqueda de aditivos sigue este patrón:

```
  +----------------+
  |      VIEW      |  <-- (AdditivesActivity)
  | (Interfaz de   |      - Muestra los datos
  |    Usuario)    |      - Notifica acciones del usuario
  +-------+--------+
          |
          | 1. El usuario escribe en el buscador.
          |    La Vista notifica al ViewModel.
          |
  +-------v--------+
  |   VIEWMODEL    |  <-- (AdditivesViewModel)
  | (Lógica de     |      - Mantiene los datos y el estado.
  |   Presentación)|      - Sobrevive a giros de pantalla.
  +-------+--------+
          |
          | 2. El ViewModel filtra la lista y actualiza
          |    el LiveData. La Vista es notificada.
          |
  +-------v--------+
  |      DATA      |  <-- (PreferencesUtils / Red)
  | (Fuentes de    |      - Carga los datos (SharedPreferences).
  |      Datos)    |      - Realiza llamadas de red.
  +----------------+
```

### Componentes Clave

-   **View (Vista):** Representada por `AdditivesActivity`. Su única responsabilidad es "pintar" la pantalla con los datos que recibe y capturar las interacciones del usuario (como escribir en el buscador). No contiene ninguna lógica de negocio.

-   **ViewModel (`AdditivesViewModel`):** Actúa como un intermediario. Contiene toda la lógica de presentación:
    -   Mantiene la lista completa de aditivos.
    -   Expone los datos que la vista necesita a través de **`LiveData`**. `LiveData` es un observador que notifica automáticamente a la `Activity` cuando los datos cambian, para que la interfaz se actualice.
    -   **Sobrevive a los cambios de configuración:** Como el `ViewModel` no se destruye cuando se gira el teléfono, la lista de aditivos y el estado de la búsqueda se conservan, evitando recargas innecesarias.

-   **Data (Datos):** Representado por `PreferencesUtils` y la lógica de descarga de red. Se encarga de obtener los datos, ya sea desde la memoria del teléfono (`SharedPreferences` con Gson) o desde el servidor web.

### Otras Tecnologías

- **Lenguaje:** 100% [Kotlin](https://kotlinlang.org/).
- **Asincronía:** Se usan **Corrutinas de Kotlin** para realizar tareas en segundo plano (como la descarga de datos) de una forma moderna y eficiente.
- **Interfaz de Usuario (UI):**
    - [Material Design 3](https://m3.material.io/).
    - **ViewBinding** para una interacción segura con las vistas.
- **Listas Eficientes:** `RecyclerView` con `ListAdapter` para un rendimiento óptimo.
- **Persistencia:** `SharedPreferences` con **Gson** para serializar la lista de aditivos de forma robusta.
- **Testing:**
    - **Tests Unitarios (JUnit):** Para validar la lógica de negocio de forma aislada.
    - **Tests de UI (Espresso):** Para validar flujos de usuario completos. Se utiliza un **`IdlingResource`** para sincronizar los tests con las corrutinas, garantizando su fiabilidad.