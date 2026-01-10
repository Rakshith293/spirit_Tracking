package com.prathik.smartworkforce

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prathik.smartworkforce.data.entities.Task
import com.prathik.smartworkforce.ui.theme.AppColors
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentEmployee = DataRepository.currentEmployee
    val tasks by if (DataRepository.currentUserType == "HR") {
        DataRepository.taskDao.getAllTasks().collectAsState(initial = emptyList())
    } else {
        DataRepository.taskDao.getTasksForEmployee(currentEmployee?.id ?: "").collectAsState(initial = emptyList())
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, null) { scope.launch { drawerState.close() } }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.tasks), color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.PrimaryBlue),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(id = R.string.hamburger_menu_content_description), tint = Color.White)
                        }
                    }
                )
            },
            floatingActionButton = {
                if (DataRepository.currentUserType == "HR") {
                    FloatingActionButton(
                        onClick = { navController.navigate("add_task") },
                        containerColor = AppColors.PrimaryBlue,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Task")
                    }
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tasks) { task ->
                    TaskCard(task = task, onTaskCompleted = {
                        scope.launch {
                            DataRepository.taskDao.update(
                                task.copy(
                                    status = "Completed",
                                    completionTimestamp = System.currentTimeMillis()
                                )
                            )
                        }
                    })
                }
            }
        }
    }
}


@Composable
fun TaskCard(task: Task, onTaskCompleted: () -> Unit) {
    val assignedToEmployee by DataRepository.employeeDao.getEmployeeById(task.assignedTo).collectAsState(initial = null)
    val assignedByEmployee by DataRepository.employeeDao.getEmployeeById(task.assignedBy).collectAsState(initial = null)
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(AppColors.PrimaryBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Assignment,
                        contentDescription = "Task Icon",
                        tint = AppColors.PrimaryBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Text(text = task.title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = task.description, fontSize = 15.sp, color = AppColors.LightText)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Assigned to: ${assignedToEmployee?.name ?: ""}", fontSize = 14.sp)
                Text("Assigned by: ${assignedByEmployee?.name ?: ""}", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Due: ${sdf.format(Date(task.dueTimestamp))}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))

            if (task.status == "Completed") {
                Text(
                    text = "Completed on ${sdf.format(Date(task.completionTimestamp!!))}",
                    color = AppColors.Green,
                    fontWeight = FontWeight.SemiBold
                )
            } else if (DataRepository.currentUserType != "HR") {
                Button(
                    onClick = onTaskCompleted,
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Green),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Mark as Complete")
                }
            } else {
                Text(text = "Status: ${task.status}", color = AppColors.OrangePending, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
