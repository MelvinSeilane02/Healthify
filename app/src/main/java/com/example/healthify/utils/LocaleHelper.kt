package com.example.healthify.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleHelper {

    /**
     * Apply the selected locale to the given context.
     * Returns a localized context that must be used when inflating UI.
     */
    fun applyLocale(context: Context, langCode: String): Context {
        val locale = Locale.forLanguageTag(langCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // For Android 7.0+ (API 24+)
            config.setLocale(locale)
            context.createConfigurationContext(config)
        } else {
            // For older devices
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }
}
