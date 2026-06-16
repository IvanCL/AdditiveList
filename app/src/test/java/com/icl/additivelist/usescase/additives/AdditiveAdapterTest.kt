package com.icl.additivelist.usescase.additives

import com.icl.additivelist.R
import org.junit.Assert.assertEquals
import org.junit.Test

class AdditiveAdapterTest {

    @Test
    fun `getVisualsForOrigin con No vegano devuelve icono de calavera y color de peligro`() {
        // Given
        val origin = "No vegano"

        // When
        val (iconRes, colorRes) = AdditiveAdapter.getVisualsForOrigin(origin)

        // Then
        assertEquals(R.drawable.skull_icon, iconRes)
        assertEquals(R.color.colorDangerous, colorRes)
    }

    @Test
    fun `getVisualsForOrigin con Dudoso devuelve icono de pregunta y color de duda`() {
        // Given
        val origin = "Dudoso"

        // When
        val (iconRes, colorRes) = AdditiveAdapter.getVisualsForOrigin(origin)

        // Then
        assertEquals(R.drawable.question_icon, iconRes) // Corregido: iconRes en lugar de colorRes
        assertEquals(R.color.colorDoubtful, colorRes)
    }

    @Test
    fun `getVisualsForOrigin con Vegano devuelve icono OK y color vegano`() {
        // Given
        val origin = "Vegano"

        // When
        val (iconRes, colorRes) = AdditiveAdapter.getVisualsForOrigin(origin)

        // Then
        assertEquals(R.drawable.vegan_icon_ok, iconRes)
        assertEquals(R.color.colorVegan, colorRes)
    }
}