package com.icl.additivelist.usescase.tutorial

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.databinding.ActivityTutorialBinding
import com.icl.additivelist.usescase.main.MainActivity

const val PREF_TUTORIAL_COMPLETED = "tutorial_completed"

class TutorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTutorialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.continueButton.setOnClickListener {
            // Guardar que el tutorial ha sido completado
            PreferencesUtils(this).saveBooleanPreference(true, PREF_TUTORIAL_COMPLETED)

            // Navegar a la actividad principal
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Finalizar esta actividad para que el usuario no pueda volver
            finish()
        }
    }
}