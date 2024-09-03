//Hier kommen alle CRUD Operationen hin

package com.example.promodoro_team_21.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {
    @Query("SELECT * FROM Todo ORDER BY createdAt DESC")
    fun getAllTodo(): LiveData<List<Todo>>

    //load all todos from a specific category
    @Query("SELECT * FROM Todo WHERE category = :category ORDER BY createdAt DESC")
    fun getTodoByCategory(category: Category): LiveData<List<Todo>>

    @Insert
    fun addTodo(todo: Todo)

    @Query("DELETE FROM Todo WHERE id = :id")
    fun deleteTodo(id: Int)
}