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
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

data class ProductAnalysisResult(
    val verdict: String,
    val detectedAdditives: List<Additive>,
    val noAdditivesFound: Boolean
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
                val allAdditives = PreferencesUtils(getApplication()).getAdditiveList(ADDITIVES)

                // Match by E-number (E120, E-120, E 120)
                val eNumbers = AdditiveParser.extractAdditiveNumbers(rawText)
                val detectedByNumber = eNumbers.mapNotNull { eNum ->
                    allAdditives.find {
                        AdditiveParser.normalizeNumb(it.numb) == AdditiveParser.normalizeNumb(eNum)
                    }
                }

                // Match by additive name (e.g. "Carmín")
                val detectedByName = AdditiveParser.findAdditivesByName(rawText, allAdditives)

                val detected = (detectedByNumber + detectedByName).distinctBy { it.numb }

                _analysisResult.value = ProductAnalysisResult(
                    verdict = computeVerdict(detected),
                    detectedAdditives = detected,
                    noAdditivesFound = detected.isEmpty()
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

    private fun computeVerdict(additives: List<Additive>): String = when {
        additives.any { it.origin.trim() == "No vegano" } -> "No vegano"
        additives.any { it.origin.trim() == "Dudoso" } -> "Dudoso"
        else -> "Vegano"
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
