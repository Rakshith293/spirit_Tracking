package com.prathik.smartworkforce.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "employees",
    foreignKeys = [
        ForeignKey(
            entity = Organization::class,
            parentColumns = ["id"],
            childColumns = ["organizationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["organizationId"])]
)
data class EmployeeEntity(
    @PrimaryKey val id: String,
    val organizationId: Long,
    val firebaseUid: String = "",
    val name: String,
    val email: String,
    val role: String,
    val department: String = "",
    val rating: Int,
    val password: String,
    val accountType: String = "Employee", // "HR" or "Employee"
    val approvalStatus: String = "pending", // "approved", "pending", "rejected"
    val createdBy: String = "self", // "HR" or "self"
    val registrationTimestamp: Long = System.currentTimeMillis(),
    val additionTimestamp: Long? = null,
    val removalTimestamp: Long? = null,
    val removalReason: String? = null,
    val isActive: Boolean = true
)
