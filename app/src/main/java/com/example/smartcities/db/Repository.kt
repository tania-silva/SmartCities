package com.example.smartcities.db

import androidx.lifecycle.LiveData
import com.example.smartcities.ENTITIES.Notas
import com.example.smartcities.dao.NotasDao

class Repository(private val notasDao: NotasDao) {
    //Repositorio
        val allNotes: LiveData<List<Notas>> = notasDao.getAllNotes()

    suspend fun insert(notas: Notas){
        notasDao.insert(notas)
    }

    suspend fun update(notas: Notas){
        notasDao.update(notas)
    }

    suspend fun delete(id: Int?){
        notasDao.delete(id)
    }
}