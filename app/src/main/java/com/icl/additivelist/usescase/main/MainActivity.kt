package com.icl.additivelist.usescase.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.icl.additivelist.R
import com.icl.additivelist.usescase.additives.AdditivesActivity
import com.icl.additivelist.usescase.products.ProductsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clickOnAdditives()
        clickOnProducts()
    }

    private fun clickOnAdditives() {
        additivesTxt.setOnClickListener {
            var intent = Intent(this.applicationContext, AdditivesActivity::class.java)
            startActivity(intent)
        }
        additivesImg.setOnClickListener {
            var intent = Intent(this.applicationContext, AdditivesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun clickOnProducts() {
        productsImg.setOnClickListener {
            var intent = Intent(this.applicationContext, ProductsActivity::class.java)
            startActivity(intent)
        }
        productsTxt.setOnClickListener {
            var intent = Intent(this.applicationContext, ProductsActivity::class.java)
            startActivity(intent)
        }
    }
}
