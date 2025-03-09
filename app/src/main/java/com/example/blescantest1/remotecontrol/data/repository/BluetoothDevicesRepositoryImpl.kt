package com.example.blescantest1.remotecontrol.data.repository

import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import arrow.core.Either
import com.example.blescantest1.remotecontrol.data.bluetooth.BLEScanner
import com.example.blescantest1.remotecontrol.data.bluetooth.PERMISSION_BLUETOOTH_CONNECT
import com.example.blescantest1.remotecontrol.data.bluetooth.PERMISSION_BLUETOOTH_SCAN
import com.example.blescantest1.remotecontrol.data.mapper.toBluetoohtError
import com.example.blescantest1.remotecontrol.domain.model.BluetoothError
import com.example.blescantest1.remotecontrol.domain.repository.BluetoothDevicesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BluetoothDevicesRepositoryImpl @Inject constructor(
    private val scanner: BLEScanner
) : BluetoothDevicesRepository {

    private val _foundedDevicesMap = MutableStateFlow(emptyMap<String, BluetoothDevice>())
    private var collectDevicesJob: Job? = null

    override fun getFoundedDevicesFlow(): Either<BluetoothError, Flow<List<BluetoothDevice>>> {
        return Either.catch {
            _foundedDevicesMap.map { it.values.toList() }
        }
            .mapLeft { it.toBluetoohtError() }
    }



    @RequiresPermission(PERMISSION_BLUETOOTH_SCAN)
    override suspend fun startScanning() {
        scanner.startScanning()
        collectDevices()
    }

    @RequiresPermission(PERMISSION_BLUETOOTH_SCAN)
    override suspend fun stopScanning() {
        scanner.stopScanning()
    }


    private suspend fun collectDevices(){
        scanner.foundDeviceFlow
            .filter { device -> !_foundedDevicesMap.value.containsKey(device.address) }
            .collect { device ->
                _foundedDevicesMap.update { it + (device.address to device) }
            }
    }
}