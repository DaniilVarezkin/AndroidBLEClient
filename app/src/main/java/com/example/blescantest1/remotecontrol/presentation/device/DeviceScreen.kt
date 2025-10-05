package com.example.blescantest1.remotecontrol.presentation.device

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun DeviceScreen() {
    DeviceScreenContent("")
}

@Composable
fun DeviceScreenContent(
    deviceName: String
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
    ) {
        Text("Управление устройством $deviceName", fontSize = 20.sp)
    }
}

@Composable
@Preview(showBackground = true)
fun DeviceScreenContentPreview() {
    DeviceScreenContent(
        deviceName = "HMSoft"
    )
}