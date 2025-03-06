package com.example.blescantest1.viewmodels

import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blescantest1.bletools.BLEDeviceManager
import com.example.blescantest1.bletools.PERMISSION_BLUETOOTH_SCAN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val deviceManager: BLEDeviceManager
) : ViewModel() {

    private val foundedDevices = deviceManager.foundedDevices
    private val isScanningNow = deviceManager.isScanningNow

    fun startScan() {
        deviceManager.startScanning(viewModelScope, 5000)
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_SCAN)
    fun stopScan() {
        deviceManager.stopScanning()
    }

    private val _uiState = MutableStateFlow(DeviceListUIState())
    val uiState = combine(
        _uiState,
        foundedDevices,
        isScanningNow
    ) { uiState, foundDevices, isScanningNow ->
        uiState.copy(
            devices = foundDevices.toList(),
            isScanningNow = isScanningNow
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DeviceListUIState())
}

data class DeviceListUIState(
    val devices: List<BluetoothDevice> = emptyList(),
    val isScanningNow: Boolean = false,
)
