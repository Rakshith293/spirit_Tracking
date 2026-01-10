package com.prathik.smartworkforce.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leave_requests")
data class LeaveRequestEntity(
    @PrimaryKey val id: String,
    val employeeName: String,
    val reason: String,
    var status: String, // "Pending", "Approved", "Rejected"
    val requestDate: String,
    val fromDate: String,
    val toDate: String,
    val numberOfDays: Int,
    var approvedBy: String? = null
)
