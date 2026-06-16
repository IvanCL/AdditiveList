package com.icl.additivelist.usescase.additives

import com.icl.additivelist.models.Additive
import org.junit.Assert.assertEquals
import org.junit.Test

class AdditivesFilteringTest {

    private val sampleAdditives = listOf(
        Additive("1", "E100", "Curcumina", "Colorante", "Vegano", "Ninguno", "details..."),
        Additive("2", "E120", "Cochinilla", "Colorante", "No vegano", "Reacciones alérgicas", "details..."),
        Additive("3", "E441", "Gelatina", "Estabilizante", "No vegano", "Ninguno", "details..."),
        Additive("4", "E901", "Cera de abeja", "Agente de recubrimiento", "Dudoso", "Ninguno", "details...")
    )

    private fun filter(query: String, list: List<Additive>): List<Additive> {
        if (query.isBlank()) return list

        return list.filter {
            it.name.contains(query, ignoreCase = true) ||
            (it.numb.startsWith("E", ignoreCase = true) && it.numb.contains(query, ignoreCase = true))
        }
    }

    @Test
    fun `filtrar por nombre devuelve los resultados correctos`() {
        val result = filter("Cochi", sampleAdditives)
        assertEquals(1, result.size)
        assertEquals("E120", result[0].numb)
    }

    @Test
    fun `filtrar por numero E devuelve el resultado correcto`() {
        val result = filter("100", sampleAdditives)
        assertEquals(1, result.size)
        assertEquals("Curcumina", result[0].name)
    }

    @Test
    fun `filtrar por nombre no distingue mayusculas y minusculas`() {
        val result = filter("gElAtInA", sampleAdditives)
        assertEquals(1, result.size)
        assertEquals("E441", result[0].numb)
    }

    @Test
    fun `filtrar por una query vacia devuelve la lista completa`() {
        val result = filter("", sampleAdditives)
        assertEquals(4, result.size)
    }

    @Test
    fun `filtrar por una query sin resultados devuelve una lista vacia`() {
        val result = filter("XYZ", sampleAdditives)
        assertEquals(0, result.size)
    }

    @Test
    fun `filtrar por parte del nombre devuelve multiples resultados`() {
        val result = filter("C", sampleAdditives)
        assertEquals(3, result.size) // Curcumina, Cochinilla, Cera
    }
}