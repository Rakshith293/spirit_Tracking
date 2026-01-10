package com.prathik.smartworkforce.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_records")
data class AttendanceEntity(
    @PrimaryKey val id: String,
    val employeeName: String,
    val date: String,
    val clockInTime: String?,
    val clockOutTime: String?,
    val totalHours: String?,
    val status: String // "Present", "Absent", "Leave", "Half Day"
)
