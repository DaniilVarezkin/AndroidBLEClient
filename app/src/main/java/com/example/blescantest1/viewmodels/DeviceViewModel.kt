package com.example.blescantest1.viewmodels

import android.bluetooth.BluetoothDevice
import android.companion.virtual.VirtualDeviceManager
import androidx.annotation.RequiresPermission
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blescantest1.bletools.BLEAutoConnectHelper
import com.example.blescantest1.bletools.BLEDeviceManager
import com.example.blescantest1.bletools.PERMISSION_BLUETOOTH_CONNECT
import com.example.blescantest1.bletools.PERMISSION_BLUETOOTH_SCAN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val connectHelper: BLEAutoConnectHelper,
    private val deviceManager: BLEDeviceManager,
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    private var activeDevice: BluetoothDevice? = null
    val isConnected = connectHelper.isDeviceConnected.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        val activeAddress = savedStateHandle.get<String>("active_device_address")
        activeDevice = deviceManager.foundedDevices.value.firstOrNull { device ->
            device.address == activeAddress
        }
    }

    @RequiresPermission(allOf = [PERMISSION_BLUETOOTH_CONNECT, PERMISSION_BLUETOOTH_SCAN])
    fun connectDevice(){
        connectHelper.setActiveDevice(activeDevice)
    }

}