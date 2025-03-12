package com.example.blescantest1.remotecontrol.presentation.device_list

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothConnectionManager
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothScanManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val connectManager: BluetoothConnectionManager
) : ViewModel() {

    private val _state = MutableStateFlow(DeviceListViewState())
    val state = _state.asStateFlow()

    init {
        observeState()
    }

    fun startScanning() {
        scanManager.startScanning()
    }

    fun stopScanning() {
        scanManager.stopScanning()
    }

    fun connectDevice(device: BluetoothDevice){

        connectManager.connectToDevice(device)
    }

    private fun observeState() {
        viewModelScope.launch {
            combine(
                scanManager.getScanningState(),
                scanManager.getFoundedDevicesFlow()
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