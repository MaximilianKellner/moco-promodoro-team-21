package com.example.promodoro_team_21.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Todo::class], version = 1)
@TypeConverters(Converters ::class)
abstract class TodoDatabase : RoomDatabase(){

    companion object {
        const val NAME = "Todo_DB"
    }

    abstract fun getTodoDao() : TodoDao

}