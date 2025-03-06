package com.example.blescantest1.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.blescantest1.permissions.PermissionManager
import com.example.blescantest1.screens.PermissionScreen
import com.example.blescantest1.screens.ScanningScreen

@SuppressLint("MissingPermission")
@Composable
fun MainNavigation() {

    val context = LocalContext.current
    var allPermissionsGranted by remember {
        mutableStateOf(PermissionManager.haveAllPermissions(context))
    }

    if (!allPermissionsGranted) {
        PermissionScreen {
            allPermissionsGranted = true
        }
    } else {
        ScanningScreen()
    }

//    else {
//        DeviceScreen(
//            viewModel,
//            uiState.isDeviceConnected,
//            uiState.isTargetServiceFound,
//            uiState.data,
//            viewModel.commandString
//        )
//    }

}