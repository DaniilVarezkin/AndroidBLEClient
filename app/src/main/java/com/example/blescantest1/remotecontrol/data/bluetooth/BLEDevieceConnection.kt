package com.example.blescantest1.remotecontrol.data.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.blescantest1.remotecontrol.domain.model.bluetooth.AbstractBLEDeviceConnection
import com.example.blescantest1.util.constants.BluetoothConstants
import com.example.blescantest1.util.constants.PermissionConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@Suppress("DEPRECATION")

class BLEDeviceConnectionImpl
@RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
constructor(
    private val context: Context,
    val bluetoothDevice: BluetoothDevice
) : AbstractBLEDeviceConnection() {
    private val TAG = "BLEDeviceConnection"
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
                this@BLEDeviceConnectionImpl.enableNotifications() // Включаем подписку на обновления
            } else {
                Log.e(TAG, "Service discovery failed with status: $status")
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
                Log.e(TAG, "Deprecated onCharacteristicRead failed with status: $status")
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            Log.d(TAG, "onCharacteristicWrite: ${characteristic.uuid}")
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

    /**
     * Подключается к удалённому BLE-устройству.
     *
     * @throws SecurityException если отсутствует разрешение BLUETOOTH_CONNECT
     */
    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun connect() {
        gatt = bluetoothDevice.connectGatt(context, false, callback)
        Log.d(TAG, "connectGatt")
    }

    /**
     * Отключается от BLE-устройства и освобождает ресурсы.
     *
     * @throws SecurityException если отсутствует разрешение BLUETOOTH_CONNECT
     */
    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun disconnect() {
        gatt?.disconnect()
        gatt?.close()
        gatt = null
    }

    override fun getDevice(): BluetoothDevice {
        return bluetoothDevice
    }

    /**
     * Запускает процесс обнаружения сервисов на подключённом BLE-устройстве.
     *
     * @throws SecurityException если отсутствует разрешение BLUETOOTH_CONNECT
     */
    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    fun discoverServices() {
        gatt?.discoverServices()
        Log.d(TAG, "discoverServices")
    }

    /**
     * Читает значение характеристики [BluetoothConstants.CUSTOM_CHARACTERISTIC_UUID].
     *
     * @throws SecurityException если отсутствует разрешение BLUETOOTH_CONNECT
     */
    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun readData() {
        val service = gatt?.getService(BluetoothConstants.CTF_SERVICE_UUID)
        val characteristic =
            service?.getCharacteristic(BluetoothConstants.CUSTOM_CHARACTERISTIC_UUID)
        if (characteristic != null) {
            if (characteristic.properties.and(BluetoothGattCharacteristic.PROPERTY_READ) == 0) {
                Log.e(TAG, "Необходимые характеристики не найдены")
                return
            }
            val success = gatt?.readCharacteristic(characteristic)
            Log.d(TAG, "Read data status: $success")
        }
    }

    /**
     * Записывает данные в характеристику [BluetoothConstants.CUSTOM_CHARACTERISTIC_UUID].
     *
     * @param data байтовый массив с данными для записи
     * @throws SecurityException если отсутствует разрешение BLUETOOTH_CONNECT
     */
    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    override fun writeData(data: ByteArray) {
        val service = gatt?.getService(BluetoothConstants.CTF_SERVICE_UUID)
        val characteristic =
            service?.getCharacteristic(BluetoothConstants.CUSTOM_CHARACTERISTIC_UUID)
        if (characteristic != null) {
            characteristic.value = data
            val success = gatt?.writeCharacteristic(characteristic)
            Log.d(TAG, "Write data status: $success")
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun sendMessage(message: String) {
        writeData(message.toByteArray(Charsets.US_ASCII))
    }

    /**
     * Включает уведомления для характеристики [BluetoothConstants.CUSTOM_CHARACTERISTIC_UUID].
     * Это позволяет получать события при изменении значения характеристики на устройстве.
     *
     * @throws SecurityException если отсутствует разрешение BLUETOOTH_CONNECT
     */
    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_CONNECT)
    fun enableNotifications() {
        val service = gatt?.getService(BluetoothConstants.CTF_SERVICE_UUID)
        val characteristic =
            service?.getCharacteristic(BluetoothConstants.CUSTOM_CHARACTERISTIC_UUID)

        if (characteristic == null) {
            Log.e(TAG, "Характеристики устройства не найдены")
            return
        }

        // Проверяем, поддерживает ли характеристика Notifications
        if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY == 0) {
            Log.e(TAG, "Characteristic does not support notifications!")
            return
        }

        gatt?.setCharacteristicNotification(characteristic, true)

        val descriptor = characteristic.getDescriptor(BluetoothConstants.CCCD_UUID)
        if (descriptor != null) {
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt?.writeDescriptor(descriptor)
        } else {
            Log.e(TAG, "Descriptor not found!")
        }
    }
}