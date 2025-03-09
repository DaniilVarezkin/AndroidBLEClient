package com.example.blescantest1.remotecontrol.domain.repository

import android.bluetooth.BluetoothDevice
import arrow.core.Either
import com.example.blescantest1.remotecontrol.domain.model.BluetoothError
import kotlinx.coroutines.flow.Flow

interface BluetoothDevicesRepository {
    fun getFoundedDevicesFlow() : Either<BluetoothError, Flow<List<BluetoothDevice>>>
    suspend fun startScanning()
    suspend fun stopScanning()
}