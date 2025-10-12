package com.example.blescantest1.di

import android.content.Context
import com.example.blescantest1.remotecontrol.data.manager.BluetoothCommunicationManagerImpl
import com.example.blescantest1.remotecontrol.data.manager.BluetoothConnectionManagerImpl
import com.example.blescantest1.remotecontrol.data.manager.BluetoothScanManagerImpl
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothCommunicationManager
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothConnectionManager
import com.example.blescantest1.remotecontrol.domain.manager.BluetoothScanManager
import com.example.blescantest1.remotecontrol.presentation.util.permissions.PermissionManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class ManagersModule {
    @Binds
    @Singleton
    abstract fun bindBluetoothScanManager(impl: BluetoothScanManagerImpl): BluetoothScanManager

    @Binds
    @Singleton
    abstract fun bindBluetoothConnectManager(impl: BluetoothConnectionManagerImpl): BluetoothConnectionManager

    @Binds
    @Singleton
    abstract fun bindBluetoothCommunicationManager(impl: BluetoothCommunicationManagerImpl): BluetoothCommunicationManager
}