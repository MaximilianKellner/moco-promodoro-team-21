package com.example.promodoro_team_21

import android.app.Application
import androidx.room.Room
import com.example.promodoro_team_21.model.TodoDatabase

class MainApplication: Application() {
    companion object{
        lateinit var todoDatabase: TodoDatabase
    }

    override fun onCreate() {
        super.onCreate()
        Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            TodoDatabase.NAME
        ).build()
    }
}