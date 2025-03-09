package com.example.blescantest1.remotecontrol.domain.model

data class BluetoothError(
    val error: BluetoothErrors,
    val t: Throwable? = null
)

enum class BluetoothErrors(val message: String){
    BluetoothError("Bluetooth Error"),
    UnknownError("Unknown Error")
}