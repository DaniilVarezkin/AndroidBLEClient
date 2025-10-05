package com.example.blescantest1.remotecontrol.data.manager

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.blescantest1.remotecontrol.data.bluetooth.BLEScanner
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothScanManager
import com.example.blescantest1.util.constants.PermissionConstants
import com.example.blescantest1.util.coroutines.RestartableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BluetoothScanManagerImpl @Inject constructor(
    externalScope: CoroutineScope,
    private val scanner: BLEScanner,
) : BluetoothScanManager {
    private val TAG = "BluetoothScanManager"
    private val _foundDevicesMap = MutableStateFlow(emptyMap<String, BluetoothDevice>())
    private var collectDevicesJob: RestartableJob = RestartableJob(externalScope)

    override fun getFoundDevicesFlow(): Flow<List<BluetoothDevice>> {
        return _foundDevicesMap.map { it.values.toList() }
    }

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_SCAN)
    override fun startScanning() {
        Log.d(TAG, "Начато сканирование")

        _foundDevicesMap.value = emptyMap()
        scanner.startScanning()
        collectDevicesJob.restart {
            collectDevices()
        }
    }

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_SCAN)
    override fun startScanningByTargetService(serviceUUID: UUID) {
        Log.d(TAG, "Начато сканирование по сервису: $serviceUUID")

        _foundDevicesMap.value = emptyMap()
        scanner.startScanning(serviceUUID)
        collectDevicesJob.restart {
            collectDevices()
        }
    }

    @RequiresPermission(PermissionConstants.PERMISSION_BLUETOOTH_SCAN)
    override fun stopScanning() {
        Log.d(TAG, "Сканирование завершено")

        scanner.stopScanning()
        collectDevicesJob.cancel()
    }

    override fun getScanningState(): StateFlow<Boolean> {
        return scanner.isScanning
    }

    private suspend fun collectDevices() {
        scanner.foundDeviceFlow
            .filter { device -> !_foundDevicesMap.value.containsKey(device.address) }
            .collect { device ->
                Log.d(TAG, "collectDevices - найдено устройство ${device.address}")
                _foundDevicesMap.update { it + (device.address to device) }
            }
    }
}