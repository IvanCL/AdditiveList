# AdditiveList - Aplicación para Android

`AdditiveList` es una aplicación para Android diseñada para ayudar a los usuarios, especialmente a aquellos que siguen una dieta vegana, a identificar rápidamente el origen de los aditivos alimentarios y a analizar si un producto es apto para veganos.

## Características Principales

- **Lista Completa de Aditivos:** La aplicación descarga y almacena una lista completa de aditivos alimentarios la primera vez que se ejecuta.
- **Búsqueda Rápida:** Permite buscar aditivos de forma instantánea por su nombre o número E.
- **Identificación por Colores:** Utiliza un sistema de colores intuitivo para una rápida identificación del origen del aditivo.
- **Vista de Detalle:** Ofrece una pantalla de detalle para cada aditivo con información sobre su función y descripción.
- **Análisis de Productos por OCR:** Fotografía o selecciona la imagen de la lista de ingredientes de un producto para obtener un veredicto vegano/dudoso/no vegano al instante, de forma totalmente local y privada.
- **Tutorial de Bienvenida:** Guía a los nuevos usuarios en su primera ejecución.
- **Diseño Moderno:** Interfaz de usuario basada en Material 3 con soporte para tema claro/oscuro.

---

## Arquitectura y Tecnologías

El proyecto sigue una arquitectura moderna **MVVM (Model-View-ViewModel)**, que separa las responsabilidades y mejora la robustez y escalabilidad de la aplicación.

### Diagrama de Flujo general

```
SplashActivity
    └── descarga aditivos desde API REST → guarda en SharedPreferences (Gson)
    └── redirige a TutorialActivity (1ª vez) o MainActivity

MainActivity
    ├── AdditivesActivity ──► AdditivesViewModel ──► PreferencesUtils
    │       └── AdditiveAdapter (ListAdapter + DiffUtil)
    │               └── AdditiveDetailActivity
    └── ProductsActivity ──► ProductAnalysisViewModel
            ├── AdditiveParser   (extrae números E del texto OCR)
            ├── IngredientParser (cruza con base de datos de ingredientes local)
            └── ProductResultActivity (veredicto + aditivos + ingredientes detectados)
```

### Componentes Clave — Búsqueda de aditivos

-   **View:** `AdditivesActivity` — muestra datos y captura la búsqueda del usuario.
-   **ViewModel:** `AdditivesViewModel` — filtra la lista y expone `LiveData`. Sobrevive a rotaciones de pantalla.
-   **Data:** `PreferencesUtils` — carga/guarda la lista de aditivos serializada con Gson.

### Componentes Clave — Análisis de productos

-   **`ProductsActivity`** — punto de entrada; el usuario captura una imagen con la cámara o la selecciona de la galería.
-   **`ProductAnalysisViewModel`** — orquesta el flujo:
    1. Escala la imagen y lanza OCR con **ML Kit Text Recognition** (on-device, sin enviar datos a terceros).
    2. Preprocesa el texto eliminando cláusulas "Puede contener…" para evitar falsos positivos.
    3. `AdditiveParser` extrae números E (`E120`, `E-120`, `E 120`) y busca aditivos por nombre.
    4. `IngredientParser` cruza el texto con una base de datos local de ingredientes problemáticos (`non_vegan_ingredients.json`).
    5. Calcula el veredicto final y emite el resultado via `LiveData`.
-   **`ProductResultActivity`** — muestra el veredicto con color e icono, la lista de aditivos detectados y los ingredientes problemáticos encontrados.

### Lógica de veredicto

| Condición | Veredicto |
|---|---|
| Algún aditivo o ingrediente es **"No vegano"** | 🔴 No vegano |
| Algún aditivo o ingrediente es **"Dudoso"** (y ninguno es "No vegano") | 🟡 Dudoso |
| Ningún aditivo ni ingrediente problemático detectado | 🟢 Vegano |

> Las cláusulas "Puede contener [trazas de] X" se ignoran — indican riesgo de contaminación cruzada en fábrica, no ingredientes reales del producto.

### Tecnologías

- **Lenguaje:** 100% [Kotlin](https://kotlinlang.org/).
- **OCR on-device:** [ML Kit Text Recognition](https://developers.google.com/ml-kit/vision/text-recognition) — gratuito, sin red, sin envío de imágenes.
- **Asincronía:** Corrutinas de Kotlin (`viewModelScope`, `Dispatchers.IO`).
- **Interfaz de Usuario:** [Material Design 3](https://m3.material.io/) con ViewBinding.
- **Listas Eficientes:** `RecyclerView` con `ListAdapter` y `DiffUtil`.
- **Persistencia:** `SharedPreferences` con **Gson**.
- **Testing:**
    - **Tests Unitarios (JUnit):** Validan la lógica de filtrado y asignación de colores.
    - **Tests de UI (Espresso):** Validan flujos completos de usuario con `IdlingResource` para sincronizar con corrutinas.

---

## Base de datos de ingredientes

El archivo `app/src/main/assets/non_vegan_ingredients.json` contiene la lista de ingredientes problemáticos con su clasificación. Ejemplos:

| Categoría | Ingredientes "No vegano" | Ingredientes "Dudoso" |
|---|---|---|
| Lácteos | leche, mantequilla, nata, queso, lactosa, caseína… | — |
| Huevo | huevo, albúmina, ovoalbúmina, lisozima… | lecitina (sin especificar origen) |
| Carne/Pescado | gelatina, colágeno, jamón, atún, anchoas… | — |
| Miel | miel, cera de abeja, jalea real… | — |
| Otros | carmín, cochinilla, cuajo, lanolina… | vitamina D3, omega-3, glicerina, aromas naturales… |
