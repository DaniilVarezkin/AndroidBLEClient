package com.example.blescantest1.remotecontrol.presentation.device_list

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blescantest1.navigation.Routes
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothScanManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val scanManager: BluetoothScanManager,
) : ViewModel() {

    private val TAG = "DeviceListViewModel"
    private val _state = MutableStateFlow(DeviceListViewState())
    val state = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<String>(replay = 0)
    val navigation = _navigation.asSharedFlow()

    init {
        observeState()
    }

    fun startScanning() {
        Log.v(TAG, "Нажата кнопка 'начать сканирование'")
        scanManager.startScanning()
    }

    fun stopScanning() {
        Log.v(TAG, "Нажата кнопка 'остановить сканирование'")
        scanManager.stopScanning()
    }

    //TODO Убрать подключение здесь, перенести его на экран конкретного кстройсва, а здесь передаётся MAC адресс
    fun connectDevice(device: BluetoothDevice){
        Log.v(TAG, "Соединение с устройством ${device.address}")

        viewModelScope.launch {
            _navigation.emit(Routes.ConnectedDevice.createRoute(device.address))
        }
        //connectManager.connectToDevice(device)
    }

    private fun observeState() {
        viewModelScope.launch {
            combine(
                scanManager.getScanningState(),
                scanManager.getFoundDevicesFlow()
            ) { isScanning, devices ->
                _state.update {
                    it.copy(
                        isScanning = isScanning,
                        devices = devices
                    )
                }
            }.catch { e ->
                _state.update { it.copy(error = e.message ?: "Unknown error") }
            }.collect()
        }
    }
}