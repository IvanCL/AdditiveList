package com.icl.additivelist.usescase.additives

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat

import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.globals.ADDITIVES
import com.icl.additivelist.globals.GlobalActivity
import com.icl.additivelist.models.Additive
import kotlinx.android.synthetic.main.activity_find_additives.*
import com.icl.additivelist.R
import com.icl.additivelist.usescase.main.MainActivity
import kotlinx.android.synthetic.main.item_additive.view.*
import kotlinx.android.synthetic.main.item_detail_additive.*
import kotlinx.android.synthetic.main.item_detail_additive.view.*


class AdditivesActivity : GlobalActivity() {

    var additiveList : MutableSet<String>? = null
    var foundsAdditives: ArrayList<Additive> = arrayListOf()

    // region LifeCircle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_additives)

        containerAdditives.layoutManager = LinearLayoutManager(this)
        findAdditive()
    }
    // endregion LifeCircle
    // region UI


    private fun findAdditive() {
        additiveList = PreferencesUtils(this.applicationContext).getPreference(ADDITIVES)

        finderTxt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (!s.isBlank() && s.length >= 2) {
                    if (additiveList != null) {
                        additiveList!!.forEach { additive: String ->
                            run {
                                Log.d("ADITIVOS [BUSQUEDA]", additive)
                                val array = additive.split("|")
                                val item = Additive(
                                    array[0],
                                    array[1],
                                    array[2],
                                    array[3],
                                    array[4],
                                    array[5],
                                    array[6]
                                )
                                if (item.name.contains(s) || item.numb.startsWith("E" + s)) {
                                    foundsAdditives.add(item)
                                    val additiveAdapter =
                                        AdditiveAdapter(foundsAdditives, this@AdditivesActivity)
                                    containerAdditives.adapter = additiveAdapter
                                }

                            }
                        }
                    }
                } else if (s.isBlank() || s.isEmpty()) {
                    foundsAdditives.clear()
                    val additiveAdapter = AdditiveAdapter(arrayListOf(), this@AdditivesActivity)
                    containerAdditives.adapter = additiveAdapter
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                foundsAdditives.clear()
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })
    }
    // endregion UI
}