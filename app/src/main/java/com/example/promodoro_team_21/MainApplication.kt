package com.example.promodoro_team_21

import android.app.Application
import androidx.room.Room
import com.example.promodoro_team_21.model.TodoDatabase

class MainApplication : Application() {
    companion object {
        lateinit var todoDatabase: TodoDatabase
    }

    override fun onCreate() {
        super.onCreate()
        todoDatabase = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            TodoDatabase.NAME
        )
            .addMigrations(TodoDatabase.MIGRATION_1_2) // Füge die Migration hinzu
            .build()
    }
}