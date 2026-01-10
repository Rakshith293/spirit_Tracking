package com.prathik.smartworkforce.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.prathik.smartworkforce.data.entities.LeaveRequest
import kotlinx.coroutines.flow.Flow

@Dao
interface LeaveRequestDao {
    @Insert
    suspend fun insert(leaveRequest: LeaveRequest)

    @Update
    suspend fun update(leaveRequest: LeaveRequest)

    @Query("SELECT * FROM leave_requests WHERE employeeId = :employeeId")
    fun getLeaveRequestsForEmployee(employeeId: String): Flow<List<LeaveRequest>>

    @Query("SELECT * FROM leave_requests WHERE status = 'pending'")
    fun getPendingLeaveRequests(): Flow<List<LeaveRequest>>
}
