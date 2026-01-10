package com.prathik.smartworkforce

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prathik.smartworkforce.data.entities.LeaveRequest
import com.prathik.smartworkforce.ui.theme.AppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveRequestsScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val pendingLeaveRequests by DataRepository.leaveRequestDao.getPendingLeaveRequests().collectAsState(initial = emptyList())

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, null) { scope.launch { drawerState.close() } }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.leave_requests), color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.PrimaryBlue),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(id = R.string.hamburger_menu_content_description), tint = Color.White)
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pendingLeaveRequests) { leaveRequest ->
                    LeaveRequestCard(leaveRequest = leaveRequest, onAction = {
                        scope.launch {
                            DataRepository.leaveRequestDao.update(it)
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun LeaveRequestCard(leaveRequest: LeaveRequest, onAction: (LeaveRequest) -> Unit) {
    val employee by DataRepository.employeeDao.getEmployeeById(leaveRequest.employeeId).collectAsState(initial = null)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Leave Request from ${employee?.name ?: leaveRequest.employeeId}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Reason: ${leaveRequest.reason}", fontSize = 15.sp, color = AppColors.LightText)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = { onAction(leaveRequest.copy(status = "Approved")) },
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Green)
                ) {
                    Text("Approve")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onAction(leaveRequest.copy(status = "Rejected")) },
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Red)
                ) {
                    Text("Reject")
                }
            }
        }
    }
}
