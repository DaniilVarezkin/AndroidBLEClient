package com.example.blescantest1.remotecontrol.presentation.device

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothConnectionManager
import com.example.blescantest1.remotecontrol.domain.model.bluetooth.AbstractBLEDeviceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("MissingPermission")
@HiltViewModel
class DeviceScreenViewModel @Inject constructor(
    private val connectionManager: BluetoothConnectionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val TAG = "DeviceScreenViewModel"

    private val connectionFlow = connectionManager.getDeviceConnection()
    private var _connection: AbstractBLEDeviceConnection? = null

    private val _state = MutableStateFlow(DeviceScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // Подписка на connection
            viewModelScope.launch {
                connectionFlow.collect { connection ->
                    _connection = connection
                    val device = connection?.getDevice()
                    _state.update {
                        it.copy(
                            name = device?.name,
                            address = device?.address
                        )
                    }
                }
            }

            // Подписка на isConnected (через flatMapLatest)
            viewModelScope.launch {
                connectionFlow
                    .flatMapLatest { connection ->
                        connection?.isConnected ?: flowOf(false)
                    }
                    .collect { isConnected ->
                        _state.update { it.copy(isConnected = isConnected) }
                    }
            }

            // Подписка на сообщения //TODO Сделать отпраку команды с колбэком
            viewModelScope.launch {
                connectionFlow.collect { connection ->
                    connection?.characteristicData?.collect { data ->
                        data?.let {
                            val response = it.decodeToString()
                            _state.update { st ->
                                st.copy(messages = st.messages + MessageUiModel(response, true))
                            }
                        }
                    }
                }
            }
        }
    }

    fun onMessageValueChange(newText: String) {
        _state.update { it.copy(messageText = newText) }
    }

    fun onClickSendMessage(message: String) {
        _connection?.let { connection ->
            Log.d(
                TAG,
                "Отправка сообщения: \"$message\" на устройство ${connection.getDevice().address}"
            )
            _state.update {
                it.copy(
                    messages = it.messages + MessageUiModel(message),
                    messageText = ""
                )
            }

            connection.sendMessage(message)
        } ?: {
            Log.w(TAG, "Нет подключения к устройству")
        }
    }
}