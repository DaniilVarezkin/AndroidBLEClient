package com.example.blescantest1.remotecontrol.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.blescantest1.remotecontrol.domain.model.room.PairedDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface PairedDeviceDao {

    @Query("SELECT * FROM paired_devices")
    fun get(): Flow<List<PairedDevice>>

    @Query("SELECT * FROM paired_devices WHERE address = :address")
    suspend fun getByAddress(address: String): PairedDevice?

    @Insert
    suspend fun add(device: PairedDevice)

    @Update
    suspend fun update(device: PairedDevice)
}