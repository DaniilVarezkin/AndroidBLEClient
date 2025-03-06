package com.example.blescantest1.screens

import android.text.BoringLayout
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.blescantest1.bletools.CTF_SERVICE_UUID


//@Composable
//fun DeviceScreen(
//   viewModel: BLEClientViewModel,
//   isConnected: Boolean,
//   isTargetServiceFound: Boolean,
//   data: String?,
//   commandString: MutableState<String>
//) {
//    Column(
//        Modifier.scrollable(rememberScrollState(), Orientation.Vertical)
//    ) {
//        Button(onClick = viewModel::connectActiveDevice) {
//            Text("1. Соединится")
//        }
//        Text("Соединение установлено: $isConnected")
//        Text("Целевое устройство: $isTargetServiceFound")
//
////        Button(onClick = viewModel::discoverServices, enabled = isConnected) {
////            Text("2. Discover Services")
////        }
//
//        Button(onClick = viewModel::readDataFromActiveDevice, enabled = isConnected && isTargetServiceFound) {
//            Text("3. Read data")
//        }
//        if (data != null) {
//            Text("Found data: $data")
//        }
//
//        TextField(value = commandString.value, onValueChange = {commandString.value = it})
//
//        Button(onClick = viewModel::writeDataToActiveDevice, enabled = isConnected && isTargetServiceFound) {
//            Text("4. Send")
//        }
//
//        OutlinedButton(modifier = Modifier.padding(top = 40.dp),  onClick = viewModel::disconnectActiveDevice) {
//            Text("Disconnect")
//        }
//    }
//}
