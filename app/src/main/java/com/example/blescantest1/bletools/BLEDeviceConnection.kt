package com.example.blescantest1.bletools

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

val CTF_SERVICE_UUID: UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")

val CUSTOM_CHARACTERISTIC_UUID: UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
val CCCD_UUID: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")



@Suppress("DEPRECATION")
class BLEDeviceConnection @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT) constructor(
    private val context: Context,
    val bluetoothDevice: BluetoothDevice
) {
    val isConnected = MutableStateFlow(false)
    val successfulWritesCount = MutableStateFlow(0)
    val services = MutableStateFlow<List<BluetoothGattService>>(emptyList())
    val customCharacteristicData = MutableStateFlow<String?>(null)


    private val callback = object: BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            val connected = newState == BluetoothGatt.STATE_CONNECTED
            if (connected) {
                services.value = gatt.services
                gatt.discoverServices()
                Log.d("bluetooth1", "Discover Services in onConnectionStateChange ")

            }
            isConnected.value = connected
        }
        @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("bluetooth1", "Services discovered!")
                services.value = gatt.services
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
                Log.d("bluetooth1", "Deprecated onCharacteristicRead: ${characteristic.uuid} -> ${String(value)}")
                if (characteristic.uuid == CUSTOM_CHARACTERISTIC_UUID) {
                    customCharacteristicData.value = String(value)
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
            if (characteristic.uuid == CUSTOM_CHARACTERISTIC_UUID) {
                successfulWritesCount.update { it + 1 }
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            Log.d("bluetooth1", "onCharacteristicChanged: ${characteristic.uuid}")

            if(characteristic.uuid == CUSTOM_CHARACTERISTIC_UUID){
                customCharacteristicData.value = String(characteristic.value)
            }
        }
    }

    private var gatt: BluetoothGatt? = null

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun enableNotifications() {
        val service = gatt?.getService(CTF_SERVICE_UUID)
        val characteristic = service?.getCharacteristic(CUSTOM_CHARACTERISTIC_UUID)

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

        val descriptor = characteristic.getDescriptor(CCCD_UUID)
        if (descriptor != null) {
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt?.writeDescriptor(descriptor)
            Log.d("bluetooth1", "Notifications enabled for ${characteristic.uuid}")
        } else {
            Log.e("bluetooth1", "Descriptor not found!")
        }
    }


    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun disconnect() {
        gatt?.disconnect()
        gatt?.close()
        gatt = null
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun connect() {
        gatt = bluetoothDevice.connectGatt(context, false, callback)
        Log.d("bluetooth1", "connectGatt")
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun discoverServices() {
        gatt?.discoverServices()
        Log.d("bluetooth1", "discoverServices")
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun writeStringData(data: String){
        val service = gatt?.getService(CTF_SERVICE_UUID)
        val characteristic = service?.getCharacteristic(CUSTOM_CHARACTERISTIC_UUID)
        if (characteristic != null) {
            characteristic.value = data.toByteArray();
            val success = gatt?.writeCharacteristic(characteristic)
            Log.d("bluetooth1", "Write data status: $success")
        }
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun readStringData() {
        val service = gatt?.getService(CTF_SERVICE_UUID)
        val characteristic = service?.getCharacteristic(CUSTOM_CHARACTERISTIC_UUID)
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