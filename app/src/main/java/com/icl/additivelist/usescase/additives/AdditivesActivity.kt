package com.icl.additivelist.usescase.additives

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
        updateList(fullAdditiveList)
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
        fullAdditiveList = PreferencesUtils(this.applicationContext).getAdditiveList(ADDITIVES)
    }

    private fun setupSearch() {
        binding.finderTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val query = s.toString()
                val filteredList = if (query.isBlank()) {
                    fullAdditiveList
                } else {
                    fullAdditiveList.filter { additive ->
                        additive.name.contains(query, ignoreCase = true) ||
                        (additive.numb.startsWith("E", ignoreCase = true) && additive.numb.contains(query, ignoreCase = true))
                    }
                }
                updateList(filteredList)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateList(list: List<Additive>) {
        additiveAdapter.submitList(list)
        if (list.isEmpty()) {
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.containerAdditives.visibility = View.GONE
        } else {
            binding.emptyStateLayout.visibility = View.GONE
            binding.containerAdditives.visibility = View.VISIBLE
        }
    }
}