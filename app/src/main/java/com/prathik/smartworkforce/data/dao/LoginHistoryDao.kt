package com.prathik.smartworkforce.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.prathik.smartworkforce.data.entities.LoginHistory

@Dao
interface LoginHistoryDao {
    @Insert
    suspend fun insert(loginHistory: LoginHistory)

    @Update
    suspend fun update(loginHistory: LoginHistory)

    @Query("SELECT * FROM login_history WHERE employeeId = :employeeId ORDER BY loginTimestamp DESC LIMIT 1")
    suspend fun getLatestLogin(employeeId: String): LoginHistory?
}
