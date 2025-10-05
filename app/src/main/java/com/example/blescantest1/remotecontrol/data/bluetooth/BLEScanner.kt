package com.example.blescantest1.remotecontrol.data.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject


class BLEScanner @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter
) {

    private val TAG = "BLEScanner"

    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.asStateFlow()

    private val _foundDeviceChannel = MutableSharedFlow<BluetoothDevice>(replay = 1)
    val foundDeviceFlow = _foundDeviceChannel.asSharedFlow()

    private val scanner: BluetoothLeScanner
        get() = bluetoothAdapter.bluetoothLeScanner

    private val scanCallback = object : ScanCallback() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result ?: return

            _foundDeviceChannel.tryEmit(result.device)
            Log.i(TAG, "Найдено устройство: ${result.device.name},  ${result.device.address}")

        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            _isScanning.value = false

            Log.v(TAG, "scanCallback: onScanFailed")
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startScanning(serviceUuid: UUID? = null) {
        val filters = if (serviceUuid == null)
            emptyList() else {
            listOf(
                android.bluetooth.le.ScanFilter.Builder()
                    .setServiceUuid(android.os.ParcelUuid(serviceUuid))
                    .build()
            )
        }

        val settings = android.bluetooth.le.ScanSettings.Builder()
            .setScanMode(android.bluetooth.le.ScanSettings.SCAN_MODE_BALANCED)
            .build()

        scanner.startScan(filters, settings, scanCallback)
        _isScanning.value = true

        Log.i(TAG, "startScanning (filter: $serviceUuid)")
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun stopScanning() {
        scanner.stopScan(scanCallback)
        _isScanning.value = false

        Log.i(TAG, "stopScanning")
    }
}