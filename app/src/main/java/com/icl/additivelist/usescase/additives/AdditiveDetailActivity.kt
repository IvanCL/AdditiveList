package com.icl.additivelist.usescase.additives

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.icl.additivelist.R
import com.icl.additivelist.databinding.ActivityAdditiveDetailBinding
import com.icl.additivelist.models.Additive

class AdditiveDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdditiveDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdditiveDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // El método getParcelableExtra está obsoleto en API 33+, pero como tu minSdk es 21,
        // este es el método compatible más sencillo.
        val additive = intent.getParcelableExtra<Additive>("additive")

        if (additive == null) {
            // Si por alguna razón no llega el aditivo, cerramos la pantalla para evitar un error.
            finish()
            return
        }

        setupToolbar(additive)
        populateDetails(additive)
    }

    private fun setupToolbar(additive: Additive) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "${additive.numb} - ${additive.name}"
    }

    override fun onSupportNavigateUp(): Boolean {
        // Esto gestiona la pulsación del botón de 'atrás' en la toolbar.
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun populateDetails(additive: Additive) {
        binding.descriptionTxt.text = additive.description
        binding.usesTxt.text = additive.func
        binding.sideEffectsTxt.text = additive.sideEffects
        binding.originAdditive.text = additive.origin

        val (iconRes, _) = getVisualsForOrigin(additive.origin)
        binding.typeIcon.setImageResource(iconRes)
    }

    private fun getVisualsForOrigin(origin: String): Pair<Int, Int> {
        return when (origin.trim()) {
            "No vegano" -> Pair(R.drawable.skull_icon, R.color.colorDangerous)
            "Dudoso" -> Pair(R.drawable.question_icon, R.color.colorDoubtful)
            else -> Pair(R.drawable.vegan_icon_ok, R.color.colorVegan)
        }
    }
}