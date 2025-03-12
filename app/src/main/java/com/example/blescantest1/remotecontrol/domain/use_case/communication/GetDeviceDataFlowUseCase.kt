package com.example.blescantest1.remotecontrol.domain.use_case.communication

import android.util.Log
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothCommunicationManager
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothConnectionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class GetDeviceDataFlowUseCase @Inject constructor(
    private val communicationRepository: BluetoothCommunicationManager,
    private val connectionRepository: BluetoothConnectionManager
) {
    //TODO Убрать connectionRepository и получать BLEDeviceConnection как параметр?
    operator fun invoke() : Flow<String?> {
        val connection = connectionRepository.getDeviceConnection()
        if(connection != null && connection.isConnected.value){
             return communicationRepository.getDataFlow(connection).map { it?.toString() }
        } else {
            Log.e("WriteDataUseCase", "Ошибка, нет соединения")
            throw IOException("Нет соединения с устройством")
        }
    }
}
