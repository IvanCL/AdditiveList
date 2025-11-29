package com.icl.additivelist.usescase.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.icl.additivelist.R
import com.icl.additivelist.config.ConfigProperties
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.globals.ADDITIVES
import com.icl.additivelist.models.Additive
import com.icl.additivelist.testing.EspressoIdlingResource
import com.icl.additivelist.usescase.main.MainActivity
import com.icl.additivelist.usescase.tutorial.PREF_TUTORIAL_COMPLETED
import com.icl.additivelist.usescase.tutorial.TutorialActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class SplashActivity : AppCompatActivity() {

    private val TAG = "SPLASH_DEBUG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val preferences = PreferencesUtils(applicationContext)
        val additivesSaved = preferences.getAdditiveList(ADDITIVES)

        if (additivesSaved.isEmpty()) {
            downloadAdditivesAndNavigate()
        } else {
            Log.d(TAG, "Additives found in cache, skipping download.")
            navigateToNextScreen()
        }
    }

    private fun downloadAdditivesAndNavigate() {
        EspressoIdlingResource.increment()
        // Usamos corrutinas para la tarea en segundo plano
        lifecycleScope.launch {
            try {
                Log.d(TAG, "Starting additive download...")
                val url = ConfigProperties.getValue("url.additiveAll", applicationContext)
                Log.d(TAG, "Downloading from: $url")

                // Hacemos la llamada de red en un hilo de IO
                val result = withContext(Dispatchers.IO) {
                    URL(url).readText()
                }

                Log.d(TAG, "Download successful. JSON Result: $result")

                val additiveList = Gson().fromJson(result, Array<Additive>::class.java).toList()
                val preferences = PreferencesUtils(applicationContext)
                preferences.saveAdditiveList(additiveList, ADDITIVES)
                Log.d(TAG, "Additives saved to preferences.")

                navigateToNextScreen()
            } catch (e: Exception) {
                Log.e(TAG, "Error downloading or parsing additives", e)
                navigateToNextScreen()
            } finally {
                EspressoIdlingResource.decrement()
            }
        }
    }

    private fun navigateToNextScreen() {
        val preferences = PreferencesUtils(applicationContext)
        val tutorialCompleted = preferences.getBooleanPreference(PREF_TUTORIAL_COMPLETED)

        val intent = if (tutorialCompleted) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, TutorialActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}