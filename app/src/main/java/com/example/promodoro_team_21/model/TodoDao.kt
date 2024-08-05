package com.example.promodoro_team_21.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.promodoro_team_21.model.Todo

interface TodoDao {
    @Query("select * from Todo order by createdAt desc")
    fun getAllTodo(): LiveData<List<Todo>>

    @Insert
    fun addTodo(todo: Todo)

    @Query("Delete from Todo where id = :id")
    fun deleteTodo(todo: Todo)
}