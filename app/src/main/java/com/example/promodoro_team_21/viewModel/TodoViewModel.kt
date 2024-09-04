package com.example.promodoro_team_21.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
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


//Findet im ViewModel statt, weil Aufgaben von Nutzer in der UI hinzugefügt werden. Dies passiert
//aber eigentlich erst im ViewModel. Hier werden die Todos verwaltet und an das Model weitergegeben,
//wo die Todos in der Room Database gespeichert werden.
class TodoViewModel: ViewModel() {

    val todoDao = MainApplication.todoDatabase.getTodoDao()
    val todoList: LiveData<List<Todo>> = todoDao.getAllTodo()

    //API wird für aktuelles Datum gebraucht
    fun addTodo(title: String, category: Category){
        viewModelScope.launch(Dispatchers.IO){
            //Das ist eine Heavy Task. Wenn wir eine große Task hinizufügen und das Adden des
            // Todos lange dauert, soll ja nciht die ganze App blockiert sein. Deshalb Coroutinen
            todoDao.addTodo(Todo(title = title, category = category, createdAt = Date.from(Instant.now())))
        }
    }

    fun deleteTodo(id: Int){
        viewModelScope.launch(Dispatchers.IO){
            todoDao.deleteTodo(id = id)
        }
    }

    fun getTodoByCategory(category: Category): LiveData<List<Todo>>{
        return todoDao.getTodoByCategory(category)
    }

    fun editToDo(todoItem: Todo, newTitle: String) {
        todoItem.title = newTitle

        viewModelScope.launch {
            todoDao.editTodoTitle(newTitle, todoItem.id)
        }
    }
}