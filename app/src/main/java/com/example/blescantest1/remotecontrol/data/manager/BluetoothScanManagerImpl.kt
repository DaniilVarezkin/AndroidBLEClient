package com.example.blescantest1.remotecontrol.data.manager

import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import com.example.blescantest1.remotecontrol.data.bluetooth.BLEScanner
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothScanManager
import com.example.blescantest1.util.constants.PermissionConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BluetoothScanManagerImpl @Inject constructor(
    private val externalScope: CoroutineScope,
    private val scanner: BLEScanner,
) : BluetoothScanManager {

    private val _foundedDevicesMap = MutableStateFlow(emptyMap<String, BluetoothDevice>())
    private var collectDevicesJob: Job? = null

    override fun getFoundedDevicesFlow(): Flow<List<BluetoothDevice>> {
        return _foundedDevicesMap.map { it.values.toList() }
    }

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_SCAN)
    override fun startScanning() {
        _foundedDevicesMap.value = emptyMap()
        scanner.startScanning()
        collectDevicesJob?.cancel()
        collectDevicesJob = collectDevices()
    }

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_SCAN)
    override fun stopScanning() {
        scanner.stopScanning()
        collectDevicesJob?.cancel()
    }

    override fun getScanningState(): StateFlow<Boolean> {
        return scanner.isScanning
    }

    private fun collectDevices(): Job {
        return externalScope.launch {
            scanner.foundDeviceFlow
                .filter { device -> !_foundedDevicesMap.value.containsKey(device.address) }
                .collect { device ->
                    _foundedDevicesMap.update { it + (device.address to device) }
                }
        }
    }
}