package com.example.blescantest1.remotecontrol.domain.model

import android.bluetooth.BluetoothGattService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BLEDeviceConnection {
    protected val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    protected val _services = MutableStateFlow<List<BluetoothGattService>>(emptyList())
    val services = _services.asStateFlow()

    protected val _characteristicData = MutableStateFlow<ByteArray?>(null)
    val characteristicData = _characteristicData.asStateFlow()

    abstract fun connect()
    abstract fun disconnect()
    abstract fun readData()
    abstract fun writeData(data: ByteArray)
}