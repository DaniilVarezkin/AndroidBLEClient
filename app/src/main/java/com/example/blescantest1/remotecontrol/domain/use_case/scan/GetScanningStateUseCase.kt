package com.example.blescantest1.remotecontrol.domain.use_case.scan

import com.example.blescantest1.remotecontrol.domain.manager.BluetoothScanManager
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetScanningStateUseCase @Inject constructor(
    private val bluetoothRepository: BluetoothScanManager
) {
    operator fun invoke(): StateFlow<Boolean> {
        return bluetoothRepository.getScanningState()
    }
}