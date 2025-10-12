package com.example.blescantest1.remotecontrol.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.blescantest1.remotecontrol.domain.model.room.PairedDevice

@Database(entities = [PairedDevice::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RemoteControlDatabase : RoomDatabase() {
    abstract fun pairedDeviceDao(): PairedDeviceDao

    companion object {
        @Volatile
        private var Instance: RemoteControlDatabase? = null

        fun getDatabase(context: Context): RemoteControlDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = RemoteControlDatabase::class.java,
                    name = "remote_control_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}