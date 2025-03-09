package com.example.blescantest1.bletools

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.blescantest1.datastore.DataStoreManager
import com.example.blescantest1.remotecontrol.data.bluetooth.BLEScanner
import com.example.blescantest1.remotecontrol.data.bluetooth.PERMISSION_BLUETOOTH_CONNECT
import com.example.blescantest1.remotecontrol.data.bluetooth.PERMISSION_BLUETOOTH_SCAN
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class BLEAutoConnectHelper @Inject constructor(
    private val context: Context,
    private val deviceManager: BLEDeviceManager,
    private val dataStoreManager: DataStoreManager,
    private val bleScanner: BLEScanner
) {
    private val TAG = "BLEAutoConnectHelper"

    val isScanning = bleScanner.isScanning

    private val _activeDeviceConnection = MutableStateFlow<BLEDeviceConnection?>(null)
    val activeDeviceConnection = _activeDeviceConnection.asStateFlow()

    val isDeviceConnected =
        _activeDeviceConnection.flatMapLatest { it?.isConnected ?: flowOf(false) }

    private var flagAutoConnect = true

    @RequiresPermission(allOf = [PERMISSION_BLUETOOTH_CONNECT, PERMISSION_BLUETOOTH_SCAN])
    suspend fun autoScanAndConnect() {
        Log.v(TAG, "Начало автоматического сканирования")

        val lastDeviceAddress = dataStoreManager.lastDeviceAddress.firstOrNull()

        val lastDevice = withTimeoutOrNull(5000) {
            bleScanner.foundDevicesFlow
                .firstOrNull { device ->
                    device.address == lastDeviceAddress
                }
        }

        lastDevice?.let {
            setActiveAndConnectDevice(it)
            saveAddressLastDevice(it.address)
        } ?: run {
            Log.w(TAG, "Устройство не найдено")
        }

    }

    @RequiresPermission(allOf = [PERMISSION_BLUETOOTH_CONNECT, PERMISSION_BLUETOOTH_SCAN])
    fun setActiveAndConnectDevice(device: BluetoothDevice?) {
        setActiveDevice(device)
        connectActiveDevice()
    }

    @RequiresPermission(allOf = [PERMISSION_BLUETOOTH_CONNECT, PERMISSION_BLUETOOTH_SCAN])
    fun setActiveDevice(device: BluetoothDevice?) {
        with(_activeDeviceConnection) {
            value?.disconnect()
            value = null

            if (device != null) {
                value = BLEDeviceConnection(context, device)
                Log.v(TAG, "Установка активного устройства: ${device.name} : ${device.address}")
            } else {
                Log.v(TAG, "Сброс активного устройства")
            }
        }
    }

    @RequiresPermission(allOf = [PERMISSION_BLUETOOTH_CONNECT, PERMISSION_BLUETOOTH_SCAN])
    fun disconnectActiveDevice() {
        _activeDeviceConnection.value?.disconnect()
        _activeDeviceConnection.value = null
        flagAutoConnect = false
        Log.v(TAG, "Сброс соединения с активым устройством")
    }


    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun connectActiveDevice() {
        _activeDeviceConnection.value?.let { connection ->
            Log.v(TAG, "Соединение с выбранным устройством")
            connection.connect()

        } ?: {
            Log.e(TAG, "Ошибка: нет активного устройства для подключения")
        }
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun discoverServices() {
        _activeDeviceConnection.value?.discoverServices()
    }

    private suspend fun saveAddressLastDevice(address: String) {
        dataStoreManager.saveAddressLastDevice(address)
        Log.v(TAG, "Сохранен адрес последнего подключенного устройства: $address")
    }

}