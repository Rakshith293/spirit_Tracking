package com.prathik.smartworkforce.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.prathik.smartworkforce.data.entities.HrManager

@Dao
interface HrManagerDao {

    @Insert
    suspend fun insert(hrManager: HrManager): Long

    @Query("SELECT * FROM hr_managers WHERE firebaseUid = :firebaseUid")
    suspend fun getHrManagerByFirebaseUid(firebaseUid: String): HrManager?
}