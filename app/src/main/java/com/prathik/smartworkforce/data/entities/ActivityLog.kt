package com.prathik.smartworkforce.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_logs")
data class ActivityLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String, // Employee ID
    val userType: String, // "HR" or "Employee"
    val activityType: String, // "login", "logout", "employee_added", etc.
    val activityDescription: String,
    val activityTimestamp: Long,
    val additionalDetails: String? = null // JSON format for extra data
)
