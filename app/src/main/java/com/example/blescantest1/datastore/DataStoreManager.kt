package com.example.blescantest1.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {
    companion object {
        private val LAST_DEVICE_KEY = stringPreferencesKey("last_device_key")
    }

    suspend fun saveAddressLastDevice(address: String){
        context.dataStore.edit { settings ->
            settings[LAST_DEVICE_KEY] = address
        }
    }

    val lastDeviceAddress: Flow<String?> =  context.dataStore.data
        .map { settings ->
            settings[LAST_DEVICE_KEY]
        }
}