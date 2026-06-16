package com.icl.additivelist.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.icl.additivelist.models.Additive

class PreferencesUtils(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(APP_PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val APP_PREFS_NAME = "additive_list_prefs"
    }

    fun getBooleanPreference(key: String, defaultValue: Boolean = false): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun saveBooleanPreference(value: Boolean, key: String) {
        // Usamos commit() en lugar de apply() para asegurar que la escritura es síncrona.
        // Esto es crucial para la fiabilidad de los tests de UI.
        preferences.edit().putBoolean(key, value).commit()
    }

    fun saveAdditiveList(additiveList: List<Additive>, key: String) {
        val jsonString = gson.toJson(additiveList)
        preferences.edit().putString(key, jsonString).apply()
    }

    fun getAdditiveList(key: String): List<Additive> {
        try {
            val jsonString = preferences.getString(key, null) ?: return emptyList()
            val type = object : TypeToken<List<Additive>>() {}.type
            return gson.fromJson(jsonString, type) ?: emptyList()
        } catch (e: ClassCastException) {
            Log.e("PreferencesUtils", "Old preference format detected. Clearing and forcing re-download.")
            preferences.edit().remove(key).apply()
            return emptyList()
        }
    }

    /**
     * Clears all data from the preferences. Used for testing.
     */
    fun clearAll() {
        preferences.edit().clear().commit()
    }
}