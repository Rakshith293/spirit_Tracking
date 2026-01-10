package com.prathik.smartworkforce.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leave_requests")
data class LeaveRequest(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val employeeId: String,
    val leaveType: String, // "sick leave", "casual leave", "vacation", "emergency"
    val startTimestamp: Long,
    val endTimestamp: Long,
    val numberOfDays: Int,
    val reason: String,
    val requestTimestamp: Long,
    var status: String = "pending", // "pending", "approved", "rejected"
    var approvedBy: String? = null, // Employee ID of HR
    var approvalTimestamp: Long? = null,
    var hrComments: String? = null
)
