package com.example.blescantest1.remotecontrol.data.repository

import android.bluetooth.BluetoothDevice
import arrow.core.Either
import com.example.blescantest1.remotecontrol.data.bluetooth.BLEScanner
import com.example.blescantest1.remotecontrol.data.mapper.toBluetoohtError
import com.example.blescantest1.remotecontrol.domain.model.BluetoothError
import com.example.blescantest1.remotecontrol.domain.repository.BluetoothDevicesRepository
import kotlinx.coroutines.flow.Flow

class BluetoothDevicesRepositoryImpl constructor(
    val scanner: BLEScanner
) : BluetoothDevicesRepository {

    override suspend fun getFoundDevicesFlow(): Either<BluetoothError, Flow<BluetoothDevice>> {
        return Either.catch {
            scanner.foundDevicesFlow
        }.mapLeft { it.toBluetoohtError() }
    }
}