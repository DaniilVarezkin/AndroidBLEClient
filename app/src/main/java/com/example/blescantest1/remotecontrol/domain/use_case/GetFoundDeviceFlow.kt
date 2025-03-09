package com.example.blescantest1.remotecontrol.domain.use_case

import android.bluetooth.BluetoothDevice
import arrow.core.Either
import com.example.blescantest1.remotecontrol.domain.model.BluetoothError
import com.example.blescantest1.remotecontrol.domain.repository.BluetoothDevicesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoundDeviceFlow @Inject constructor(private val bluetoothRepository: BluetoothDevicesRepository) {

    operator fun invoke(): Flow<List<BluetoothDevice>> {
        return bluetoothRepository.getFoundedDevicesFlow()
    }
}