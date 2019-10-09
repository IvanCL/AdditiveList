package com.icl.additivelist.usescase.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.icl.additivelist.R
import com.icl.additivelist.usescase.additives.AdditivesActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var intent = Intent(this@MainActivity, AdditivesActivity::class.java)
        startActivity(intent)
    }
}
