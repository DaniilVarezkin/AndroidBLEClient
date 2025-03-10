package com.example.blescantest1.remotecontrol.presentation.device_list

import android.bluetooth.BluetoothDevice

data class DeviceListViewState (
    val isScanning: Boolean = false,
    val devices: List<BluetoothDevice> = emptyList(),
    val error: String? = null
)