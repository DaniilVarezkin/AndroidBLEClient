package com.example.blescantest1.remotecontrol.domain.use_case.connection

import android.bluetooth.BluetoothDevice
import com.example.blescantest1.remotecontrol.domain.repository.BluetoothConnectionRepository
import javax.inject.Inject

class ConnectDeviceUseCase @Inject constructor(
    private val connectionRepository: BluetoothConnectionRepository
) {
    operator fun invoke(device: BluetoothDevice) {
        connectionRepository.connectToDevice(device)
    }
}