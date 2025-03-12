package com.example.blescantest1.remotecontrol.domain.manager

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothScanManager {
    fun getFoundedDevicesFlow() : Flow<List<BluetoothDevice>>
    fun startScanning()
    fun stopScanning()
    fun getScanningState() : StateFlow<Boolean>
}