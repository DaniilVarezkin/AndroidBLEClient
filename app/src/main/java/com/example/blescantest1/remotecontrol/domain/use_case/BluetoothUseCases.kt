package com.example.blescantest1.remotecontrol.domain.use_case

import javax.inject.Inject

data class BluetoothUseCases @Inject constructor (
    val startScanning: StartScanningUseCase,
    val stopScanning: StopScanningUseCase,
    val getDeviceFlow: GetFoundDeviceFlow,
    val getScanningState: GetScanningStateUseCase,
)