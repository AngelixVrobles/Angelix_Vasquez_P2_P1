package com.example.angelix_vasquez_p2_p1.data.local.dao

import androidx.room.*
import com.example.angelix_vasquez_p2_p1.data.local.entities.EntradasHuacalesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EntradasDao {
    @Query("SELECT * FROM EntradasHuacales ORDER BY idEntrada DESC")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<EntradasHuacalesEntity>>

    @Insert
    suspend fun insert(entrada: EntradasHuacalesEntity)

    @Update
    suspend fun update(entrada: EntradasHuacalesEntity)

    @Delete
    suspend fun delete(entrada: EntradasHuacalesEntity)
}
