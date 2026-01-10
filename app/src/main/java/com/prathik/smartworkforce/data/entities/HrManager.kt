package com.prathik.smartworkforce.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "hr_managers",
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
data class HrManager(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firebaseUid: String,
    val name: String,
    val email: String,
    val organizationId: Long
)
