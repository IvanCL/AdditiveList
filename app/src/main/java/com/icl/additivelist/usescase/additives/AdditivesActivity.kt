package com.icl.additivelist.usescase.additives

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.google.gson.Gson
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.globals.ADDITIVES
import com.icl.additivelist.globals.GlobalActivity
import com.icl.additivelist.models.Additive
import kotlinx.android.synthetic.main.activity_find_additives.*
import com.icl.additivelist.R
import com.icl.additivelist.models.RestResponse


class AdditivesActivity : GlobalActivity() {

    var additiveList: MutableSet<String>? = null
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
                        additiveList!!.forEach { itemAdditive: String ->
                            run {
                                Log.d("ADITIVOS [BUSQUEDA]", itemAdditive)

                                var additive = Gson().fromJson<Additive>(itemAdditive, Additive::class.java)

                                if (additive.name.contains(
                                        s,
                                        true
                                    ) || (additive.numb.startsWith("E") && additive.numb.contains(s))
                                ) {
                                    foundsAdditives.add(additive)
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