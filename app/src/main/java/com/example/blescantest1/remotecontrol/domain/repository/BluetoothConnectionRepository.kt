package com.example.blescantest1.remotecontrol.domain.repository

import android.bluetooth.BluetoothDevice
import com.example.blescantest1.remotecontrol.domain.model.BLEDeviceConnection

interface BluetoothConnectionRepository {
    fun connectToDevice(device: BluetoothDevice) : BLEDeviceConnection?
    fun disconnectDevice()
    fun getDeviceConnection() : BLEDeviceConnection?
}