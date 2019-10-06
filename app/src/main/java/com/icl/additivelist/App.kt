package com.icl.additivelist

import android.app.Application
import android.util.Log


/**
 * Application es la la instancia en sí de toda la aplicacion
 * Es por el primer sitio que se pasa al iniciar la aplicacion
 * Responde a un patron singleton, es decir, solo puedo tener una instancia de esta clase.
 * El ejemplo visual es que no puedo tener dos veces abiertas "whatsapp"
 */
class App : Application(){

    override fun onCreate() {
        super.onCreate()
        Log.d("CICE", "La aplicación se ha iniciado")
    }
}