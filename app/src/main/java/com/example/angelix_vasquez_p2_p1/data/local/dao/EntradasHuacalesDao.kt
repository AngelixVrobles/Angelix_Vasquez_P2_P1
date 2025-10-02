package com.example.angelix_vasquez_p2_p1.data.local.dao

import androidx.room.*
import com.example.angelix_vasquez_p2_p1.data.local.entities.EntradasHuacalesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EntradasHuacalesDao {

    @Query("SELECT * FROM EntradasHuacales")
    fun getAll(): Flow<List<EntradasHuacalesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entrada: EntradasHuacalesEntity)

    @Update
    suspend fun update(entrada: EntradasHuacalesEntity)

    @Delete
    suspend fun delete(entrada: EntradasHuacalesEntity)
}
