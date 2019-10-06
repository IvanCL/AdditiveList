package com.icl.additivelist.usescase.splash

import android.content.Intent
import android.icu.util.TimeZone
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.util.JsonReader
import com.icl.additivelist.R
import com.icl.additivelist.config.ConfigProperties
import java.net.URL
import android.util.Log
import com.google.gson.Gson
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.models.Additive
import com.icl.additivelist.usescase.additives.AdditivesActivity


class SplashActivity :AppCompatActivity() {

    var additiveList : List<Additive>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getAdditives()
    }

    private fun getAdditives() {
        var result: String
            Thread {
                result = URL(
                    ConfigProperties.getValue(
                        "url.additiveAll",
                        this.applicationContext
                    )
                ).readText()
                this.runOnUiThread {
                    Log.d("ADITIVOS RECIBIDOS", result)

                    additiveList = Gson().fromJson(result, Array<Additive>::class.java).toList()

                    loadAdditivesIntoPreferences()
                    var intent = Intent(this@SplashActivity, AdditivesActivity::class.java)
                    startActivity(intent)
                }
            }.start()

    }

    private fun loadAdditivesIntoPreferences(){
        // lateinit var date : LocalDateTime
        // if (Build.VERSION.SDK_INT>=26) date = LocalDateTime.now()

        if(additiveList!=null){
           PreferencesUtils(this.applicationContext).saveSetPreferences(additiveList!!, "ADDITIVES")
        }
    }



}