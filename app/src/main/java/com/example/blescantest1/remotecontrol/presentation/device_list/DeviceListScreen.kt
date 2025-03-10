package com.example.blescantest1.remotecontrol.presentation.device_list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blescantest1.remotecontrol.presentation.device_list.components.DeviceItem
import com.example.blescantest1.remotecontrol.presentation.util.components.MyTopBar


@Composable
internal fun DeviceListScreen(
    viewModel: DeviceListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DeviceListScreenContent(state, viewModel::startScanning, viewModel::stopScanning)
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceListScreenContent(
    state: DeviceListViewState,
    onStartScanning: () -> Unit,
    onStopScanning: () -> Unit
) {
    //LoadingDialog(isLoading = state.isScanning)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(title = "Devices")
        }
    ) {
        Column(modifier = Modifier.padding(top = it.calculateTopPadding()).padding(10.dp)) {
            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                if (state.isScanning) {
                    Button(onClick = onStopScanning) {
                        Text("Stop scanning")
                    }
                    Text("Сканирование...", Modifier.padding(horizontal = 10.dp))
                } else {
                    Button(onClick = onStartScanning) {
                        Text("Start scanning")
                    }

                }
            }

            LazyColumn(
                modifier = Modifier.padding(top = 10.dp)
            ) {
                items(state.devices) { device ->
                    DeviceItem(device, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}