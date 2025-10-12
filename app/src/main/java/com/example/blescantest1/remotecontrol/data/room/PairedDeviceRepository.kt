package com.example.blescantest1.remotecontrol.data.room

import com.example.blescantest1.remotecontrol.domain.model.room.PairedDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PairedDeviceRepository @Inject constructor (
    private val dao: PairedDeviceDao,
    private val scope: CoroutineScope
) {
    val deviceList = dao.get()

    suspend fun getByAddress(address: String): PairedDevice? {
        return dao.getByAddress(address)
    }

    suspend fun add(device: PairedDevice){
        dao.add(device)
    }

    suspend fun update(device: PairedDevice) {
        dao.update(device)
    }
}