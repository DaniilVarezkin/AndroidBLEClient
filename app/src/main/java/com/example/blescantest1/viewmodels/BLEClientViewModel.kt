package com.example.blescantest1.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.blescantest1.bletools.BLEDeviceConnection
import com.example.blescantest1.bletools.BLEScanner
import com.example.blescantest1.bletools.PERMISSION_BLUETOOTH_CONNECT
import com.example.blescantest1.bletools.PERMISSION_BLUETOOTH_SCAN
import com.example.blescantest1.datastore.DataStoreManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalCoroutinesApi::class)
class BLEClientViewModel(private val application: Application) : AndroidViewModel(application) {
    private val bleScanner = BLEScanner(application)
    val dataStoreManager = DataStoreManager(application)

    public val commandString = mutableStateOf<String>("");

    private val _uiState = MutableStateFlow(BLEClientUIState())

    private val foundDevices = bleScanner.foundDevices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var activeConnection = MutableStateFlow<BLEDeviceConnection?>(null)

    private val isDeviceConnected =
        activeConnection.flatMapLatest { it?.isConnected ?: flowOf(false) }
    private val activeDeviceServices = activeConnection.flatMapLatest {
        it?.services ?: flowOf(emptyList())
    }
    private val activeDeviceData = activeConnection.flatMapLatest {
        it?.customCharacteristicData ?: flowOf(null)
    }
    private val activeDeviceNameWrittenTimes = activeConnection.flatMapLatest {
        it?.successfulWritesCount ?: flowOf(0)
    }


    val uiState = combine(
        _uiState,
        isDeviceConnected,
        activeDeviceServices,
        activeDeviceData,
        activeDeviceNameWrittenTimes
    ) { state, isDeviceConnected, services, data, nameWrittenTimes ->
        state.copy(
            isDeviceConnected = isDeviceConnected,
            discoveredCharacteristics = services.associate { service ->
                Pair(
                    service.uuid.toString(),
                    service.characteristics.map { it.uuid.toString() })
            },
            data = data,
            nameWrittenTimes = nameWrittenTimes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BLEClientUIState())

    init {
        viewModelScope.launch {
            Log.v("BleViewModel", "Автоматический запуск сканирования")
            startScanning()

            foundDevices.collect { devices ->
                _uiState.update { it.copy(foundDevices = devices) }
                val lastDeviceAddress = dataStoreManager.lastDeviceAddress.firstOrNull()
                val lastDevice = devices.firstOrNull { it.address == lastDeviceAddress }
                if (lastDevice != null) {
                    stopScanning()
                    setActiveDevice(lastDevice)
                    Log.v("BleViewModel", "Автоматически подключаемся к ${lastDevice.name}")
                    connectActiveDevice()
                }
            }
        }

        viewModelScope.launch {
            bleScanner.isScanning.collect { isScanning ->
                _uiState.update { it.copy(isScanning = isScanning) }
            }
        }

        viewModelScope.launch {
            isDeviceConnected.collect { isConnected ->
                if (isConnected) {
                    Log.v("BleViewModel", "Устройство подключено, начинаем поиск сервисов автоматически")
                    discoverActiveDeviceServices()
                }
            }
        }
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_SCAN)
    fun startScanning() {
        bleScanner.startScanning()
        Log.v("BleViewModel", "Запуск сканирования")
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_SCAN)
    fun stopScanning() {
        bleScanner.stopScanning()
        Log.v("BleViewModel", "Остановка сканирования")
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = [PERMISSION_BLUETOOTH_CONNECT, PERMISSION_BLUETOOTH_SCAN])
    fun setActiveDevice(device: BluetoothDevice?) {
        activeConnection.value?.disconnect() // Отключаем предыдущее устройство
        activeConnection.value = null // Обнуляем соединение, если device == null

        if (device != null) {
            activeConnection.value = BLEDeviceConnection(application, device)
            _uiState.update { it.copy(activeDevice = device) }
            Log.v("BleViewModel", "Установка активного устройства: ${device.address}")
        } else {
            _uiState.update { it.copy(activeDevice = null) }
            Log.v("BleViewModel", "Активное устройство сброшено")
        }
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun connectActiveDevice() {
        activeConnection.value?.let { connection ->
            connection.connect()
            val device = connection.bluetoothDevice
            viewModelScope.launch {
                dataStoreManager.saveAddressLastDevice(device.address)
                Log.v("BleViewModel", "Сохранен MAC-адрес устройства: ${device.address}")
            }
        } ?: Log.e("BleViewModel", "Ошибка: Нет активного устройства для подключения")

    }

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun disconnectActiveDevice() {
        activeConnection.value?.disconnect()
        Log.v("BleViewModel", "Отсоединение от активного устройства")
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun discoverActiveDeviceServices() {
        activeConnection.value?.discoverServices()
        Log.v("BleViewModel", "Обнаружение сервисов активного устройства")
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun readDataFromActiveDevice() {
        activeConnection.value?.readStringData()
        Log.v("BleViewModel", "Чтение данных с активного устройства")
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun writeDataFromActiveDevice() {
        activeConnection.value?.writeStringData(commandString.value);
        Log.v("BleViewModel", "Запись данных на активное устройство")

    }

    override fun onCleared() {
        super.onCleared()

        //when the ViewModel dies, shut down the BLE client with it
        if (bleScanner.isScanning.value) {
            if (ActivityCompat.checkSelfPermission(
                    getApplication(),
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bleScanner.stopScanning()
            }
        }
    }
}

data class BLEClientUIState(
    val isScanning: Boolean = false,
    val foundDevices: List<BluetoothDevice> = emptyList(),
    val activeDevice: BluetoothDevice? = null,
    val isDeviceConnected: Boolean = false,
    val discoveredCharacteristics: Map<String, List<String>> = emptyMap(),
    val data: String? = null,
    val nameWrittenTimes: Int = 0
)