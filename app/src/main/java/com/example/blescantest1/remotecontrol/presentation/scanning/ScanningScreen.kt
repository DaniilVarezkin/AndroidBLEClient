package com.example.blescantest1.remotecontrol.presentation.scanning

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.blescantest1.remotecontrol.presentation.scanning.components.AlertDialogPendingDevice

@Composable
fun ScanningScreen(
    navController: NavController,
    viewModel: ScanScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pendingDevice by viewModel.pendingDevice.collectAsState()

    LaunchedEffect(Unit)
    {
        viewModel.navigateTo.collect {
            if(it != null) navController.navigate(it)
        }
    }

    ScanningScreenContent(
        isScanning = state.isScanning,
        pendingDevice = pendingDevice,
        startScan = { viewModel.startScan() },
        stopScan = { viewModel.stopScan() },
        confirmDevice = { viewModel.confirmDevice() },
        dismissDevice = { viewModel.dismissDevice() }
    )
}

@SuppressLint("MissingPermission")
@Composable
fun ScanningScreenContent(
    isScanning: Boolean,
    pendingDevice: BluetoothDevice?,
    startScan: () -> Unit,
    stopScan: () -> Unit,
    confirmDevice: () -> Unit,
    dismissDevice: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //TODO Подумать как это обработать может использовать DTO UI
        if (pendingDevice != null) {
            AlertDialogPendingDevice(
                title = "Найдено устройство",
                text = "${pendingDevice.name}",
                onConfirm = { confirmDevice() },
                onDismiss = { dismissDevice() }
            )
        }

        if (isScanning) {
            Row {
                CircularProgressIndicator()
                Text("Сканирование...")
            }
            Button(
                onClick = stopScan
            ) {
                Text("Остановить сканирование")
            }
        } else {
            Button(
                onClick = startScan
            ) {
                Text("Запустить сканирование")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScanningScreenPreview() {
    ScanningScreenContent(
        isScanning = true,
        pendingDevice = null,
        startScan = {},
        stopScan = {},
        confirmDevice = {},
        dismissDevice = {}
    )
}