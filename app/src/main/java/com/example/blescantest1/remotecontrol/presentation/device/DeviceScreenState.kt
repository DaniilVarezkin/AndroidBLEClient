package com.example.blescantest1.remotecontrol.presentation.device

data class DeviceScreenState(
    val isConnected: Boolean = false,
    val name: String? = null,
    val address: String? = null,
    val messages: List<MessageUiModel> = emptyList(),
    val messageText: String = ""
)