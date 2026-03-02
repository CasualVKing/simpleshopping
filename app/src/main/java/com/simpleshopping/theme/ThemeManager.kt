package com.simpleshopping.theme

import android.app.Activity
import android.content.Context
import com.simpleshopping.R

object ThemeManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_THEME = "active_theme"

    enum class AppTheme(val styleRes: Int) {
        NOTEPAD(R.style.Theme_SimpleShopping_Notepad)
    }

    fun applySavedTheme(activity: Activity) {
        val prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val themeName = prefs.getString(KEY_THEME, AppTheme.NOTEPAD.name)
        val theme = try {
            AppTheme.valueOf(themeName ?: AppTheme.NOTEPAD.name)
        } catch (_: IllegalArgumentException) {
            AppTheme.NOTEPAD
        }
        activity.setTheme(theme.styleRes)
    }

    fun setTheme(activity: Activity, theme: AppTheme) {
        activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_THEME, theme.name).apply()
        activity.recreate()
    }
}
