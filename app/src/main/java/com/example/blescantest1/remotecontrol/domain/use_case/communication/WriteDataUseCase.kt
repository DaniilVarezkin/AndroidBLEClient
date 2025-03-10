package com.example.blescantest1.remotecontrol.domain.use_case.communication

import android.util.Log
import com.example.blescantest1.remotecontrol.domain.repository.BluetoothCommunicationRepository
import com.example.blescantest1.remotecontrol.domain.repository.BluetoothConnectionRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class WriteDataUseCase @Inject constructor(
    private val communicationRepository: BluetoothCommunicationRepository,
    private val connectionRepository: BluetoothConnectionRepository
) {
    operator fun invoke(stringData: String){
        val connection = connectionRepository.getDeviceConnection()
        if(connection != null && connection.isConnected.value){
            communicationRepository.writeData(connection, stringData.toByteArray())
        } else {
            Log.e("WriteDataUseCase", "Ошибка отправки данных, нет соединения")
        }
    }
}