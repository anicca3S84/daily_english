package com.codework.dailyenglish.core.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object PermissionManager {

    fun getLocationPermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    fun getBluetoothPermissions(): Array<String> {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        return permissions.toTypedArray()
    }

    fun getNotificationPermission(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            emptyArray()
        }
    }

    fun getMeasurementPermissions(): Array<String> {
        val allPerms = mutableListOf<String>()
        allPerms.addAll(getLocationPermissions())
        allPerms.addAll(getBluetoothPermissions())
        allPerms.addAll(getNotificationPermission())
        return allPerms.toTypedArray()
    }

    fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all { per ->
            ContextCompat.checkSelfPermission(context, per) == PackageManager.PERMISSION_GRANTED
        }
    }
}