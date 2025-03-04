package com.example.blescantest1.bletools

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.blescantest1.datastore.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class BLEAutoConnectHelper (
    private val context: Context,
    private val coroutineScope: CoroutineScope
) {
    private val TAG = "BLEAutoConnectHelper"

    private val bleScanner = BLEScanner(context)
    private val dataStoreManager = DataStoreManager(context)

    val isScanning = bleScanner.isScanning

    val foundDevices = bleScanner.foundDevices

    private val _activeDeviceConnection = MutableStateFlow<BLEDeviceConnection?>(null)
    val activeDeviceConnection = _activeDeviceConnection.asStateFlow()

    val isDeviceConnected = _activeDeviceConnection.flatMapLatest { it?.isConnected ?: flowOf(false) }

    private var flagAutoConnect = true

    @RequiresPermission(allOf = [PERMISSION_BLUETOOTH_CONNECT, PERMISSION_BLUETOOTH_SCAN])
    suspend fun autoScanAndConnect() {
        Log.v(TAG, "Начало автоматического сканирования")
        startScanning()

        val lastDeviceAddress = dataStoreManager.lastDeviceAddress.firstOrNull()

        foundDevices.collect { devices ->
            if(flagAutoConnect){
                val lastDevice = devices.firstOrNull { it.address == lastDeviceAddress }
                if(lastDevice != null){
                    stopScanning()
                    setActiveAndConnectDevice(lastDevice)
                }
            }
        }
    }

    @RequiresPermission(allOf = [PERMISSION_BLUETOOTH_CONNECT, PERMISSION_BLUETOOTH_SCAN])
    fun setActiveAndConnectDevice(device: BluetoothDevice?){
        setActiveDevice(device)
        connectActiveDevice()
    }

    @RequiresPermission(allOf = [PERMISSION_BLUETOOTH_CONNECT, PERMISSION_BLUETOOTH_SCAN])
    fun setActiveDevice(device: BluetoothDevice?){
        with(_activeDeviceConnection){
            value?.disconnect()
            value = null

            if(device != null){
                value = BLEDeviceConnection(context, device)
                Log.v(TAG, "Установка активного устройства: ${device.name} : ${device.address}")
            } else {
                Log.v(TAG, "Сброс активного устройства")
            }
        }
    }

    @RequiresPermission(allOf = [PERMISSION_BLUETOOTH_CONNECT, PERMISSION_BLUETOOTH_SCAN])
    fun disconnectActiveDevice(){
        _activeDeviceConnection.value?.disconnect()
        _activeDeviceConnection.value = null
        flagAutoConnect = false
        Log.v(TAG, "Сброс соединения с активым устройством")
    }


    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun connectActiveDevice(){
        _activeDeviceConnection.value?.let { connection ->
            Log.v(TAG, "Соединение с выбранным устройством")
            connection.connect()
            saveAddressLastDevice(connection.bluetoothDevice.address)
        } ?: {
            Log.e(TAG, "Ошибка: нет активного устройства для подключения")
        }
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun discoverServices(){
        _activeDeviceConnection.value?.discoverServices()
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_SCAN)
    fun stopScanning(){
        bleScanner.stopScanning()
        Log.v(TAG, "Остановка сканирования устройств")
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_SCAN)
    fun startScanning(){
        bleScanner.startScanning()
        Log.v(TAG, "Запуск сканирования устройств")
    }

    fun saveAddressLastDevice(address: String){
        coroutineScope.launch { dataStoreManager.saveAddressLastDevice(address) }
        Log.v(TAG, "Сохранен адрес последнего подключенного устройства: $address")
    }

}