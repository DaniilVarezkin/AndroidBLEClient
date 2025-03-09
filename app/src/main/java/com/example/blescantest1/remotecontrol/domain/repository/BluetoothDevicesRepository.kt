package com.example.blescantest1.remotecontrol.domain.repository

import android.bluetooth.BluetoothDevice
import arrow.core.Either
import com.example.blescantest1.remotecontrol.domain.model.BluetoothError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothDevicesRepository {
    fun getFoundedDevicesFlow() : Flow<List<BluetoothDevice>>
    fun startScanning()
    fun stopScanning()
    fun getScanningState() : StateFlow<Boolean>
}