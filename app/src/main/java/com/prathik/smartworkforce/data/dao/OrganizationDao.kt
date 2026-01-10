package com.prathik.smartworkforce.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.prathik.smartworkforce.data.entities.Organization

@Dao
interface OrganizationDao {

    @Insert
    suspend fun insert(organization: Organization): Long
}