package com.example.promodoro_team_21

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title : String,
    var createdAt : Date
)