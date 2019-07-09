package com.icl.additivelist

import android.app.Application
import androidx.lifecycle.Lifecycle

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner


/**
 * Application es la la instancia en sí de toda la aplicacion
 * Es por el primer sitio que se pasa al iniciar la aplicacion
 * Responde a un patron singleton, es decir, solo puedo tener una instancia de esta clase.
 * El ejemplo visual es que no puedo tener dos veces abiertas "whatsapp"
 */
class App : Application() {
    companion object {
        var isInForeground = false
    }


    override fun onCreate() {
        super.onCreate()
    }
}