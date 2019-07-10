package com.icl.additivelist.usescase.splash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import com.icl.additivelist.R
import com.icl.additivelist.config.ConfigProperties
import java.net.URL
import android.util.Log
import com.google.gson.Gson
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.models.Additive
import com.icl.additivelist.usescase.additives.AdditivesActivity

class SplashActivity :AppCompatActivity() {

    var additiveList : Additive? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getCategories()
    }

    private fun getCategories() {
        var result: String
        Thread {
            result = URL(ConfigProperties.getValue("url.categories", this.applicationContext)).readText()
            this.runOnUiThread {
                Log.d("CATEGORIAS RECIBIDAS", result)

                additiveList = Gson().fromJson(result, Additive::class.java)

                loadCategoriesIntoPreferences()
                var intent = Intent(this@SplashActivity, AdditivesActivity::class.java)
                startActivity(intent)
            }
        }.start()
    }

    private fun loadCategoriesIntoPreferences(){
        if(additiveList!=null){
            PreferencesUtils(this.applicationContext).saveSetPreferences(additiveList!!, "ADDITIVES")
        }
    }



}