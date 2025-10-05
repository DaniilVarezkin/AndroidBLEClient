package com.example.blescantest1.remotecontrol.presentation.connect

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blescantest1.navigation.Routes
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothConnectionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectScreenViewModel @Inject constructor(
    private val connectionManager: BluetoothConnectionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val TAG = "ConnectScreenViewModel"

    val address = savedStateHandle.get<String>("address")
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo = _navigateTo.asStateFlow()

    init {
        viewModelScope.launch {
            if (address != null) {
                connectionManager.connectToDevice(address).collect { connection ->
                    connection?.isConnected?.collect {
                        Log.d(TAG, "Соединение: ${it}")
                        if (it == true) {
                            _navigateTo.update { Routes.Device.createRoute(address) }
                        }
                    }
                }
            }
        }
    }
}