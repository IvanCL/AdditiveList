package com.icl.additivelist.usescase.additives

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.icl.additivelist.R
import com.icl.additivelist.databinding.ActivityAdditiveDetailBinding // Importa el binding
import com.icl.additivelist.models.Additive
import com.icl.additivelist.usescase.additives.AdditivesActivity

class AdditiveDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdditiveDetailBinding // Declara el binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdditiveDetailBinding.inflate(layoutInflater) // Infla el layout con binding
        setContentView(binding.root) // Usa binding.root como la vista raíz
        loadDetail()
    }

    private fun loadDetail() {
        val bundle: Bundle? = intent.extras
        val additive = bundle!!.get("additive") as Additive
        val founds = bundle!!.get("founds") as ArrayList<Additive>

        if (!additive.name.isNullOrEmpty() && !additive.numb.isNullOrEmpty() && !additive.description.isNullOrEmpty() && !additive.origin.isNullOrEmpty()) {
            binding.additiveNameTxt.text = additive.name
            binding.additiveNumbTxt.text = additive.numb
            binding.descriptionTxt.text = additive.description
            binding.usesTxt.text = additive.func
            binding.sideEffectsTxt.text = additive.sideEffects

            // Check type of additive
            var newIconImage: Drawable?
            newIconImage = ContextCompat.getDrawable(this, R.drawable.vegan_icon_ok)
            binding.typeIcon.setImageDrawable(newIconImage!!)
            binding.originAdditive.text = additive.origin
            binding.itemAdditive.setBackgroundColor(ContextCompat.getColor(this, R.color.colorVegan))

            // Check origin
            if (additive.origin.trim().equals("No vegano", true)) {
                newIconImage = ContextCompat.getDrawable(this, R.drawable.skull_icon)
                binding.typeIcon.setImageDrawable(newIconImage!!)
                binding.itemAdditive.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDangerous))
            } else if (additive.origin.trim().equals("Dudoso", true)) {
                newIconImage = ContextCompat.getDrawable(this, R.drawable.question_icon)
                binding.typeIcon.setImageDrawable(newIconImage!!)
                binding.itemAdditive.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDoubtful))
            }

            binding.backIcon.setOnClickListener {
                val intent = Intent(this, AdditivesActivity::class.java)
                intent.putExtra("founds", founds)
                startActivity(intent)
            }
        }
    }
}