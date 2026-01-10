package com.prathik.smartworkforce.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val employeeId: String,
    val checkInTimestamp: Long,
    var checkOutTimestamp: Long? = null,
    var totalHours: Float? = null,
    val location: String? = null,
    var status: String = "present" // "present", "absent", "half-day", "late"
)
