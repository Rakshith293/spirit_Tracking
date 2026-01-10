package com.prathik.smartworkforce

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prathik.smartworkforce.auth.SessionManager
import com.prathik.smartworkforce.data.entities.ActivityLog
import com.prathik.smartworkforce.data.entities.EmployeeEntity
import com.prathik.smartworkforce.ui.theme.AppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingApprovalsScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val organizationId = sessionManager.getUserId()?.toLong() ?: -1L
    val pendingEmployees by DataRepository.employeeDao.getPendingEmployees(organizationId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pending Approvals") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = AppColors.Background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pendingEmployees) { employee ->
                PendingEmployeeCard(employee = employee, onApprove = {
                    scope.launch {
                        DataRepository.employeeDao.updateEmployeeStatus(employee.id, "approved")
                        val activityLog = ActivityLog(
                            userId = DataRepository.currentEmployee?.id ?: "",
                            userType = "HR",
                            activityType = "employee_approved",
                            activityDescription = "Approved employee: ${employee.name}",
                            activityTimestamp = System.currentTimeMillis()
                        )
                        DataRepository.activityLogDao.insert(activityLog)
                        Toast.makeText(context, "Employee approved", Toast.LENGTH_SHORT).show()
                    }
                }, onReject = {
                    scope.launch {
                        DataRepository.employeeDao.updateEmployeeStatus(employee.id, "rejected")
                        val activityLog = ActivityLog(
                            userId = DataRepository.currentEmployee?.id ?: "",
                            userType = "HR",
                            activityType = "employee_rejected",
                            activityDescription = "Rejected employee: ${employee.name}",
                            activityTimestamp = System.currentTimeMillis()
                        )
                        DataRepository.activityLogDao.insert(activityLog)
                        Toast.makeText(context, "Employee rejected", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}

@Composable
fun PendingEmployeeCard(
    employee: EmployeeEntity,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = employee.name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = employee.email, fontSize = 15.sp, color = AppColors.LightText)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${employee.role} - ${employee.department}", fontSize = 15.sp, color = AppColors.LightText)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = onApprove,
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Green)
                ) {
                    Text("Approve")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Red)
                ) {
                    Text("Reject")
                }
            }
        }
    }
}
