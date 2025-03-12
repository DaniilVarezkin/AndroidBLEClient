package com.example.blescantest1.remotecontrol.domain.use_case.scan

import android.bluetooth.BluetoothDevice
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothScanManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoundDeviceFlow @Inject constructor(private val bluetoothRepository: BluetoothScanManager) {

    operator fun invoke(): Flow<List<BluetoothDevice>> {
        return bluetoothRepository.getFoundedDevicesFlow()
    }
}