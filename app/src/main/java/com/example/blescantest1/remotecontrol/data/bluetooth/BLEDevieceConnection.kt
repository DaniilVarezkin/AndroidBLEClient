package com.example.blescantest1.remotecontrol.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.blescantest1.remotecontrol.domain.model.BLEDeviceConnection
import com.example.blescantest1.util.constants.BluetoothConstants
import com.example.blescantest1.util.constants.PermissionConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@Suppress("DEPRECATION")
class BLEDeviceConnectionImpl @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT) constructor(
    private val context: Context,
    private val bluetoothDevice: BluetoothDevice
) : BLEDeviceConnection() {
    //TODO добавить тег для логов
    val successfulWritesCount = MutableStateFlow(0)

    private val callback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            val connected = newState == BluetoothGatt.STATE_CONNECTED
            if (connected) {
                _services.value = gatt.services
                gatt.discoverServices()
            }
            _isConnected.value = connected
        }

        @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                _services.value = gatt.services
                enableNotifications() // Включаем подписку на обновления
            } else {
                Log.e("bluetooth1", "Service discovery failed with status: $status")
            }
        }


        @Deprecated("Deprecated in Java")
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val value = characteristic.value ?: ByteArray(0)
                if (characteristic.uuid == BluetoothConstants.CUSTOM_CHARACTERISTIC_UUID) {
                    _characteristicData.value = value
                }
            } else {
                Log.e("bluetooth1", "Deprecated onCharacteristicRead failed with status: $status")
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            Log.d("bluetooth1", "onCharacteristicWrite: ${characteristic.uuid}")
            if (characteristic.uuid == BluetoothConstants.CUSTOM_CHARACTERISTIC_UUID) {
                successfulWritesCount.update { it + 1 }
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            if (characteristic.uuid == BluetoothConstants.CUSTOM_CHARACTERISTIC_UUID) {
                _characteristicData.value = characteristic.value
            }
        }
    }

    private var gatt: BluetoothGatt? = null

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    fun enableNotifications() {
        val service = gatt?.getService(BluetoothConstants.CTF_SERVICE_UUID)
        val characteristic =
            service?.getCharacteristic(BluetoothConstants.CUSTOM_CHARACTERISTIC_UUID)

        if (characteristic == null) {
            Log.e("bluetooth1", "Characteristic not found!")
            return
        }

        // Проверяем, поддерживает ли характеристика Notifications
        if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY == 0) {
            Log.e("bluetooth1", "Characteristic does not support notifications!")
            return
        }

        gatt?.setCharacteristicNotification(characteristic, true)

        val descriptor = characteristic.getDescriptor(BluetoothConstants.CCCD_UUID)
        if (descriptor != null) {
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt?.writeDescriptor(descriptor)
        } else {
            Log.e("bluetooth1", "Descriptor not found!")
        }
    }


    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun disconnect() {
        gatt?.disconnect()
        gatt?.close()
        gatt = null
    }

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun connect() {
        gatt = bluetoothDevice.connectGatt(context, false, callback)
        Log.d("bluetooth1", "connectGatt")
    }

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    fun discoverServices() {
        gatt?.discoverServices()
        Log.d("bluetooth1", "discoverServices")
    }

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun writeData(data: ByteArray) {
        val service = gatt?.getService(BluetoothConstants.CTF_SERVICE_UUID)
        val characteristic =
            service?.getCharacteristic(BluetoothConstants.CUSTOM_CHARACTERISTIC_UUID)
        if (characteristic != null) {
            characteristic.value = data;
            val success = gatt?.writeCharacteristic(characteristic)
            Log.d("bluetooth1", "Write data status: $success")
        }
    }

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun readData() {
        val service = gatt?.getService(BluetoothConstants.CTF_SERVICE_UUID)
        val characteristic =
            service?.getCharacteristic(BluetoothConstants.CUSTOM_CHARACTERISTIC_UUID)
        if (characteristic != null) {
            if (characteristic.properties.and(BluetoothGattCharacteristic.PROPERTY_READ) == 0) {
                Log.e("bluetooth1", "Characteristic is not readable!")
                return
            }
            val success = gatt?.readCharacteristic(characteristic)
            Log.d("bluetooth1", "Read data status: $success")
        }
    }
}