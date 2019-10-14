package com.icl.additivelist.usescase.products

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.icl.additivelist.R
import com.icl.additivelist.globals.GlobalActivity
import com.icl.additivelist.models.Products
import kotlinx.android.synthetic.main.activity_products_detail.*
import kotlinx.android.synthetic.main.activity_products_detail.productNameTxt


class ProductsDetailActivity : GlobalActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_detail)
        loadDetail()
    }

    private fun loadDetail() {
        val bundle: Bundle? = intent.extras
        val product = bundle!!.get("product") as Products
        //val founds =bundle!!.get("founds") as  ArrayList<Products>

        if (!product.name.isNullOrEmpty() && !product.uses.isNullOrEmpty() && !product.description.isNullOrEmpty() && !product.origin.isNullOrEmpty()) {
            productNameTxt.text = product.name
            productDescriptionTxt.text = product.description
            productOrigin.text = product.origin
            usesTxt.text = product.uses

            // Check type of additive
            var newIconImage: Drawable?
            newIconImage = ContextCompat.getDrawable(this, R.drawable.vegan_icon_ok)
            typeIcon.setImageDrawable(newIconImage!!)
            productOrigin.text = product.origin
            itemProductDetail.setBackgroundColor(ContextCompat.getColor(this, R.color.colorVegan))

            // Check origin
            if (product.origin.trim().equals("No vegano", true)) {
                newIconImage = ContextCompat.getDrawable(this, R.drawable.skull_icon)

                typeIcon.setImageDrawable(newIconImage!!)
                itemProductDetail.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDangerous))
            }else if (product.origin.trim().equals("Dudoso", true)){
                newIconImage = ContextCompat.getDrawable(this, R.drawable.question_icon)
                typeIcon.setImageDrawable(newIconImage!!)
                itemProductDetail.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDoubtful))
            }

            backIcon.setOnClickListener {

                var intent = Intent(this, ProductsActivity::class.java)
                startActivity(intent)

            }
        }
    }
}
