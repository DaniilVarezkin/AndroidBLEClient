package com.example.blescantest1.remotecontrol.presentation.device_list_screen

import android.bluetooth.BluetoothDevice
import java.lang.Error

data class DeviceListViewState (
    val isLoading: Boolean = false,
    val devices: List<BluetoothDevice> = emptyList(),
    val error: String? = null
)