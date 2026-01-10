package com.prathik.smartworkforce

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prathik.smartworkforce.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDetailScreen(navController: NavController) {
    val employee = DataRepository.currentEmployee

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Employee Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
    ) { paddingValues ->
        if (employee == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("No employee selected.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(AppColors.PrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = employee.name.split(" ").take(2).joinToString("") { it.first().uppercase() },
                    fontWeight = FontWeight.Bold,
                    color = AppColors.PrimaryBlue,
                    fontSize = 48.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(employee.name, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AppColors.DarkText)
            Text(employee.role, fontSize = 18.sp, color = AppColors.LightText)
            Spacer(modifier = Modifier.height(32.dp))

            DetailCard(icon = Icons.Default.CalendarToday, title = "Joining Date", value = "January 1, 2023")
            Spacer(modifier = Modifier.height(16.dp))
            DetailCard(icon = Icons.Default.CheckCircle, title = "Check-In / Check-Out", value = "9:05 AM / 6:15 PM")
            Spacer(modifier = Modifier.height(16.dp))
            TasksCard(tasks = listOf("Complete project proposal", "Review code for feature X", "Prepare for team meeting"))
        }
    }
}

@Composable
fun DetailCard(icon: ImageVector, title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = title, tint = AppColors.PrimaryBlue, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = AppColors.DarkText)
                Text(value, fontSize = 14.sp, color = AppColors.LightText)
            }
        }
    }
}

@Composable
fun TasksCard(tasks: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Task, contentDescription = "Assigned Tasks", tint = AppColors.PrimaryBlue, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Assigned Tasks", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = AppColors.DarkText)
            }
            Spacer(modifier = Modifier.height(12.dp))
            tasks.forEach {
                Text("• $it", fontSize = 14.sp, color = AppColors.LightText)
            }
        }
    }
}
