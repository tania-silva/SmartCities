package com.example.smartcities.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.smartcities.ENTITIES.Notas


@Dao
interface NotasDao {

    @Query("SELECT * from notas_table ORDER BY id ASC")
    fun getAllCities(): LiveData<List<Notas>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notas: Notas)

    @Query("DELETE FROM notas_table")
    suspend fun deleteAll()

}