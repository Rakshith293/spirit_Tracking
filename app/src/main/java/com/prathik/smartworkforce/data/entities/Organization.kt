package com.prathik.smartworkforce.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "organizations")
data class Organization(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)