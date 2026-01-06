package com.deducto.app.util

import android.Manifest
import android.content.Context
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager

object PermissionUtils {
    fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    // For now, this checks permission and returns boolean. Requesting should be handled by Activity/Compose permission API.
    fun checkAndRequestPermission(context: Context, permission: String): Boolean {
        return hasPermission(context, permission)
    }
}
