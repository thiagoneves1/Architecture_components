package com.thiagoneves.architecturecomponents.data.repository

import androidx.lifecycle.LiveData

import com.thiagoneves.architecturecomponents.data.ToDoDao
import com.thiagoneves.architecturecomponents.data.model.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAll()
    val sortByHighPriority: LiveData<List<ToDoData>> = toDoDao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<ToDoData>> = toDoDao.sortByLowPriority()

    suspend fun insertData(toDoData: ToDoData){
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData){
        toDoDao.updatedData(toDoData)
    }

    suspend fun deleteData(toDoData: ToDoData){
        toDoDao.deleteData(toDoData)
    }

    suspend fun deleteAll(){
        toDoDao.deleteAll()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>> {
        return toDoDao.searchDatabase(searchQuery)
    }

}