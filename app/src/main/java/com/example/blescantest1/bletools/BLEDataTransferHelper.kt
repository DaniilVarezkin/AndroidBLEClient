package com.example.blescantest1.bletools

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalCoroutinesApi::class)
class BLEDataTransferHelper(
    private val activeDeviceConnection: StateFlow<BLEDeviceConnection?>,
    private val coroutineScope: CoroutineScope
) {
    private val TAG = "BLEDataTransferHelper"

    private val deviceServices =
        activeDeviceConnection.flatMapLatest { it?.services ?: flowOf(emptyList()) }

    val isTargetServiceFound = deviceServices.map { services ->
//        val formattedServices = services.joinToString(separator = "\n") { service ->
//            val characteristics =
//                service.characteristics.joinToString(separator = "\n\t") { it.uuid.toString() }
//            "${service.uuid}\n\t$characteristics"
//        }
//        Log.v(TAG, "Device Services:\n${formattedServices.ifEmpty { "null" }}")

        services.any { it.uuid == CTF_SERVICE_UUID }
    }

    val deviceData =
        activeDeviceConnection.flatMapLatest { it?.customCharacteristicData ?: flowOf(null) }


    init {
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun writeStringData(data: String) {
        activeDeviceConnection.value?.let {
            it.writeStringData(data)
            Log.v(TAG, "Запись данных в устройство: $data")
        } ?: Log.e(TAG, "Ошибка записи: нет соединения с устройством")
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    fun readStringData() {
        activeDeviceConnection.value?.let {
            it.readStringData()
            Log.v(TAG, "Чтение данных с устройства")
        } ?: Log.e(TAG, "Ошибка чтения: нет соединения с устройством")
    }
}