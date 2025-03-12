package com.example.blescantest1.remotecontrol.data.repository

import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.annotation.RequiresPermission
import com.example.blescantest1.remotecontrol.data.bluetooth.BLEDeviceConnectionImpl
import com.example.blescantest1.remotecontrol.domain.model.BLEDeviceConnection
import com.example.blescantest1.remotecontrol.domain.repository.BluetoothConnectionRepository
import com.example.blescantest1.util.constants.PermissionConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class BluetoothConnectionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : BluetoothConnectionRepository {

    private val deviceConnection = MutableStateFlow<BLEDeviceConnection?>(null)

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun connectToDevice(device: BluetoothDevice): StateFlow<BLEDeviceConnection?> {
        val newConnection = BLEDeviceConnectionImpl(context, device)
        newConnection.connect()
        deviceConnection.update {
            it?.disconnect()
            newConnection
        }
        return deviceConnection.asStateFlow()
    }

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun disconnectDevice() {
        deviceConnection.update {
            it?.disconnect()
            null
        }
    }

    override fun getDeviceConnection(): StateFlow<BLEDeviceConnection?> {
        return deviceConnection.asStateFlow()
    }
}