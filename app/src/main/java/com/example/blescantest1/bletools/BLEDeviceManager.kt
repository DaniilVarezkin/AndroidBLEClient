package com.example.blescantest1.bletools

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import okio.Timeout
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BLEDeviceManager @Inject constructor(
    private val bleScanner: BLEScanner
) {
    private val TAG = "BLEDeviceManager"

    private val _foundedDevices = MutableStateFlow<Set<BluetoothDevice>>(emptySet())
    val foundedDevices = _foundedDevices.asStateFlow()
    val isScanningNow = bleScanner.isScanning

    private var isScanningStarted = false
    private var scanJob: Job? = null

    fun startScanning(coroutineScope: CoroutineScope, timeout: Long = 5000) {
        if (!isScanningNow.value) {
            scanJob = coroutineScope.launch(Dispatchers.IO) {
                Log.v(TAG, "Запущено сканирование c таймаутом: $timeout ms")
                withTimeout(timeout) {
                    bleScanner.foundDevicesFlow.collect { device ->
                        _foundedDevices.update { it + device }
                    }
                }
            }
        } else {
            Log.w(TAG, "Сканирование уже запущено")
        }
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_SCAN)
    fun stopScanning() {
        scanJob?.cancel()
        bleScanner.stopScan()
    }
}