package com.icl.additivelist.usescase.additives

import android.os.Bundle

import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.globals.ADDITIVES
import com.icl.additivelist.globals.GlobalActivity
import com.icl.additivelist.models.Additive
import kotlinx.android.synthetic.main.activity_find_additives.*
import android.support.v7.widget.DividerItemDecoration
import com.icl.additivelist.R


class AdditivesActivity : GlobalActivity() {

    var additiveList : MutableSet<String>? = null
    var foundsAdditives: ArrayList<Additive> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_additives)
        additiveList = PreferencesUtils(this.applicationContext).getPreference(ADDITIVES)



        //loadFragment()

        containerAdditives.layoutManager = LinearLayoutManager(this)



        /*containerAdditives.layoutManager = LinearLayoutManager(this)*/

        finderTxt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (!s.isBlank() && s.length>=2) {
                    if (additiveList!=null) {
                        additiveList!!.forEach { additive: String ->
                            run {
                                Log.d("ADITIVOS [BUSQUEDA]", additive)
                                var array = additive.split("|")
                                var item = Additive(array[0],array[1],array[2],array[3],array[4],array[5],array[6])
                                if (item.name.contains(s) || item.numb.startsWith("E"+s)){
                                    foundsAdditives.add(item)
                                    var additiveAdapter = AdditiveAdapter(foundsAdditives, this@AdditivesActivity)
                                    containerAdditives.adapter = additiveAdapter
                                    containerAdditives.addItemDecoration(
                                        DividerItemDecoration(
                                            containerAdditives.getContext(),
                                            DividerItemDecoration.VERTICAL
                                        )
                                    )
                                }

                            }
                        }
                    }
                    //Toast.makeText(this@AdditivesActivity, "Cambiando texto", Toast.LENGTH_SHORT).show()
                }else if (s.isBlank() || s.isEmpty()){
                    foundsAdditives.clear()
                    var additiveAdapter = AdditiveAdapter(arrayListOf(), this@AdditivesActivity)
                    containerAdditives.adapter = additiveAdapter
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
                foundsAdditives.clear()
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })
    }
}