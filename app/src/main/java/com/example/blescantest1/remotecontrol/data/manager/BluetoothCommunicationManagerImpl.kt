package com.example.blescantest1.remotecontrol.data.manager

import com.example.blescantest1.remotecontrol.domain.model.BLEDeviceConnection
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothCommunicationManager
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BluetoothCommunicationManagerImpl @Inject constructor() : BluetoothCommunicationManager {
    override fun writeData(connection: BLEDeviceConnection, data: ByteArray) {
        if(connection.isConnected.value){
            connection.writeData(data)
        }
    }

    override fun getDataFlow(connection: BLEDeviceConnection): StateFlow<ByteArray?> {
        return connection.characteristicData
    }
}