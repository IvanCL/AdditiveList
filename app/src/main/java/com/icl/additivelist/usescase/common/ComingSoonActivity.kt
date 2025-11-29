package com.icl.additivelist.usescase.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.icl.additivelist.R

class ComingSoonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coming_soon)
        // Opcional: Configurar una Toolbar con un botón de atrás si se desea.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Información"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}