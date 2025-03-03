package com.example.blescantest1.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.blescantest1.permissions.PermissionManager

@Composable
fun PermissionScreen(
    onPermissionGranted: ()->Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { granted ->
        if (granted.values.all { it }) {
            onPermissionGranted()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(PermissionManager.ALL_BLE_PERMISSIONS)
    }

    Box {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {

            Button(onClick = { launcher.launch(PermissionManager.ALL_BLE_PERMISSIONS) }) {
                Text("Grant Permission")
            }
        }
    }
}