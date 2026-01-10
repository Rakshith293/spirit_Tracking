package com.prathik.smartworkforce.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val assignedTo: String, // Employee ID
    val assignedBy: String, // Employee ID
    val assignmentTimestamp: Long,
    val dueTimestamp: Long,
    var completionTimestamp: Long? = null,
    var status: String = "pending", // "pending", "in_progress", "completed", "overdue"
    var priority: String = "medium" // "low", "medium", "high"
)
