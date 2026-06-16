package com.icl.additivelist.usescase.products

object AdditiveParser {

    // Matches E/e optionally followed by a space, then 3-4 digits, optionally a letter suffix (E471, E471a, E 100)
    private val ADDITIVE_REGEX = Regex("""[Ee]\s*(\d{3,4}[a-zA-Z]?)""")

    fun extractAdditiveNumbers(text: String): List<String> {
        return ADDITIVE_REGEX.findAll(text)
            .map { "E${it.groupValues[1].uppercase()}" }
            .distinct()
            .toList()
    }

    // Normalizes stored numb values like "E100(ii)" to "E100" for comparison
    fun normalizeNumb(numb: String): String {
        return Regex("""[Ee]\d{3,4}[a-zA-Z]?""").find(numb)?.value?.uppercase() ?: numb.uppercase()
    }
}
