package com.icl.additivelist.usescase.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.icl.additivelist.R
import com.icl.additivelist.databinding.ActivityMainBinding // Importa el binding
import com.icl.additivelist.usescase.additives.AdditivesActivity
import com.icl.additivelist.usescase.products.ProductsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Declara el binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Infla el layout
        setContentView(binding.root) // Usa binding.root

        clickOnAdditives()
        clickOnProducts()
    }

    private fun clickOnAdditives() {
        binding.additivesTxt.setOnClickListener { // Usa binding
            val intent = Intent(this.applicationContext, AdditivesActivity::class.java)
            startActivity(intent)
        }
        binding.additivesImg.setOnClickListener { // Usa binding
            val intent = Intent(this.applicationContext, AdditivesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun clickOnProducts() {
        binding.productsImg.setOnClickListener { // Usa binding
            val intent = Intent(this.applicationContext, ProductsActivity::class.java)
            startActivity(intent)
        }
        binding.productsTxt.setOnClickListener { // Usa binding
            val intent = Intent(this.applicationContext, ProductsActivity::class.java)
            startActivity(intent)
        }
    }
}