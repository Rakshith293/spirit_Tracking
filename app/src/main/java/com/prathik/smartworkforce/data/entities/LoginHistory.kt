package com.prathik.smartworkforce.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "login_history")
data class LoginHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val employeeId: String,
    val firebaseUid: String,
    val loginTimestamp: Long,
    var logoutTimestamp: Long? = null
)
