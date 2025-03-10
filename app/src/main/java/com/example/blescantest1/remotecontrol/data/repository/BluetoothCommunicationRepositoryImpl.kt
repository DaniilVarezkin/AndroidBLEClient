package com.example.blescantest1.remotecontrol.data.repository

import com.example.blescantest1.remotecontrol.domain.model.BLEDeviceConnection
import com.example.blescantest1.remotecontrol.domain.repository.BluetoothCommunicationRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BluetoothCommunicationRepositoryImpl @Inject constructor() : BluetoothCommunicationRepository {
    override fun writeData(connection: BLEDeviceConnection, data: ByteArray) {
        if(connection.isConnected.value){
            connection.writeData(data)
        }
    }

    override fun getDataFlow(connection: BLEDeviceConnection): StateFlow<ByteArray?> {
        return connection.characteristicData
    }
}