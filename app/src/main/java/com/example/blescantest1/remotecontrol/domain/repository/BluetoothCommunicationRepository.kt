package com.example.blescantest1.remotecontrol.domain.repository

import com.example.blescantest1.remotecontrol.domain.model.BLEDeviceConnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothCommunicationRepository {
    fun writeData(connection: BLEDeviceConnection, data: ByteArray)
    fun getDataFlow(connection: BLEDeviceConnection) : StateFlow<ByteArray?>
}