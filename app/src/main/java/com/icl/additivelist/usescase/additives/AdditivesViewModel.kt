package com.icl.additivelist.usescase.additives

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.globals.ADDITIVES
import com.icl.additivelist.models.Additive

class AdditivesViewModel(application: Application) : AndroidViewModel(application) {

    // Lista completa de aditivos, cargada una sola vez.
    private var fullAdditiveList: List<Additive> = emptyList()

    // LiveData para la lista filtrada que la vista observará.
    private val _additives = MutableLiveData<List<Additive>>()
    val additives: LiveData<List<Additive>> = _additives

    // LiveData para el estado vacío que la vista observará.
    private val _showEmptyState = MutableLiveData<Boolean>()
    val showEmptyState: LiveData<Boolean> = _showEmptyState

    init {
        loadAdditives()
        // Al inicio, mostramos la lista completa.
        _additives.value = fullAdditiveList
        _showEmptyState.value = fullAdditiveList.isEmpty()
    }

    private fun loadAdditives() {
        fullAdditiveList = PreferencesUtils(getApplication()).getAdditiveList(ADDITIVES)
    }

    fun search(query: String) {
        val filteredList = if (query.isBlank()) {
            fullAdditiveList
        } else {
            fullAdditiveList.filter {
                it.name.contains(query, ignoreCase = true) ||
                (it.numb.startsWith("E", ignoreCase = true) && it.numb.contains(query, ignoreCase = true))
            }
        }
        _additives.value = filteredList
        _showEmptyState.value = filteredList.isEmpty()
    }
}