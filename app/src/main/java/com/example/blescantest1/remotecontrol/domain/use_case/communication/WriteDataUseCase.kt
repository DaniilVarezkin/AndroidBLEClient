package com.example.blescantest1.remotecontrol.domain.use_case.communication

import android.util.Log
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothCommunicationManager
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothConnectionManager
import javax.inject.Inject

class WriteDataUseCase @Inject constructor(
    private val communicationRepository: BluetoothCommunicationManager,
    private val connectionRepository: BluetoothConnectionManager
) {
    //TODO Убрать connectionRepository и получать BLEDeviceConnection как параметр?
    operator fun invoke(stringData: String){
        val connection = connectionRepository.getDeviceConnection()
        if(connection != null && connection.isConnected.value){
            communicationRepository.writeData(connection, stringData.toByteArray())
        } else {
            Log.e("WriteDataUseCase", "Ошибка отправки данных, нет соединения")
        }
    }
}