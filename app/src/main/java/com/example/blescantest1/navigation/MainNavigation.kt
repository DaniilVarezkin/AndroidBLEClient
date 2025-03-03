package com.example.blescantest1.navigation

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import com.example.blescantest1.permissions.PermissionManager
import com.example.blescantest1.viewmodels.BLEClientViewModel

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.blescantest1.screens.DeviceScreen
import com.example.blescantest1.screens.PermissionScreen
import com.example.blescantest1.screens.ScanningScreen

@SuppressLint("MissingPermission")
@Composable
fun MainNavigation(viewModel: BLEClientViewModel = viewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    var allPermissionsGranted by remember {
        mutableStateOf (PermissionManager.haveAllPermissions(context))
    }



    if(!allPermissionsGranted){
        PermissionScreen {
            allPermissionsGranted = true
        }
    } else if(uiState.activeDevice == null) {
        ScanningScreen(
            isScanning = uiState.isScanning,
            foundDevices = uiState.foundDevices,
            viewModel
        )
    }
    else {
        DeviceScreen(
            viewModel,
            uiState.isDeviceConnected,
            uiState.isTargetServiceFound,
            uiState.data,
            viewModel.commandString
        )
    }

}