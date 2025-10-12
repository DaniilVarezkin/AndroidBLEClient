package com.example.blescantest1.remotecontrol.presentation.util.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val ALL_BLE_PERMISSIONS =
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN
            )
    }

    fun haveAllPermissions() =
        ALL_BLE_PERMISSIONS
            .all { ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED }

    fun havePermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}