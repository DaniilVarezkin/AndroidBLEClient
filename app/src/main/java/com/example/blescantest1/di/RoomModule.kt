package com.example.blescantest1.di

import android.content.Context
import com.example.blescantest1.remotecontrol.data.room.PairedDeviceRepository
import com.example.blescantest1.remotecontrol.data.room.RemoteControlDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope


@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Provides
    fun providePairedDeviceRepository(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): PairedDeviceRepository {
        val database = RemoteControlDatabase.getDatabase(context)
        return PairedDeviceRepository(database.pairedDeviceDao(), scope)
    }
}