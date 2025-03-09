package com.example.blescantest1.remotecontrol.presentation.device_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blescantest1.remotecontrol.domain.repository.BluetoothDevicesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val deviceRepository: BluetoothDevicesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DeviceListViewState())
    val state = _state.asStateFlow()

    fun collectDevices() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            deviceRepository.getFoundedDevicesFlow()
                .onRight { devicesFlow ->
                    devicesFlow.collectLatest { devices ->
                        _state.update { it.copy(devices = devices) }
                    }
                }.onLeft {

                }
        }
    }
}