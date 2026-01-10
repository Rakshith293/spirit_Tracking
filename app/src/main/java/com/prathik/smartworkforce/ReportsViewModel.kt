package com.prathik.smartworkforce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathik.smartworkforce.data.entities.Attendance
import com.prathik.smartworkforce.data.entities.EmployeeEntity
import com.prathik.smartworkforce.data.entities.LeaveRequest
import com.prathik.smartworkforce.data.entities.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ReportsViewModel(organizationId: Long) : ViewModel() {

    val employees = DataRepository.employeeDao.getAllEmployees(organizationId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val tasks = DataRepository.taskDao.getTasksForEmployee("0") // This needs to be improved
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val leaveRequests = DataRepository.leaveRequestDao.getPendingLeaveRequests()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val attendance = DataRepository.attendanceDao.getAttendanceForEmployee("0")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
