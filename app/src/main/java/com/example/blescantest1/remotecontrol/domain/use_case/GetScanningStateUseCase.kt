package com.example.blescantest1.remotecontrol.domain.use_case

import com.example.blescantest1.remotecontrol.domain.repository.BluetoothDevicesRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetScanningStateUseCase @Inject constructor(
    private val bluetoothRepository: BluetoothDevicesRepository
) {
    operator fun invoke(): StateFlow<Boolean> {
        return bluetoothRepository.getScanningState()
    }
}