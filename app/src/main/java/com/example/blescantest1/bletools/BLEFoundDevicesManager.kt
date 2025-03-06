package com.example.blescantest1.bletools

import android.bluetooth.BluetoothDevice
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class BLEFoundDevicesManager @Inject constructor(
    private val bleScanner: BLEScanner
) {
    private val TAG = "BLEFoundDevicesManager"
    val foundedDevices = bleScanner.foundDevicesFlow.runningFold(emptySet<BluetoothDevice>()) { acc, device ->
        acc + device
    }
}