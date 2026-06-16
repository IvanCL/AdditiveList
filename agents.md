# Agents — AdditiveList

Guía para que un agente de IA (Claude Code u otro) pueda trabajar en este proyecto de forma autónoma y segura.

---

## 1. Descripción del proyecto

**AdditiveList** es una aplicación Android nativa escrita en 100% Kotlin. Su propósito es ayudar a personas veganas (y cualquier interesado) a:
1. Identificar el origen de los aditivos alimentarios por número E o nombre.
2. Analizar si un producto es apto para veganos fotografiando su lista de ingredientes.

- **Package:** `com.icl.additivelist`
- **Versión actual:** 1.2.2 (versionCode 6)
- **SDK:** minSdk 21, targetSdk 35, compileSdk 35
- **Idioma de la UI:** Español

---

## 2. Arquitectura

El proyecto sigue **MVVM** con las siguientes capas:

```
SplashActivity
    └── descarga aditivos desde API REST → guarda en SharedPreferences (Gson)
    └── redirige a TutorialActivity (1ª vez) o MainActivity

MainActivity
    ├── AdditivesActivity ──► AdditivesViewModel ──► PreferencesUtils
    │       └── AdditiveAdapter (ListAdapter + DiffUtil)
    │               └── AdditiveDetailActivity
    └── ProductsActivity ──► ProductAnalysisViewModel
            ├── AdditiveParser
            ├── IngredientParser
            └── ProductResultActivity
```

### Archivos clave — módulo de aditivos

| Archivo | Responsabilidad |
|---|---|
| `SplashActivity.kt` | Descarga inicial de aditivos vía corrutina, navegación condicional |
| `AdditivesViewModel.kt` | Carga la lista desde prefs, expone LiveData, lógica de filtrado |
| `AdditivesActivity.kt` | Observa el ViewModel, gestiona el buscador (TextWatcher) |
| `AdditiveAdapter.kt` | ListAdapter + DiffUtil + lógica de color por origen |
| `AdditiveDetailActivity.kt` | Muestra detalle de un aditivo recibido como Parcelable |
| `PreferencesUtils.kt` | Wrapper de SharedPreferences con serialización Gson |
| `ConfigProperties.kt` | Lee URLs de `assets/config.properties` |
| `EspressoIdlingResource.kt` | Semáforo para sincronizar tests de UI con corrutinas |

### Archivos clave — módulo de análisis de productos

| Archivo | Responsabilidad |
|---|---|
| `ProductsActivity.kt` | Captura imagen (cámara/galería), gestiona permisos, lanza análisis |
| `ProductAnalysisViewModel.kt` | OCR → preprocesado → parseo → cruce → veredicto vía LiveData |
| `AdditiveParser.kt` | Extrae números E del texto OCR; soporta E120, E-120, E 120; búsqueda por nombre |
| `IngredientParser.kt` | Carga `non_vegan_ingredients.json` y busca términos con detección de palabra completa |
| `ProductResultActivity.kt` | Muestra veredicto + aditivos detectados (RecyclerView) + ingredientes (ChipGroup) |
| `non_vegan_ingredients.json` | Base de datos local de ingredientes problemáticos (~100 entradas) |
| `NonVeganIngredient.kt` | Modelo Parcelable: `term` + `origin` |

### Modelos de datos

```kotlin
data class Additive(
    val id: String,
    val numb: String,       // "E100", "E100(ii)"
    val name: String,
    val description: String,
    val func: String,
    val sideEffects: String,
    val origin: String      // "Vegano" | "Dudoso" | "No vegano"
)

data class NonVeganIngredient(
    val term: String,       // "leche", "vitamina D3"
    val origin: String      // "No vegano" | "Dudoso"
)
```

---

## 3. Fuente de datos

Los aditivos se descargan una sola vez desde un servidor propio y se cachean en `SharedPreferences`:

```
GET http://icmd-soft-services.asuscomm.com:8001/api/additive
```

Las URLs están en `app/src/main/assets/config.properties`. **No hardcodear URLs en el código.**

Los ingredientes problemáticos están en `app/src/main/assets/non_vegan_ingredients.json` — es una lista local, no requiere red.

---

## 4. Sistema de colores por origen

| Valor de `origin` | Color | Icono |
|---|---|---|
| `"Vegano"` (o cualquier otro) | `colorVegan` (verde) | `vegan_icon_ok` |
| `"Dudoso"` | `colorDoubtful` (amarillo/naranja) | `question_icon` |
| `"No vegano"` | `colorDangerous` (rojo) | `skull_icon` |

La lógica vive en `AdditiveAdapter.getVisualsForOrigin()` y se duplica en `AdditiveDetailActivity` y `ProductResultActivity`. Si se modifica, actualizar los tres sitios.

---

## 5. Flujo de análisis de productos (OCR)

1. `ProductsActivity` — usuario pulsa "Usar cámara" o "Elegir de galería".
2. Imagen se decodifica a `Bitmap` localmente (nunca se envía a ningún servidor).
3. `ProductAnalysisViewModel.analyzeImage(bitmap)`:
   - Escala la imagen a máx. 1024px para optimizar el OCR.
   - ML Kit Text Recognition extrae el texto (on-device).
   - `removeTraceWarnings()` elimina cláusulas `"Puede contener…"` con regex `puede\s+contener[^.;]*` para evitar falsos positivos.
   - `AdditiveParser` extrae números E y busca aditivos por nombre en el texto limpio.
   - `IngredientParser` busca ingredientes problemáticos con detección de palabra completa (evita que "leche" coincida con "lechuga").
   - Se calcula el veredicto y se emite `ProductAnalysisResult` por LiveData.
4. `ProductResultActivity` — muestra veredicto con color, lista de aditivos y chips de ingredientes.

### Lógica de veredicto

- **No vegano** si cualquier aditivo O ingrediente tiene `origin == "No vegano"`
- **Dudoso** si cualquier aditivo O ingrediente tiene `origin == "Dudoso"` (y ninguno es "No vegano")
- **Vegano** en caso contrario

---

## 6. Flujo de primer arranque

1. `SplashActivity` → comprueba si hay aditivos en caché.
2. Si no los hay → descarga desde API → guarda en prefs → navega.
3. Comprueba si el tutorial ya fue completado (`PREF_TUTORIAL_COMPLETED`).
4. Si no fue completado → `TutorialActivity` → al finalizar guarda la preferencia.
5. Si ya fue completado → `MainActivity`.

---

## 7. Testing

### Tests unitarios (`/src/test/`)
- `AdditivesFilteringTest.kt` — valida la lógica de filtrado por nombre y número E.
- `AdditiveAdapterTest.kt` — valida la asignación de color/icono por origen.

### Tests de UI (`/src/androidTest/`)
- `TutorialFlowTest.kt` — flujo completo: borrar prefs → arrancar app → pasar tutorial → llegar a MainActivity.
- `IdlingResourceRule.kt` — registra el `EspressoIdlingResource` para sincronizar con corrutinas.

**Importante:** Los tests de UI usan `EspressoIdlingResource`. Cualquier tarea asíncrona nueva que afecte a la navegación debe hacer `increment()` al iniciar y `decrement()` al terminar.

```bash
./gradlew test                    # Tests unitarios
./gradlew connectedAndroidTest    # Tests de UI (requiere dispositivo/emulador)
```

---

## 8. Build y publicación

```bash
./gradlew assembleDebug    # APK debug
./gradlew bundleRelease    # AAB firmado para Google Play
```

- El keystore está en `app/build.gradle` → `signingConfigs.release`. **No está en el repositorio.**
- El formato de publicación en Google Play es `.aab`, no `.apk`.

---

## 9. Convenciones del proyecto

- **Idioma del código:** inglés (clases, variables, funciones).
- **Idioma de comentarios y strings de usuario:** español.
- **ViewBinding** habilitado — nunca usar `findViewById`.
- **No usar `runOnUiThread`** — usar `lifecycleScope.launch` con `Dispatchers.Main`.
- **No usar `AsyncTask`** — obsoleto; usar corrutinas.
- Layouts con **Material Design 3** (`Theme.Material3.DayNight`).
- Layouts raíz con `android:fitsSystemWindows="true"` para respetar barras del sistema.
- Los elementos de lista usan `MaterialCardView` con color de fondo según `origin`.

---

## 10. Deuda técnica

- `AdditiveDetailActivity` y `ProductResultActivity` duplican la lógica `getVisualsForOrigin()` de `AdditiveAdapter`. Candidata a extraerse a un objeto utilitario.
- `getParcelableExtra<T>()` usa la API deprecada en API 33+. Migrar cuando se eleve el minSdk.
- `IngredientParser` se instancia en cada análisis. Podría convertirse en singleton o inyectarse via ViewModel factory.
- `GlobalActivity` es código legado sin uso real. Se puede eliminar.
- No hay inyección de dependencias (sin Hilt/Dagger). Valorar si la app crece.

---

## 11. Qué NO debe hacer un agente sin confirmación explícita

- Modificar `signingConfigs` en `build.gradle`.
- Cambiar el `versionCode` o `versionName`.
- Eliminar el `EspressoIdlingResource` o sus llamadas en `SplashActivity`.
- Cambiar la lógica de caché en `PreferencesUtils` (riesgo de romper datos en dispositivos de usuarios).
- Modificar `non_vegan_ingredients.json` sin revisar posibles falsos positivos (términos demasiado cortos o genéricos).
- Subir a Google Play o hacer push a producción.
