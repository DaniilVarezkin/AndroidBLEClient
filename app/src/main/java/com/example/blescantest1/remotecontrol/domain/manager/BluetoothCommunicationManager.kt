package com.example.blescantest1.remotecontrol.domain.manager

import com.example.blescantest1.remotecontrol.domain.model.BLEDeviceConnection
import kotlinx.coroutines.flow.StateFlow

interface BluetoothCommunicationManager {
    fun writeData(connection: BLEDeviceConnection, data: ByteArray)
    fun getDataFlow(connection: BLEDeviceConnection) : StateFlow<ByteArray?>
}