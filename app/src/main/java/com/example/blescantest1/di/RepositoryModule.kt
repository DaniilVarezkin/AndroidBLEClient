package com.example.blescantest1.di

import com.example.blescantest1.remotecontrol.data.repository.BluetoothDevicesRepositoryImpl
import com.example.blescantest1.remotecontrol.domain.repository.BluetoothDevicesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindBluetoothDevicesRepository(impl: BluetoothDevicesRepositoryImpl) : BluetoothDevicesRepository

}