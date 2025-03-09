package com.example.blescantest1.remotecontrol.domain.use_case

import com.example.blescantest1.remotecontrol.domain.repository.BluetoothDevicesRepository
import javax.inject.Inject

class StopScanningUseCase @Inject constructor(private val bluetoothRepository: BluetoothDevicesRepository) {
    suspend operator fun  invoke() {
        bluetoothRepository.stopScanning()
    }
}