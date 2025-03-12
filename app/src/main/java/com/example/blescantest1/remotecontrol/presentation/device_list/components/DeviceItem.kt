package com.example.blescantest1.remotecontrol.presentation.device_list.components

import android.Manifest
import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
@Composable
fun DeviceItem(
    device: BluetoothDevice,
    onClickConnect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = device.name ?: "[Unnamed]",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "address: ${device.address}",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            OutlinedButton(
                modifier = Modifier.padding(5.dp),
                onClick = onClickConnect
            ) {
                Text("Connect", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
