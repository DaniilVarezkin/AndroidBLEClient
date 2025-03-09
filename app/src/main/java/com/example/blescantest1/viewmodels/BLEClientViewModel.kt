package com.example.blescantest1.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.blescantest1.bletools.BLEDataTransferHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//@SuppressLint("MissingPermission")
//@HiltViewModel
//class BLEClientViewModel(private val application: Application) : AndroidViewModel(application) {
//    private val TAG = "BLEClientViewModel"
//
//    val commandString = mutableStateOf("")
//
//    private val bleAutoConnectHelper = BLEAutoConnectHelper(application, viewModelScope)
//    private val bleDataTransferHelper =
//        BLEDataTransferHelper(bleAutoConnectHelper.activeDeviceConnection, viewModelScope)
//
//    private val _uiState = MutableStateFlow(BLEClientUIState())
//
//    private val isTargetServiceFound = bleDataTransferHelper.isTargetServiceFound.stateIn(
//        viewModelScope,
//        SharingStarted.WhileSubscribed(5000),
//        false
//    )
//
//
//    val uiState = combine(
//        _uiState,
//        bleAutoConnectHelper.isScanning,
//        bleAutoConnectHelper.foundDevices,
//        bleAutoConnectHelper.activeDeviceConnection,
//        bleDataTransferHelper.deviceData
//    ) { uiState, isScanning, foundDevices, activeDeviceConnection, deviceData ->
//        uiState.copy(
//            isScanning = isScanning,
//            foundDevices = foundDevices,
//            activeDevice = activeDeviceConnection?.bluetoothDevice,
//            data = deviceData
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BLEClientUIState())
//
//    init {
//        viewModelScope.launch {
//            bleAutoConnectHelper.autoScanAndConnect()
//        }
//
//        viewModelScope.launch {
//            isTargetServiceFound.collectLatest { isTarget ->
//                _uiState.update { it.copy(isTargetServiceFound = isTarget) }
//            }
//        }
//
//        viewModelScope.launch {
//            bleAutoConnectHelper.isDeviceConnected.collectLatest { isConnected ->
//                _uiState.update { it.copy(isDeviceConnected = isConnected) }
//            }
//        }
//    }
//
//    fun startScanning() {
//        bleAutoConnectHelper.startScanning()
//    }
//
//    fun stopScanning() {
//        bleAutoConnectHelper.stopScanning()
//    }
//
//    fun setActiveDevice(device: BluetoothDevice) {
//        bleAutoConnectHelper.setActiveDevice(device)
//    }
//
//    fun connectActiveDevice() {
//        bleAutoConnectHelper.connectActiveDevice()
//    }
//
//    fun setActiveAndConnectDevice(device: BluetoothDevice){
//        bleAutoConnectHelper.setActiveAndConnectDevice(device)
//    }
//
//    fun discoverServices(){
//        bleAutoConnectHelper.discoverServices()
//    }
//
//    fun disconnectActiveDevice() {
//        bleAutoConnectHelper.disconnectActiveDevice()
//    }
//
//    fun readDataFromActiveDevice() {
//        bleDataTransferHelper.readStringData()
//    }
//
//    fun writeDataToActiveDevice() {
//        bleDataTransferHelper.writeStringData(commandString.value)
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//
//        if (bleAutoConnectHelper.isScanning.value) {
//            if (ActivityCompat.checkSelfPermission(
//                    getApplication(),
//                    Manifest.permission.BLUETOOTH_SCAN
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                bleAutoConnectHelper.stopScanning()
//            }
//        }
//        Log.d(TAG, "ViewModel очищена")
//    }
//}
//
//data class BLEClientUIState(
//    val isScanning: Boolean = false,
//    val foundDevices: List<BluetoothDevice> = emptyList(),
//    val activeDevice: BluetoothDevice? = null,
//    val isDeviceConnected: Boolean = false,
//    val isTargetServiceFound: Boolean = false,
//    val data: String? = null,
//)