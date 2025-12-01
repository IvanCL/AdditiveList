package com.icl.additivelist.usescase.additives

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.icl.additivelist.databinding.ActivityFindAdditivesBinding

class AdditivesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindAdditivesBinding
    private lateinit var additiveAdapter: AdditiveAdapter
    private val viewModel: AdditivesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindAdditivesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupSearch()
        observeViewModel()
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

    private fun setupSearch() {
        binding.finderTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // Notificamos al ViewModel que la query ha cambiado.
                viewModel.search(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeViewModel() {
        // La Activity observa los cambios en el ViewModel y actualiza la UI.
        viewModel.additives.observe(this) { additives ->
            additiveAdapter.submitList(additives)
        }

        viewModel.showEmptyState.observe(this) { showEmpty ->
            binding.emptyStateLayout.visibility = if (showEmpty) View.VISIBLE else View.GONE
            binding.containerAdditives.visibility = if (showEmpty) View.GONE else View.VISIBLE
        }
    }
}