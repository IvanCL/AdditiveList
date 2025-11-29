package com.icl.additivelist.usescase.additives

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.globals.ADDITIVES
import com.icl.additivelist.globals.GlobalActivity
import com.icl.additivelist.models.Additive
import com.icl.additivelist.databinding.ActivityFindAdditivesBinding

class AdditivesActivity : GlobalActivity() {

    private lateinit var binding: ActivityFindAdditivesBinding
    private lateinit var additiveAdapter: AdditiveAdapter
    private var fullAdditiveList: List<Additive> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindAdditivesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        loadAdditives()
        setupSearch()

        // Mostrar la lista completa al inicio
        additiveAdapter.submitList(fullAdditiveList)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        additiveAdapter = AdditiveAdapter(this)
        binding.containerAdditives.apply {
            layoutManager = LinearLayoutManager(this@AdditivesActivity)
            adapter = additiveAdapter
        }
    }

    private fun loadAdditives() {
        // Leemos la lista de aditivos directamente desde el JSON guardado.
        // No hay más procesamiento manual de Strings.
        fullAdditiveList = PreferencesUtils(this.applicationContext).getAdditiveList(ADDITIVES)
    }

    private fun setupSearch() {
        binding.finderTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val query = s.toString()
                if (query.isBlank()) {
                    additiveAdapter.submitList(fullAdditiveList)
                } else {
                    val filteredList = fullAdditiveList.filter { additive ->
                        additive.name.contains(query, ignoreCase = true) ||
                        (additive.numb.startsWith("E", ignoreCase = true) && additive.numb.contains(query, ignoreCase = true))
                    }
                    additiveAdapter.submitList(filteredList)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}