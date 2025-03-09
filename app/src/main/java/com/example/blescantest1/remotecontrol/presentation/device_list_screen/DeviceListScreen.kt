package com.example.blescantest1.remotecontrol.presentation.device_list_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.blescantest1.remotecontrol.presentation.util.components.LoadingDialog
import com.example.blescantest1.remotecontrol.presentation.util.components.MyTopBar


@Composable
internal fun DeviceListScreen() {

}

@Composable
fun DeviceListScreenContent(
    state: DeviceListViewState,
    modifier: Modifier = Modifier
) {
    LoadingDialog(isLoading = state.isLoading)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(title = "Devices")
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(top = it.calculateTopPadding())
        ) {
            items(state.devices) { device ->

            }
        }
    }
}