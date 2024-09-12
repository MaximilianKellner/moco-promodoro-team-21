package com.example.promodoro_team_21.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.promodoro_team_21.MainApplication
import com.example.promodoro_team_21.model.Category
import com.example.promodoro_team_21.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date

class TodoViewModel : ViewModel() {

    val todoDao = MainApplication.todoDatabase.getTodoDao()
    val todoList: LiveData<List<Todo>> = todoDao.getAllTodo()

    // API wird für aktuelles Datum gebraucht
    fun addTodo(title: String, category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            // Eine umfangreiche Aufgabe. Coroutine sorgt dafür, dass die App nicht blockiert wird.
            todoDao.addTodo(
                Todo(
                    title = title,
                    category = category,
                    createdAt = Date.from(Instant.now())
                )
            )
        }
    }

    fun deleteTodo(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.deleteTodo(id = id)
        }
    }

    fun getTodoByCategory(category: Category): LiveData<List<Todo>> {
        return todoDao.getTodoByCategory(category)
    }

    fun editToDoTitle(id: Int, newTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.editTodoTitle(newTitle, id)
        }
    }
}
