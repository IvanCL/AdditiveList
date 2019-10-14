package com.icl.additivelist.usescase.additives

import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.icl.additivelist.R
import com.icl.additivelist.models.Additive
import kotlinx.android.synthetic.main.activity_additive_detail.*

class AdditiveDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additive_detail)
        loadDetail()
    }

    private fun loadDetail() {
        val bundle: Bundle? = intent.extras
        val additive = bundle!!.get("additive") as Additive
        val founds =bundle!!.get("founds") as  ArrayList<Additive>

        if (!additive.name.isNullOrEmpty() && !additive.numb.isNullOrEmpty() && !additive.description.isNullOrEmpty() && !additive.origin.isNullOrEmpty()) {
            additiveNameTxt.text = additive.name
            additiveNumbTxt.text = additive.numb
            descriptionTxt.text = additive.description
            usesTxt.text = additive.func
            sideEffectsTxt.text = additive.sideEffects

            // Check type of additive
            var newIconImage: Drawable?
            newIconImage = ContextCompat.getDrawable(this, R.drawable.vegan_icon_ok)
            typeIcon.setImageDrawable(newIconImage!!)
            originAdditive.text = additive.origin
            itemAdditive.setBackgroundColor(ContextCompat.getColor(this, R.color.colorVegan))

            // Check origin
            if (additive.origin.trim().equals("No vegano", true)) {
                newIconImage = ContextCompat.getDrawable(this, R.drawable.skull_icon)

                typeIcon.setImageDrawable(newIconImage!!)
                itemAdditive.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDangerous))
            }else if (additive.origin.trim().equals("Dudoso", true)){
                newIconImage = ContextCompat.getDrawable(this, R.drawable.question_icon)
                typeIcon.setImageDrawable(newIconImage!!)
                itemAdditive.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDoubtful))
            }

            backIcon.setOnClickListener {

                var intent = Intent(this, AdditivesActivity::class.java)
                intent.putExtra("founds", founds)
                startActivity(intent)

            }
        }
    }
}
