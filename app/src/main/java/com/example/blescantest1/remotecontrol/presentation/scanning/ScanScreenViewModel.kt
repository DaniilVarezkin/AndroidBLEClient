package com.example.blescantest1.remotecontrol.presentation.scanning

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blescantest1.navigation.Routes
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothScanManager
import com.example.blescantest1.util.constants.BluetoothConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanScreenViewModel @Inject constructor(
    private val scanManager: BluetoothScanManager,

) : ViewModel() {
    private val TAG = "ScanScreenViewModel"
    private val _state = MutableStateFlow(ScanningScreenState())
    val state = _state.asStateFlow()

    private val ignoreDevices = mutableListOf<String>()

    private val _pendingDevice = MutableStateFlow<BluetoothDevice?>(null)
    val pendingDevice = _pendingDevice.asStateFlow()

    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo = _navigateTo.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                scanManager.getScanningState(),
                scanManager.getFoundDevicesFlow()
            ) { isScanning, devices ->
                _state.update {
                    it.copy(
                        isScanning = isScanning,
                    )
                }

                // Берем первое устройство, которое не содержится в списке игнорируемых
                val foundDevice = devices.firstOrNull{ device ->
                    !ignoreDevices.contains(device.address)
                }

                if (
                    _pendingDevice.value == null
                    && foundDevice != null
                    && !ignoreDevices.contains(foundDevice.address)
                ) {
                    scanManager.stopScanning() //Остановка сканирования на время принятия решения
                    _pendingDevice.value = foundDevice
                }
            }.collect()
        }
    }

    fun startScan() {
        scanManager.startScanningByTargetService(BluetoothConstants.CTF_SERVICE_UUID)
    }

    fun stopScan() {
        scanManager.stopScanning()
    }

    fun confirmDevice() {
        _pendingDevice.value?.let { device ->
            Log.d(TAG, "Переход к экрану подключения")
            _navigateTo.update {
                Routes.ConnectDevice.createRoute(device.address)
            }
        } ?: {
            Log.e(TAG, "Ошибка перехода к подключению")
        }
    }

    fun dismissDevice() {
        Log.w(TAG, "Игнор устройства: ${_pendingDevice.value?.address}")

        _pendingDevice.value?.let {
            ignoreDevices.add(it.address)
        }
        _pendingDevice.update { null }

        //Продолжаем сканировать
        scanManager.startScanningByTargetService(BluetoothConstants.CTF_SERVICE_UUID)
    }
}