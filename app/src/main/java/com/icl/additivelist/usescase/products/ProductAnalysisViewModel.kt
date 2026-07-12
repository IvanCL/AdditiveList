package com.icl.additivelist.usescase.products

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.globals.ADDITIVES
import com.icl.additivelist.models.Additive
import com.icl.additivelist.models.NonVeganIngredient
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

data class ProductAnalysisResult(
    val verdict: String,
    val detectedAdditives: List<Additive>,
    val detectedIngredients: List<NonVeganIngredient>,
    val noIssuesFound: Boolean
)

class ProductAnalysisViewModel(application: Application) : AndroidViewModel(application) {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _analysisResult = MutableLiveData<ProductAnalysisResult?>()
    val analysisResult: LiveData<ProductAnalysisResult?> = _analysisResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun analyzeImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val scaledBitmap = scaleBitmap(bitmap)
                val rawText = recognizeText(scaledBitmap)
                val cleanedText = removeTraceWarnings(rawText)
                val allAdditives = PreferencesUtils(getApplication()).getAdditiveList(ADDITIVES)

                // Match additives by E-number (E120, E-120, E 120)
                val eNumbers = AdditiveParser.extractAdditiveNumbers(cleanedText)
                val detectedByNumber = eNumbers.mapNotNull { eNum ->
                    allAdditives.find {
                        AdditiveParser.normalizeNumb(it.numb) == AdditiveParser.normalizeNumb(eNum)
                    }
                }

                // Match additives by name (e.g. "Carmín")
                val detectedByName = AdditiveParser.findAdditivesByName(cleanedText, allAdditives)
                val detectedAdditives = (detectedByNumber + detectedByName).distinctBy { it.numb }

                // Match non-vegan/doubtful ingredients by name (e.g. "leche", "vitamina D3")
                val detectedIngredients = IngredientParser(getApplication()).findProblematicIngredients(cleanedText)

                _analysisResult.value = ProductAnalysisResult(
                    verdict = computeVerdict(detectedAdditives, detectedIngredients),
                    detectedAdditives = detectedAdditives,
                    detectedIngredients = detectedIngredients,
                    noIssuesFound = detectedAdditives.isEmpty() && detectedIngredients.isEmpty()
                )
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearResult() {
        _analysisResult.value = null
        _error.value = null
    }

    private fun computeVerdict(additives: List<Additive>, ingredients: List<NonVeganIngredient>): String = when {
        additives.any { it.origin.trim() == "No vegano" } || ingredients.any { it.origin == "No vegano" } -> "No vegano"
        additives.any { it.origin.trim() == "Dudoso" } || ingredients.any { it.origin == "Dudoso" } -> "Dudoso"
        else -> "Vegano"
    }

    // Removes "Puede contener [trazas de] ..." clauses so their ingredients
    // are not counted as actual ingredients of the product.
    private fun removeTraceWarnings(text: String): String {
        return text.replace(Regex("""puede\s+contener[^.;]*""", RegexOption.IGNORE_CASE), "")
    }

    private fun scaleBitmap(bitmap: Bitmap, maxDimension: Int = 1024): Bitmap {
        val ratio = minOf(maxDimension.toFloat() / bitmap.width, maxDimension.toFloat() / bitmap.height)
        if (ratio >= 1f) return bitmap
        return Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * ratio).toInt(),
            (bitmap.height * ratio).toInt(),
            true
        )
    }

    private suspend fun recognizeText(bitmap: Bitmap): String = suspendCoroutine { cont ->
        val image = InputImage.fromBitmap(bitmap, 0)
        recognizer.process(image)
            .addOnSuccessListener { visionText -> cont.resume(visionText.text) }
            .addOnFailureListener { e -> cont.resumeWithException(e) }
    }

    override fun onCleared() {
        super.onCleared()
        recognizer.close()
    }
}
