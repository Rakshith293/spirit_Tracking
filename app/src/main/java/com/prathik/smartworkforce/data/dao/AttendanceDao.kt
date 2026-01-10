package com.prathik.smartworkforce.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prathik.smartworkforce.data.entities.Attendance
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attendance: Attendance)

    @Query("SELECT * FROM attendance WHERE employeeId = :employeeId")
    fun getAttendanceForEmployee(employeeId: String): Flow<List<Attendance>>
}
