# Roadmap — Mejora de Interfaz Gráfica (AdditiveList / Vegan Scan)

Rama: `feature/ui-revamp` (creada desde `master`, incluye el commit de limpieza de `.gitignore`).

## Contexto

El proyecto ya migró a **Material 3** (`Theme.Material3.DayNight.NoActionBar`), tiene paleta de colores M3 completa con soporte claro/oscuro, y pantallas como `ProductsActivity` o `ProductResultActivity` están bien resueltas (toolbar, `MaterialCardView`, `ChipGroup`, estados de carga). Sin embargo, la migración no llegó a todas las pantallas por igual, y hay inconsistencias visuales entre módulos. El objetivo de esta rama es nivelar toda la app al mismo estándar visual y pulir los puntos débiles detectados.

## Hallazgos (por prioridad)

### 1. Pantalla principal (`MainActivity`) — el eslabón más débil
- No tiene `AppBarLayout`/`Toolbar`, a diferencia del resto de pantallas.
- Es un `ConstraintLayout` plano con dos textos ("ADITIVOS"/"PRODUCTOS") e imágenes a modo de botón, sin afordancia de tarjeta/botón ni feedback táctil (ripple).
- Textos hardcodeados en el XML en vez de `strings.xml`.
- Es la primera pantalla que ve el usuario tras el splash/tutorial y es la que menos transmite la identidad "Material 3 / Vegan Scan" del resto de la app.

**Propuesta:** rediseñar como pantalla de inicio con toolbar propio, header de marca y dos `MaterialCardView` grandes (tipo "tiles") para Aditivos/Productos, con icono, título y subtítulo, ripple y elevación — coherente con el resto de tarjetas de la app.

### 2. Inconsistencia entre listados: `item_additive.xml` vs `item_product.xml`
- `item_additive.xml` usa `MaterialCardView` (con elevación y esquinas redondeadas).
- `item_product.xml` usa un `ConstraintLayout` plano sin tarjeta ni elevación, con `Guideline`s sin usar (código muerto) y márgenes inconsistentes.
- Resultado: el listado de aditivos y el de productos analizados se ven como si fueran de apps distintas.

**Propuesta:** unificar `item_product.xml` al mismo lenguaje visual que `item_additive.xml` (tarjeta, jerarquía tipográfica, icono de veredicto alineado) y limpiar los `Guideline` no usados.

### 3. Buscador de aditivos sin componentes Material
- `activity_find_additives.xml` usa un `EditText` suelto con `hint`, sin `TextInputLayout`/`TextInputEditText`, sin icono de búsqueda ni botón de limpiar.

**Propuesta:** sustituir por `TextInputLayout` estilo `Widget.Material3.TextInputLayout.OutlinedBox` con icono de búsqueda inicial y acción de limpiar texto.

### 4. Iconografía en PNG en vez de vectores
- Todos los iconos (`food_additive.png`, `products.png`, `vegan_icon_ok.png`, `skull_icon.png`, `question_icon.png`, `back_arrow_icon.png`) son rasterizados: no se pueden `tint`-ear, no se adaptan al tema/color dinámico y probablemente vienen de packs distintos (estilos de trazo inconsistentes entre ellos).

**Propuesta:** sustituir por `VectorDrawable`s (Material Symbols u otro set consistente) tintados con los colores semánticos (`colorVegan`, `colorDoubtful`, `colorDangerous`) para que se adapten automáticamente a claro/oscuro.

### 5. Colores semánticos sin variante oscura
- `colorVegan`, `colorDoubtful`, `colorDangerous` están definidos solo en `values/colors.xml`, sin equivalente en `values-night/colors.xml`. En tema oscuro pueden perder contraste/legibilidad (p. ej. el amarillo `#FFC107` sobre fondo oscuro puede chocar con el resto de la paleta M3 oscura).

**Propuesta:** definir variantes oscuras armonizadas con la paleta M3 dark ya existente.

### 6. Sin Material You (color dinámico)
- Existe la paleta M3 completa pero no se invoca `DynamicColors.applyToActivitiesIfAvailable()` en la `Application`. Es una mejora de bajo esfuerzo y alto impacto visual en Android 12+.

### 7. Splash screen mínimo
- `activity_splash.xml` es solo un `ProgressBar` centrado sin marca.

**Propuesta:** adoptar la API `androidx.core:core-splashscreen` para un splash nativo con icono/branding y transición suave, evitando el "flash" en frío.

### 8. Accesibilidad
- Varios `ImageView` con significado (icono de veredicto, icono vegano/no vegano) usan `tools:ignore="ContentDescription"` en vez de una descripción real, por lo que TalkBack no los anuncia.

**Propuesta:** añadir `contentDescription` reales (pueden ser dinámicos, p. ej. "Producto apto para veganos").

### 9. Limpieza de recursos no usados
- `strings.xml` conserva cadenas de una pantalla de login que no existe en la app actual (`title_activity_login`, `prompt_email`, `prompt_password`, `action_sign_in`, etc.), y algunas están en inglés mientras el resto de la app está en español. No es un cambio visual pero ensucia el proyecto y conviene retirarlo en la misma pasada.

## Propuesta de fases

1. **Fase 1 — Base visual coherente** (mayor impacto/menor riesgo)
   - Rediseño de `MainActivity` con tiles tipo tarjeta.
   - Unificación de `item_product.xml` con `item_additive.xml`.
   - Buscador con `TextInputLayout`.
2. **Fase 2 — Sistema de color y marca**
   - Colores semánticos con variante oscura.
   - Activar Material You (`DynamicColors`).
   - Splash nativo con `core-splashscreen`.
3. **Fase 3 — Iconografía y accesibilidad**
   - Migrar iconos PNG a vectores tintables.
   - Añadir `contentDescription` reales.
4. **Fase 4 — Limpieza**
   - Eliminar strings/recursos muertos de login.

## Siguiente paso

Dime si seguimos con esta propuesta tal cual, si quieres reordenar prioridades, o si prefieres acotar el alcance a una sola fase para esta rama.
