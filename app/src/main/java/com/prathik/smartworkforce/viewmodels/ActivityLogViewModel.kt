
package com.prathik.smartworkforce.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathik.smartworkforce.data.dao.ActivityLogDao
import com.prathik.smartworkforce.data.entities.ActivityLog
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ActivityLogViewModel(private val activityLogDao: ActivityLogDao) : ViewModel() {

    val recentActivity: StateFlow<List<ActivityLog>> = activityLogDao.getRecentActivity()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
