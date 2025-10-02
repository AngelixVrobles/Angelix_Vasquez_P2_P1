package com.example.angelix_vasquez_p2_p1.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.angelix_vasquez_p2_p1.data.local.dao.EntradasDao
import com.example.angelix_vasquez_p2_p1.data.local.entities.EntradasHuacalesEntity

@Database(
    entities = [EntradasHuacalesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entradasDao(): EntradasDao
}
