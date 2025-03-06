package com.example.blescantest1.bletools

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
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

    private val scanner: BluetoothLeScanner
        get() = bluetooth.adapter.bluetoothLeScanner

    @SuppressLint("MissingPermission")
    private val foundDevicesFlow = callbackFlow<BluetoothDevice> {
        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)
                result ?: return

                trySend(result.device)
                Log.v(TAG, "foundDevicesFlow, trySend() - найдено устройство, попытка отправки в поток")
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                super.onBatchScanResults(results)
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                _isScanning.value = false

                Log.e(TAG, "scanCallback: onScanFailed: error $errorCode")
            }
        }

        Log.i(TAG, "Запуск сканирования")
        scanner.startScan(scanCallback)
        _isScanning.value = true

        awaitClose {
            scanner.stopScan(scanCallback)
            _isScanning.value = false
        }
    }
}