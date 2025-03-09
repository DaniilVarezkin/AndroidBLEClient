package com.example.blescantest1.remotecontrol.data.mapper

import androidx.bluetooth.BluetoothException
import com.example.blescantest1.remotecontrol.domain.model.BluetoothError
import com.example.blescantest1.remotecontrol.domain.model.BluetoothErrors
import okio.IOException


fun Throwable.toBluetoohtError(): BluetoothError {
    val error =  when(this){
        is BluetoothException -> BluetoothErrors.BluetoothError
        else -> BluetoothErrors.UnknownError
    }

    return  BluetoothError(
        error = error,
        t = this
    )
}