package com.example.blescantest1.remotecontrol.presentation.device_list_screen.components

import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.blescantest1.remotecontrol.data.bluetooth.PERMISSION_BLUETOOTH_CONNECT

@RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
@Composable
fun DeviceItem(
    modifier: Modifier = Modifier,
    device: BluetoothDevice
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Text(text = device.name ?: "[Unnamed]", style = MaterialTheme.typography.titleMedium)
            Text(text = "address: ${device.address}", style = MaterialTheme.typography.titleSmall)
        }
    }
}
