package com.example.blescantest1.screens

import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blescantest1.bletools.PERMISSION_BLUETOOTH_CONNECT
import com.example.blescantest1.bletools.PERMISSION_BLUETOOTH_SCAN
import com.example.blescantest1.viewmodels.DeviceListViewModel

@RequiresPermission(allOf = [PERMISSION_BLUETOOTH_SCAN, PERMISSION_BLUETOOTH_CONNECT])
@Composable
fun ScanningScreen() {
    val viewModel: DeviceListViewModel = hiltViewModel()

    ScanningScreenContent(viewModel)
}

@RequiresPermission(allOf = [PERMISSION_BLUETOOTH_SCAN, PERMISSION_BLUETOOTH_CONNECT])
@Composable
fun ScanningScreenContent(
    viewModel: DeviceListViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()


    Column(
        Modifier.padding(horizontal = 10.dp)
    ) {
        if (uiState.value.isScanningNow) {
            Text("Сканирование...")

            Button(onClick = viewModel::stopScan) {
                Text("Остановить сканирование")
            }
        } else {
            Button(onClick = viewModel::startScan) {
                Text("Запустить сканирование")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(uiState.value.devices) { device ->
                DeviceItem(
                    deviceName = device.name,
                    selectDevice = { },
                    device
                )
            }
        }
    }
}

@Composable
fun DeviceItem(
    deviceName: String?,
    selectDevice: () -> Unit,
    device: BluetoothDevice
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = deviceName ?: "[Unnamed]",
                textAlign = TextAlign.Center,
            )
            Text(
                text = """
                    address: ${device.address}
                """.trimIndent()
            )

            Button(onClick = selectDevice) {
                Text("Connect")
            }
        }
    }
}