package com.example.blescantest1.remotecontrol.domain.use_case

import com.example.blescantest1.remotecontrol.domain.repository.BluetoothDevicesRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class StartScanningUseCase @Inject constructor(private val bluetoothRepository: BluetoothDevicesRepository) {
    operator fun invoke(){
        bluetoothRepository.startScanning()
    }
}