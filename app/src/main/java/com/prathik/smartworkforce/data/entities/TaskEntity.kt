package com.prathik.smartworkforce.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val assignedTo: String,
    val assignedBy: String,
    val dueDate: String,
    val priority: String, // "High", "Medium", "Low"
    var status: String // "Pending", "In Progress", "Completed"
)
