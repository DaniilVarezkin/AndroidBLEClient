package com.example.blescantest1.bletools

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

const val PERMISSION_BLUETOOTH_SCAN = "android.permission.BLUETOOTH_SCAN"
const val PERMISSION_BLUETOOTH_CONNECT = "android.permission.BLUETOOTH_CONNECT"

class BLEScanner(context: Context) {

    private val TAG = "BLEScanner"

    private val bluetooth = context.getSystemService(Context.BLUETOOTH_SERVICE)
            as? BluetoothManager
        ?: throw Exception("Bluetooth is not supported by this device")

    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.asStateFlow()

    private val _foundDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val foundDevices = _foundDevices.asStateFlow()

    private val scanner: BluetoothLeScanner
        get() = bluetooth.adapter.bluetoothLeScanner

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result ?: return

            if (!_foundDevices.value.contains(result.device)) {
                _foundDevices.update { it + result.device }
                Log.v(TAG, "scanCallback: onScanResult: found new device: ${result.device.address}")
            }
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

    @RequiresPermission(PERMISSION_BLUETOOTH_SCAN)
    fun startScanning() {
        scanner.startScan(scanCallback)
        _isScanning.value = true

        Log.v(TAG, "startScanning")
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_SCAN)
    fun stopScanning() {
        scanner.stopScan(scanCallback)
        _isScanning.value = false

        Log.v(TAG, "stopScanning")
    }
}