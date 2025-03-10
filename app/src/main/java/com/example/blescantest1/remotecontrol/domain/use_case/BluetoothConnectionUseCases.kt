package com.example.blescantest1.remotecontrol.domain.use_case

import com.example.blescantest1.remotecontrol.domain.use_case.connection.ConnectDeviceUseCase
import com.example.blescantest1.remotecontrol.domain.use_case.connection.DisconnectDeviceUseCase

data class BluetoothConnectionUseCases(
    val connectDevice: ConnectDeviceUseCase,
    val disconnectDevice: DisconnectDeviceUseCase,
)