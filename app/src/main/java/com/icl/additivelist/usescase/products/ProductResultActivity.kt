package com.icl.additivelist.usescase.products

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.icl.additivelist.R
import com.icl.additivelist.databinding.ActivityProductResultBinding
import com.icl.additivelist.models.Additive
import com.icl.additivelist.models.NonVeganIngredient
import com.icl.additivelist.usescase.additives.AdditiveAdapter

class ProductResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        val verdict = intent.getStringExtra(EXTRA_VERDICT) ?: "Vegano"
        val additives = intent.getParcelableArrayListExtra<Additive>(EXTRA_ADDITIVES) ?: arrayListOf()
        val ingredients = intent.getParcelableArrayListExtra<NonVeganIngredient>(EXTRA_INGREDIENTS) ?: arrayListOf()
        val readText = intent.getStringExtra(EXTRA_READ_TEXT) ?: ""

        renderVerdict(verdict)
        renderAdditives(additives)
        renderIngredients(ingredients)
        renderReadText(readText)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun renderVerdict(verdict: String) {
        binding.verdictText.text = verdict
        val (iconRes, colorRes) = when (verdict.trim()) {
            "No vegano" -> Pair(R.drawable.skull_icon, R.color.colorDangerous)
            "Dudoso" -> Pair(R.drawable.question_icon, R.color.colorDoubtful)
            else -> Pair(R.drawable.vegan_icon_ok, R.color.colorVegan)
        }
        binding.verdictIcon.setImageResource(iconRes)
        binding.verdictCard.setCardBackgroundColor(getColor(colorRes))
    }

    private fun renderAdditives(additives: List<Additive>) {
        binding.additivesHeader.text = getString(R.string.additives_found_count, additives.size)
        if (additives.isEmpty()) {
            binding.emptyAdditivesText.visibility = View.VISIBLE
            binding.additivesRecyclerView.visibility = View.GONE
        } else {
            binding.emptyAdditivesText.visibility = View.GONE
            binding.additivesRecyclerView.visibility = View.VISIBLE
            val adapter = AdditiveAdapter(this)
            binding.additivesRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.additivesRecyclerView.adapter = adapter
            adapter.submitList(additives)
        }
    }

    private fun renderIngredients(ingredients: List<NonVeganIngredient>) {
        binding.ingredientsHeader.text = getString(R.string.ingredients_found_count, ingredients.size)
        if (ingredients.isEmpty()) {
            binding.noIngredientsText.visibility = View.VISIBLE
            binding.ingredientsChipGroup.visibility = View.GONE
        } else {
            binding.noIngredientsText.visibility = View.GONE
            binding.ingredientsChipGroup.visibility = View.VISIBLE
            ingredients.forEach { ingredient ->
                val chip = Chip(this).apply {
                    text = ingredient.term
                    isClickable = false
                    setChipBackgroundColorResource(
                        if (ingredient.origin == "No vegano") R.color.colorDangerous else R.color.colorDoubtful
                    )
                }
                binding.ingredientsChipGroup.addView(chip)
            }
        }
    }

    private fun renderReadText(readText: String) {
        binding.readTextContent.text = readText.ifBlank { getString(R.string.read_text_empty) }
        binding.readTextHeader.setOnClickListener {
            val expanded = binding.readTextContent.visibility == View.VISIBLE
            binding.readTextContent.visibility = if (expanded) View.GONE else View.VISIBLE
            binding.readTextExpandIcon.rotation = if (expanded) 0f else 180f
        }
    }

    companion object {
        const val EXTRA_VERDICT = "verdict"
        const val EXTRA_ADDITIVES = "additives"
        const val EXTRA_INGREDIENTS = "ingredients"
        const val EXTRA_READ_TEXT = "read_text"
    }
}
