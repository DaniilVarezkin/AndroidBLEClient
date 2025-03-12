package com.example.blescantest1.remotecontrol.domain.use_case.scan

import com.example.blescantest1.remotecontrol.domain.manager.BluetoothScanManager
import javax.inject.Inject

class StopScanningUseCase @Inject constructor(private val bluetoothRepository: BluetoothScanManager) {

    operator fun invoke() {
        bluetoothRepository.stopScanning()
    }
}