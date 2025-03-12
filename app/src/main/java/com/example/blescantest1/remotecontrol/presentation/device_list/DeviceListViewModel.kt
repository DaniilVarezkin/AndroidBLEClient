package com.example.blescantest1.remotecontrol.presentation.device_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val scanManager: BluetoothScanManager
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