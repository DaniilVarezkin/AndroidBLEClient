package com.example.blescantest1.remotecontrol.domain.use_case

import com.example.blescantest1.remotecontrol.domain.use_case.communication.GetDeviceDataFlowUseCase
import com.example.blescantest1.remotecontrol.domain.use_case.communication.WriteDataUseCase

data class BluetoothCommunicationUseCases(
    val getDeviceDataFlow: GetDeviceDataFlowUseCase,
    val writeData: WriteDataUseCase
)