package com.prathik.smartworkforce.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prathik.smartworkforce.data.dao.*
import com.prathik.smartworkforce.data.entities.*

@Database(
    entities = [
        Organization::class,
        HrManager::class,
        EmployeeEntity::class,
        Task::class,
        LeaveRequest::class,
        Attendance::class,
        LoginHistory::class,
        ActivityLog::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun organizationDao(): OrganizationDao
    abstract fun hrManagerDao(): HrManagerDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun taskDao(): TaskDao
    abstract fun leaveRequestDao(): LeaveRequestDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun loginHistoryDao(): LoginHistoryDao
    abstract fun activityLogDao(): ActivityLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_workforce_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
