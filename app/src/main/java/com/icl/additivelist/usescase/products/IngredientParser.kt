package com.icl.additivelist.usescase.products

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.icl.additivelist.models.NonVeganIngredient

class IngredientParser(context: Context) {

    private val ingredients: List<NonVeganIngredient> by lazy { loadIngredients(context) }

    private fun loadIngredients(context: Context): List<NonVeganIngredient> {
        return try {
            val json = context.assets.open("non_vegan_ingredients.json").bufferedReader().readText()
            val type = object : TypeToken<List<NonVeganIngredient>>() {}.type
            Gson().fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun findProblematicIngredients(text: String): List<NonVeganIngredient> {
        val normalizedText = text.lowercase()
        return ingredients.filter { containsWholeWord(normalizedText, it.term.lowercase()) }
    }

    // Checks that the term appears surrounded by non-letter/digit chars to avoid
    // false positives (e.g. "leche" inside "lechuga"), and skips occurrences directly
    // followed by "vegano/a" (e.g. "queso vegano"), which are explicitly vegan variants.
    private fun containsWholeWord(text: String, term: String): Boolean {
        var startIndex = 0
        while (true) {
            val index = text.indexOf(term, startIndex)
            if (index == -1) return false
            val before = if (index > 0) text[index - 1] else ' '
            val after = if (index + term.length < text.length) text[index + term.length] else ' '
            val isWholeWord = !before.isLetterOrDigit() && !after.isLetterOrDigit()
            if (isWholeWord && !isFollowedByVeganModifier(text, index + term.length)) return true
            startIndex = index + 1
        }
    }

    // Only whitespace (no commas) is allowed between the term and the modifier, so
    // "queso, vegano" (a separate list item) still counts as a match, unlike "queso vegano".
    private fun isFollowedByVeganModifier(text: String, afterTermIndex: Int): Boolean {
        var i = afterTermIndex
        while (i < text.length && text[i] == ' ') i++
        return VEGAN_MODIFIERS.any { modifier ->
            text.startsWith(modifier, i) &&
                (i + modifier.length >= text.length || !text[i + modifier.length].isLetterOrDigit())
        }
    }

    private companion object {
        val VEGAN_MODIFIERS = setOf("vegano", "vegana", "veganos", "veganas")
    }
}
