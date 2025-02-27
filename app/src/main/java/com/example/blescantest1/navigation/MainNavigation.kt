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
import com.example.blescantest1.screens.ScanningScreen

@SuppressLint("MissingPermission")
@Composable
fun MainNavigation(viewModel: BLEClientViewModel = viewModel()) {
    val permissionManager: PermissionManager = PermissionManager();

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    var allPermissionsGranted by remember {
        mutableStateOf (permissionManager.haveAllPermissions(context))
    }



    if(!allPermissionsGranted){
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { granted ->
            if (granted.values.all { it }) {
                allPermissionsGranted = true
            }
        }

        LaunchedEffect(Unit) {
            launcher.launch(permissionManager.ALL_BLE_PERMISSIONS)
        }

        Box {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {

                Button(onClick = { launcher.launch(permissionManager.ALL_BLE_PERMISSIONS) }) {
                    Text("Grant Permission")
                }
            }
        }
    } else if(uiState.activeDevice == null) {
        ScanningScreen(
            isScanning = uiState.isScanning,
            foundDevices = uiState.foundDevices,
            startScanning = viewModel::startScanning,
            stopScanning = viewModel::stopScanning,
            selectDevice = { device ->
                viewModel.stopScanning()
                viewModel.setActiveDevice(device)
            }
        )
    }
    else {
        DeviceScreen(
            unselectDevice = {
                viewModel.disconnectActiveDevice()
                viewModel.setActiveDevice(null)
            },
            isDeviceConnected = uiState.isDeviceConnected,
            discoveredCharacteristics = uiState.discoveredCharacteristics,
            data = uiState.data,
            nameWrittenTimes = uiState.nameWrittenTimes,
            connect = viewModel::connectActiveDevice,
            discoverServices = viewModel::discoverActiveDeviceServices,
            readData = viewModel::readDataFromActiveDevice,
            writeData = viewModel::writeDataFromActiveDevice,
            commandString = viewModel.commandString
        )
    }

}