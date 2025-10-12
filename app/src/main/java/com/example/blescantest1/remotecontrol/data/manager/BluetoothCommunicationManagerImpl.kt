package com.example.blescantest1.remotecontrol.data.manager

import com.example.blescantest1.remotecontrol.domain.model.bluetooth.AbstractBLEDeviceConnection
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothCommunicationManager
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BluetoothCommunicationManagerImpl @Inject constructor() : BluetoothCommunicationManager {
    override fun writeData(connection: AbstractBLEDeviceConnection, data: ByteArray) {
        if(connection.isConnected.value){
            connection.writeData(data)
        }
    }

    override fun getDataFlow(connection: AbstractBLEDeviceConnection): StateFlow<ByteArray?> {
        return connection.characteristicData
    }
}