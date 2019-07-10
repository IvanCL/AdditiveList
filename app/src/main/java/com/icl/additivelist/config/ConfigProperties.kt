package com.icl.additivelist.config

import android.content.Context
import android.content.res.AssetManager
import java.io.InputStream
import java.util.*

class ConfigProperties {

    companion object {
        /**
         * Get a property value from 'config.properties' file
         */
        fun getValue(key: String, context: Context): String {
            val properties = Properties()
            val assetManager: AssetManager = context.assets
            val inputStream: InputStream = assetManager.open("config.properties")
            properties.load(inputStream)
            return properties.getProperty(key)
        }
    }

}