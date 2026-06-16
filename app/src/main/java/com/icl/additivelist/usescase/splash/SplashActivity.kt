package com.icl.additivelist.usescase.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.icl.additivelist.R
import com.icl.additivelist.config.ConfigProperties
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.databinding.ActivitySplashBinding
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
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.progressBar.visibility = View.VISIBLE
        EspressoIdlingResource.increment()

        lifecycleScope.launch {
            try {
                Log.d(TAG, "Starting additive download...")
                val url = ConfigProperties.getValue("url.additiveAll", applicationContext)
                val result = withContext(Dispatchers.IO) { URL(url).readText() }

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