package com.example.blescantest1.remotecontrol.domain.repository

import android.bluetooth.BluetoothDevice
import com.example.blescantest1.remotecontrol.domain.model.BLEDeviceConnection
import kotlinx.coroutines.flow.StateFlow

interface BluetoothConnectionRepository {
    //TODO Пересмотреть использование чистого BLEDeviceConnection, возможно вернуть StateFlow
    fun connectToDevice(device: BluetoothDevice) : StateFlow<BLEDeviceConnection?>
    fun disconnectDevice()
    fun getDeviceConnection() : StateFlow<BLEDeviceConnection?>
}