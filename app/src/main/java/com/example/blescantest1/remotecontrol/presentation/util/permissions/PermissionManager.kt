package com.example.blescantest1.remotecontrol.presentation.util.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

object PermissionManager {
    val ALL_BLE_PERMISSIONS =
        arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
        )

    fun haveAllPermissions(context: Context) =
        ALL_BLE_PERMISSIONS
            .all { context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
}