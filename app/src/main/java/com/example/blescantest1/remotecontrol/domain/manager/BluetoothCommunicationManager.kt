package com.example.blescantest1.remotecontrol.domain.manager

import com.example.blescantest1.remotecontrol.domain.model.AbstractBLEDeviceConnection
import kotlinx.coroutines.flow.StateFlow

interface BluetoothCommunicationManager {
    fun writeData(connection: AbstractBLEDeviceConnection, data: ByteArray)
    fun getDataFlow(connection: AbstractBLEDeviceConnection) : StateFlow<ByteArray?>
}