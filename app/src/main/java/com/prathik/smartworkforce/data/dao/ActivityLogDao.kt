package com.prathik.smartworkforce.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.prathik.smartworkforce.data.entities.ActivityLog
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {
    @Insert
    suspend fun insert(activityLog: ActivityLog)

    @Query("SELECT * FROM activity_logs ORDER BY activityTimestamp DESC LIMIT 5")
    fun getRecentActivity(): Flow<List<ActivityLog>>
}
