package com.thiagoneves.architecturecomponents.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room

import androidx.room.RoomDatabase
import com.thiagoneves.architecturecomponents.data.model.ToDoData

@Database(entities = [ToDoData::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun toDoDao() : ToDoDao


    companion object {

        @Volatile
        private var INSTANCE: ToDoDatabase? = null

        fun getDatabase(context: Context) : ToDoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    name = "todo_database").build()
                    INSTANCE = instance
                    return instance
            }
        }
    }

}