package com.example.blescantest1.remotecontrol.data.repository

import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.annotation.RequiresPermission
import com.example.blescantest1.remotecontrol.data.bluetooth.BLEDeviceConnectionImpl
import com.example.blescantest1.remotecontrol.domain.model.BLEDeviceConnection
import com.example.blescantest1.remotecontrol.domain.repository.BluetoothConnectionRepository
import com.example.blescantest1.util.constants.PermissionConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BluetoothConnectionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : BluetoothConnectionRepository {

    private var deviceConnection: BLEDeviceConnection? = null

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun connectToDevice(device: BluetoothDevice): BLEDeviceConnection? {
        deviceConnection = BLEDeviceConnectionImpl(context, device)
        deviceConnection?.connect()
        return deviceConnection
    }

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun disconnectDevice() {
        deviceConnection?.disconnect()
        deviceConnection = null
    }

    override fun getDeviceConnection(): BLEDeviceConnection? {
        return deviceConnection
    }
}