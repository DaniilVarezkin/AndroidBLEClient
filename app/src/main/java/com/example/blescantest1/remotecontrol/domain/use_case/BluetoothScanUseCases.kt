package com.example.blescantest1.remotecontrol.domain.use_case

import com.example.blescantest1.remotecontrol.domain.use_case.scan.GetFoundDeviceFlow
import com.example.blescantest1.remotecontrol.domain.use_case.scan.GetScanningStateUseCase
import com.example.blescantest1.remotecontrol.domain.use_case.scan.StartScanningUseCase
import com.example.blescantest1.remotecontrol.domain.use_case.scan.StopScanningUseCase
import javax.inject.Inject

data class BluetoothScanUseCases @Inject constructor (
    val startScanning: StartScanningUseCase,
    val stopScanning: StopScanningUseCase,
    val getDeviceFlow: GetFoundDeviceFlow,
    val getScanningState: GetScanningStateUseCase,
)