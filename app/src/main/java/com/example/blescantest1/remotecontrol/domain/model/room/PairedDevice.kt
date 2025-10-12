package com.example.blescantest1.remotecontrol.domain.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "paired_devices")
data class PairedDevice(
    @PrimaryKey()
    @ColumnInfo(name = "address")
    var address: String,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "last_connection_date")
    var lastConnectionDate: Date? = null
)