package com.icl.additivelist.usescase.splash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.icl.additivelist.R
import com.icl.additivelist.config.ConfigProperties
import java.net.URL
import android.util.Log
import com.google.gson.Gson
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.globals.ADDITIVES
import com.icl.additivelist.globals.DAYS_FROM_ADDITIVES_UPDATE
import com.icl.additivelist.models.Additive
import com.icl.additivelist.models.RestResponse
import com.icl.additivelist.usescase.main.MainActivity
import java.util.Arrays.asList


class SplashActivity :AppCompatActivity() {

    var additiveList : List<Additive>? = null
    var days : MutableSet<String>? = mutableSetOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var additivesSaved:  MutableSet<String>? = PreferencesUtils(this.applicationContext).getPreference(ADDITIVES)
        days = PreferencesUtils(this.applicationContext).getPreference(DAYS_FROM_ADDITIVES_UPDATE)

        if(additivesSaved.isNullOrEmpty() || (days.isNullOrEmpty() || (!days.isNullOrEmpty() && checkLastUpdateOfAdditives()))){
            getAdditives()
        }else{
            var intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
        }
        var intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
    }

    /**
     * Check the last day when shared preferences were updated
     */
    private fun checkLastUpdateOfAdditives(): Boolean {
        var any =false
        if (days != null) {
            var any = days!!.any { p: String ->
                run {
                    var a = Integer.parseInt(p)
                    if (a > 7) {
                        days!!.clear()
                        days!!.add("0")
                        return true
                    }
                    Log.d("ADITIVOS AL DIA", "ADITIVOS AL DIA")
                    days!!.clear()
                    a += 1
                    days!!.add(a.toString())
                    PreferencesUtils(this.applicationContext).saveSetPreferences(days!!.toList(), DAYS_FROM_ADDITIVES_UPDATE)
                    return false
                }
            }
        }
        return any
    }

    /**
     * Gets the updated list of additives
     */
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

                    additiveList = Gson().fromJson<RestResponse<List<Additive>>>(result, RestResponse::class.java).data //.data.asList() //as List<Additive>
                    Log.d("ADITIVOS EN VARIABLE", additiveList.toString())

                    loadAdditivesIntoPreferences()
                    /*var intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)*/
                }
            }.start()

    }

    /**
     * Save and update additives and timer
     */
    private fun loadAdditivesIntoPreferences(){
        if(additiveList!=null){
            Log.d("ADITIVOS ACTUALIZADOS", "ADITIVOS ACTUALIZADOS")
           PreferencesUtils(this.applicationContext).saveSetPreferences(additiveList!!, ADDITIVES)
            if (days.isNullOrEmpty())
                days  = mutableSetOf("1")
           PreferencesUtils(this.applicationContext).saveSetPreferences(days!!.toList(), DAYS_FROM_ADDITIVES_UPDATE)
        }
    }



}