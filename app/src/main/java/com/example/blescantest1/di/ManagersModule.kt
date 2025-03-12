package com.example.blescantest1.di

import com.example.blescantest1.remotecontrol.data.manager.BluetoothScanManagerImpl
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothScanManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class ManagersModule {
    @Binds
    @Singleton
    abstract fun bindBluetoothScanManager(impl: BluetoothScanManagerImpl) : BluetoothScanManager

}