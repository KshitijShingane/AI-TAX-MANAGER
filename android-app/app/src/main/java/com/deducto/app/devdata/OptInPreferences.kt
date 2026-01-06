package com.deducto.app.devdata

import android.content.Context

object OptInPreferences {
    private const val PREFS = "deducto_prefs"
    private const val KEY_DEV_DATA = "dev_dataset_opt_in"

    fun isOptedIn(context: Context): Boolean {
        val shared = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return shared.getBoolean(KEY_DEV_DATA, false)
    }

    fun setOptIn(context: Context, optedIn: Boolean) {
        val shared = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        shared.edit().putBoolean(KEY_DEV_DATA, optedIn).apply()
    }
}
