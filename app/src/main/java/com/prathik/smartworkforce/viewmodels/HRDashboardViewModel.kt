package com.prathik.smartworkforce.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathik.smartworkforce.data.dao.EmployeeDao
import com.prathik.smartworkforce.data.dao.HrManagerDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HRDashboardViewModel(
    private val employeeDao: EmployeeDao,
    private val hrManagerDao: HrManagerDao,
    private val hrManagerId: Long
) : ViewModel() {

    val totalEmployees: StateFlow<Int> = employeeDao.getAllEmployees(hrManagerId)
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)
}
