package com.icl.additivelist.usescase.additives

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.icl.additivelist.R
import com.icl.additivelist.globals.GlobalActivity
import kotlinx.android.synthetic.main.activity_find_additives.*

class AdditivesActivity : GlobalActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_additives)
        loadFragment()
        /*containerAdditives.layoutManager = LinearLayoutManager(this)

        finderTxt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (!s.isBlank() && s.length>=2) {
                    Toast.makeText(this@AdditivesActivity, "Cambiando texto", Toast.LENGTH_SHORT).show()

                }
            }
        })*/
    }
}