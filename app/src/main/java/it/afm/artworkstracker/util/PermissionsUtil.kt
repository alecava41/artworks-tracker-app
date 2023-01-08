package it.afm.artworkstracker.util

import android.os.Build

object PermissionsUtil {
    fun getPermissionsList(): List<String> {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            arrayListOf(android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.BLUETOOTH_CONNECT)
            else arrayListOf(android.Manifest.permission.BLUETOOTH, android.Manifest.permission.BLUETOOTH_ADMIN)

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)

        return permissions
    }
}