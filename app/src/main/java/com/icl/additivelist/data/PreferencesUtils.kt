package com.icl.additivelist.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.icl.additivelist.models.Additive
import java.util.*

class PreferencesUtils(context: Context) : AppCompatActivity() {

    val context: Context = context


    lateinit var preferences: SharedPreferences

    private fun initPreferences(name: String) {
        /*preferences = PreferenceManager.getDefaultSharedPreferences(context)*/
        preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun saveSetPreferences(nameList: List<Any>, namePreference: String) {

        initPreferences(namePreference)
        val editor = preferences.edit()
        var preferenceElement = getPreference(namePreference) ?: mutableSetOf()
        nameList.forEach { it: Any ->
            preferenceElement.add(it.toString())
        }

        editor.clear()
        editor.putStringSet(namePreference, preferenceElement)
        editor.apply()
        editor.commit()
    }

    fun savePreferences(name: String, namePreference: String) {
        initPreferences(namePreference)
        val editor = preferences.edit()
        var preferenceElement = getPreference(namePreference) ?: mutableSetOf()

        preferenceElement.add(name)
        editor.clear()
        editor.putStringSet(namePreference, preferenceElement)
        editor.apply()
        editor.commit()
    }

    fun getPreference(namePreference: String): MutableSet<String>? {
        initPreferences(namePreference)
        val prfSetElement = preferences.getStringSet(namePreference, null)
        prfSetElement?.forEach { Log.d("Getting preferences", it) }
        return prfSetElement
    }

}