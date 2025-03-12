package com.example.blescantest1.remotecontrol.domain.use_case.connection

import com.example.blescantest1.remotecontrol.domain.manager.BluetoothConnectionManager
import javax.inject.Inject

class DisconnectDeviceUseCase @Inject constructor(
    private val connectionRepository: BluetoothConnectionManager
) {
    operator fun invoke() {
        connectionRepository.disconnectDevice()
    }
}