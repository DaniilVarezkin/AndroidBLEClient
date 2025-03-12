package com.example.blescantest1.remotecontrol.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bluetooth_settings")

@Singleton
class DataStoreManager @Inject constructor(
    private val context: Context
) {
    companion object {
        private val LAST_DEVICE_KEY = stringPreferencesKey("last_device_key")
    }

    val lastDeviceAddress: Flow<String?>
        get() = context.dataStore.data
            .map { settings -> settings[LAST_DEVICE_KEY] }

    suspend fun saveLastDeviceAddress(address: String) = withContext(Dispatchers.IO) {
        context.dataStore.edit { settings ->
            settings[LAST_DEVICE_KEY] = address
        }
    }

    suspend fun deleteLastDeviceAddress()  = withContext(Dispatchers.IO) {
        context.dataStore.edit { settings ->
            settings.remove(LAST_DEVICE_KEY)
        }
    }
}