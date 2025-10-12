package com.example.blescantest1.remotecontrol.domain.manager

import android.bluetooth.BluetoothDevice
import com.example.blescantest1.remotecontrol.domain.model.bluetooth.AbstractBLEDeviceConnection
import kotlinx.coroutines.flow.StateFlow

interface BluetoothConnectionManager {
    fun connectToDevice(device: BluetoothDevice) : StateFlow<AbstractBLEDeviceConnection?>
    fun connectToDevice(address: String) : StateFlow<AbstractBLEDeviceConnection?>
    fun disconnectDevice()
    fun getDeviceConnection() : StateFlow<AbstractBLEDeviceConnection?>
}