package com.example.blescantest1.remotecontrol.data.manager

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.blescantest1.remotecontrol.data.bluetooth.BLEDeviceConnectionImpl
import com.example.blescantest1.remotecontrol.domain.model.AbstractBLEDeviceConnection
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothConnectionManager
import com.example.blescantest1.util.constants.PermissionConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class BluetoothConnectionManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : BluetoothConnectionManager {
    private val tag = "BluetoothConnectionManager"

    private val deviceConnection = MutableStateFlow<AbstractBLEDeviceConnection?>(null)

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun connectToDevice(device: BluetoothDevice): StateFlow<AbstractBLEDeviceConnection?> {
        val newConnection = BLEDeviceConnectionImpl(context, device)
        newConnection.connect()
        deviceConnection.update {
            it?.disconnect()
            newConnection
        }
        Log.i(tag, "Соединение с устройством: ${device.address}")
        return deviceConnection.asStateFlow()
    }

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun disconnectDevice() {
        deviceConnection.update {
            it?.disconnect()
            null
        }
    }

    override fun getDeviceConnection(): StateFlow<AbstractBLEDeviceConnection?> {
        return deviceConnection.asStateFlow()
    }
}