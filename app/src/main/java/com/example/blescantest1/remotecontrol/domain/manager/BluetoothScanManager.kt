package com.example.blescantest1.remotecontrol.domain.manager

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

interface BluetoothScanManager {
    fun getFoundDevicesFlow() : Flow<List<BluetoothDevice>>
    fun startScanning()
    fun startScanningByTargetService(serviceUUID: UUID)
    fun stopScanning()
    fun getScanningState() : StateFlow<Boolean>
}