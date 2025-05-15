package com.icl.additivelist.usescase.products

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.icl.additivelist.R
import com.icl.additivelist.databinding.ActivityProductsDetailBinding // Importa el binding
import com.icl.additivelist.models.Products
import com.icl.additivelist.globals.GlobalActivity

class ProductsDetailActivity : GlobalActivity() {

    private lateinit var binding: ActivityProductsDetailBinding // Declara el binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsDetailBinding.inflate(layoutInflater) // Infla el layout
        setContentView(binding.root) // Usa binding.root
        loadDetail()
    }

    private fun loadDetail() {
        val bundle: Bundle? = intent.extras
        val product = bundle!!.get("product") as Products
        //val founds =bundle!!.get("founds") as  ArrayList<Products>

        if (!product.name.isNullOrEmpty() && !product.uses.isNullOrEmpty() && !product.description.isNullOrEmpty() && !product.origin.isNullOrEmpty()) {
            binding.productNameTxt.text = product.name
            binding.productDescriptionTxt.text = product.description
            binding.productOrigin.text = product.origin
            binding.usesTxt.text = product.uses

            // Check type of additive
            var newIconImage: Drawable?
            newIconImage = ContextCompat.getDrawable(this, R.drawable.vegan_icon_ok)
            binding.typeIcon.setImageDrawable(newIconImage!!)
            binding.productOrigin.text = product.origin
            binding.itemProductDetail.setBackgroundColor(ContextCompat.getColor(this, R.color.colorVegan))

            // Check origin
            if (product.origin.trim().equals("No vegano", true)) {
                newIconImage = ContextCompat.getDrawable(this, R.drawable.skull_icon)
                binding.typeIcon.setImageDrawable(newIconImage!!)
                binding.itemProductDetail.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDangerous))
            } else if (product.origin.trim().equals("Dudoso", true)) {
                newIconImage = ContextCompat.getDrawable(this, R.drawable.question_icon)
                binding.typeIcon.setImageDrawable(newIconImage!!)
                binding.itemProductDetail.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDoubtful))
            }

            binding.backIcon.setOnClickListener {
                val intent = Intent(this, ProductsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}