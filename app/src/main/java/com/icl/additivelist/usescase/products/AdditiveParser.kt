package com.icl.additivelist.usescase.products

import com.icl.additivelist.models.Additive

object AdditiveParser {

    // Matches E120, E-120, E 120, e-120, etc. — hyphen and spaces are optional between E and digits
    private val ADDITIVE_REGEX = Regex("""[Ee][-\s]?(\d{3,4}[a-zA-Z]?)""")

    fun extractAdditiveNumbers(text: String): List<String> {
        return ADDITIVE_REGEX.findAll(text)
            .map { "E${it.groupValues[1].uppercase()}" }
            .distinct()
            .toList()
    }

    // Normalizes stored numb values like "E100(ii)" or "E-100" to "E100" for comparison
    fun normalizeNumb(numb: String): String {
        return Regex("""[Ee]\d{3,4}[a-zA-Z]?""").find(numb.replace("-", ""))?.value?.uppercase()
            ?: numb.uppercase()
    }

    // Returns additives whose name appears in the OCR text (case-insensitive, minimum 4 chars to avoid false positives)
    fun findAdditivesByName(text: String, allAdditives: List<Additive>): List<Additive> {
        val normalizedText = text.lowercase()
        return allAdditives.filter { additive ->
            additive.name.length >= 4 && normalizedText.contains(additive.name.lowercase())
        }
    }
}
