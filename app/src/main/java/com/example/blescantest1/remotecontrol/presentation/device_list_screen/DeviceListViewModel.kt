package com.example.blescantest1.remotecontrol.presentation.device_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.right
import com.example.blescantest1.remotecontrol.domain.repository.BluetoothDevicesRepository
import com.example.blescantest1.remotecontrol.domain.use_case.BluetoothUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val bluetoothUseCases: BluetoothUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(DeviceListViewState())
    val state = _state.asStateFlow()

    init {
        observeState()
    }

    fun startScanning() {
        bluetoothUseCases.startScanning()
    }

    fun stopScanning() {
        bluetoothUseCases.stopScanning()
    }

    private fun observeState() {
        viewModelScope.launch {
            combine(
                bluetoothUseCases.getScanningState(),
                bluetoothUseCases.getDeviceFlow()
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