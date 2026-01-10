package com.prathik.smartworkforce

import com.prathik.smartworkforce.data.AppDatabase
import com.prathik.smartworkforce.data.dao.ActivityLogDao
import com.prathik.smartworkforce.data.dao.AttendanceDao
import com.prathik.smartworkforce.data.dao.EmployeeDao
import com.prathik.smartworkforce.data.dao.LeaveRequestDao
import com.prathik.smartworkforce.data.dao.LoginHistoryDao
import com.prathik.smartworkforce.data.dao.TaskDao
import com.prathik.smartworkforce.data.entities.EmployeeEntity

object DataRepository {
    private lateinit var database: AppDatabase

    fun initialize(database: AppDatabase) {
        this.database = database
    }

    // Expose DAOs
    val employeeDao: EmployeeDao get() = database.employeeDao()
    val taskDao: TaskDao get() = database.taskDao()
    val leaveRequestDao: LeaveRequestDao get() = database.leaveRequestDao()
    val attendanceDao: AttendanceDao get() = database.attendanceDao()
    val loginHistoryDao: LoginHistoryDao get() = database.loginHistoryDao()
    val activityLogDao: ActivityLogDao get() = database.activityLogDao()

    // Current logged in user
    var currentEmployee: EmployeeEntity? = null
    var currentUserType: String? = null // "HR" or "Employee"
}
