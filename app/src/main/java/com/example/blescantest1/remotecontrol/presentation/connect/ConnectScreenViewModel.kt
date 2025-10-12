package com.example.blescantest1.remotecontrol.presentation.connect

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blescantest1.navigation.Routes
import com.example.blescantest1.remotecontrol.data.room.PairedDeviceRepository
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothConnectionManager
import com.example.blescantest1.remotecontrol.domain.model.room.PairedDevice
import com.example.blescantest1.remotecontrol.presentation.util.permissions.PermissionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@SuppressLint("MissingPermission")
@HiltViewModel
class ConnectScreenViewModel @Inject constructor(
    private val connectionManager: BluetoothConnectionManager,
    private val pairedDeviceRepository: PairedDeviceRepository,
    private val permissionManager: PermissionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val TAG = "ConnectScreenViewModel"

    val address = savedStateHandle.get<String>("address")
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo = _navigateTo.asStateFlow()

    init {
        viewModelScope.launch {
            if (permissionManager.haveAllPermissions()) {
                if (address != null) {
                    connectionManager.connectToDevice(address).collect { connection ->
                        connection?.isConnected?.collect {
                            Log.d(TAG, "Соединение: ${it}")
                            if (it == true) {
                                onSuccessConnect(connection.getDevice())
                            }
                        }
                    }
                }
            } else {
                Log.e(TAG, "Разрешения не выданы")
            }
        }
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private suspend fun onSuccessConnect(device: BluetoothDevice) {

        var pairedDevice = pairedDeviceRepository.getByAddress(device.address)

        if (pairedDevice == null) {
            pairedDeviceRepository.add(
                PairedDevice(
                    name = device.name,
                    address = device.address,
                    lastConnectionDate = Date()
                )
            )
        } else {
            Log.d(TAG, "Устройство уже подключалось, обновление даты подключения")
            pairedDevice.lastConnectionDate = Date()
            pairedDeviceRepository.update(pairedDevice)
        }

        _navigateTo.update { Routes.Device.createRoute(device.address) }
    }
}