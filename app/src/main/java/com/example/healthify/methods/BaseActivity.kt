package com.example.healthify.methods

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.healthify.PrefsManager
import com.example.healthify.utils.LocaleHelper

open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        val prefs = PrefsManager(newBase)
        val langCode = prefs.getLanguage()
        val localizedContext = LocaleHelper.applyLocale(newBase, langCode)
        super.attachBaseContext(localizedContext)
    }
}