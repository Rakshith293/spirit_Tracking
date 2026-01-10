package com.prathik.smartworkforce

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.prathik.smartworkforce.components.StatusBadge
import com.prathik.smartworkforce.data.entities.ActivityLog
import com.prathik.smartworkforce.data.entities.Attendance
import com.prathik.smartworkforce.data.entities.LeaveRequest
import com.prathik.smartworkforce.ui.theme.AppColors
import com.prathik.smartworkforce.ui.theme.SmartWorkforceTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeePortalScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            EmployeeDrawerContent(
                navController = navController,
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            containerColor = AppColors.Background,
            topBar = {
                TopAppBar(
                    title = { Text(text = "Employee Portal", color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.PrimaryBlue),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(id = R.string.hamburger_menu_content_description),
                                tint = Color.White
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { WelcomeCard() }
                item { AttendanceSection() }
                item { TasksSection() }
                item { LeaveRequestSection() }
                item { RequestStatusSection() }
            }
        }
    }
}

@Composable
fun EmployeeDrawerContent(
    navController: NavController,
    onCloseDrawer: () -> Unit
) {
    val currentEmployee = DataRepository.currentEmployee
    val scope = rememberCoroutineScope()
    ModalDrawerSheet(
        drawerContainerColor = AppColors.CardWhite,
        modifier = Modifier.width(300.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(AppColors.PrimaryBlue)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = AppColors.PrimaryBlue,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = currentEmployee?.name ?: "Employee",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Text(
                text = currentEmployee?.email ?: "employee@example.com",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
        }
        Spacer(Modifier.height(12.dp))
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Dashboard, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Dashboard", fontSize = 16.sp) },
            selected = true,
            onClick = { onCloseDrawer() },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp).height(56.dp),
            colors = NavigationDrawerItemDefaults.colors(
                unselectedTextColor = AppColors.DarkText,
                selectedContainerColor = AppColors.PrimaryBlue.copy(alpha = 0.1f),
                selectedTextColor = AppColors.PrimaryBlue
            )
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Profile", fontSize = 16.sp) },
            selected = false,
            onClick = { navController.navigate("profile"); onCloseDrawer() },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp).height(56.dp),
            colors = NavigationDrawerItemDefaults.colors(
                unselectedTextColor = AppColors.DarkText,
                selectedTextColor = AppColors.PrimaryBlue
            )
        )
        // ... add other items similarly
        Spacer(Modifier.weight(1f))

        HorizontalDivider(thickness = 1.dp, color = AppColors.LightText)

        NavigationDrawerItem(
            label = { Text("Logout", fontSize = 16.sp) },
            selected = false,
            onClick = {
                scope.launch {
                    val loginHistory = DataRepository.loginHistoryDao.getLatestLogin(currentEmployee?.id ?: "")
                    if (loginHistory != null) {
                        loginHistory.logoutTimestamp = System.currentTimeMillis()
                        DataRepository.loginHistoryDao.update(loginHistory)
                    }
                    val activityLog = ActivityLog(
                        userId = currentEmployee?.id ?: "",
                        userType = "Employee",
                        activityType = "logout",
                        activityDescription = "Employee logged out",
                        activityTimestamp = System.currentTimeMillis()
                    )
                    DataRepository.activityLogDao.insert(activityLog)
                    FirebaseAuth.getInstance().signOut()
                    DataRepository.currentEmployee = null
                    navController.navigate("main_login") {
                        popUpTo("main_login") { inclusive = true }
                    }
                    onCloseDrawer()
                }
            },
            icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(24.dp)) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).height(56.dp),
            colors = NavigationDrawerItemDefaults.colors(
                unselectedTextColor = AppColors.RedError,
                unselectedIconColor = AppColors.RedError
            )
        )
    }
}

@Composable
fun WelcomeCard() {
    val currentEmployee = DataRepository.currentEmployee

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.PrimaryBlue)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Welcome, ${currentEmployee?.name ?: "Employee"}!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Here's your dashboard for today.",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun AttendanceSection() {
    val currentEmployee = DataRepository.currentEmployee
    var clockInTime by remember { mutableStateOf<Long?>(null) }
    var clockOutTime by remember { mutableStateOf<Long?>(null) }
    var isClockIn by remember { mutableStateOf(false) }
    var workingSeconds by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    // Load today's attendance on launch
    LaunchedEffect(Unit) {
        currentEmployee?.let { employee ->
            val attendance = DataRepository.attendanceDao.getAttendanceForEmployee(employee.id).firstOrNull()?.firstOrNull { it.checkInTimestamp >= System.currentTimeMillis() - 24 * 60 * 60 * 1000 }
            if (attendance != null) {
                clockInTime = attendance.checkInTimestamp
                clockOutTime = attendance.checkOutTimestamp
                if (clockOutTime == null) {
                    isClockIn = true
                    // Calculate working seconds from clock in time
                }
            }
        }
    }

    // Timer effect
    LaunchedEffect(isClockIn) {
        while (isClockIn) {
            delay(1000)
            workingSeconds++
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Mark Attendance", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(12.dp))

            Text(
                "Clock In: ${clockInTime?.let { timeFormatter.format(Date(it)) } ?: "--:--"} | Clock Out: ${clockOutTime?.let { timeFormatter.format(Date(it)) } ?: "--:--"}",
                fontSize = 15.sp
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Clock In Button
                Button(
                    onClick = {
                        scope.launch {
                            val time = System.currentTimeMillis()
                            clockInTime = time
                            isClockIn = true
                            workingSeconds = 0

                            currentEmployee?.let { employee ->
                                val attendance = Attendance(
                                    employeeId = employee.id,
                                    checkInTimestamp = time
                                )
                                DataRepository.attendanceDao.insert(attendance)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.GreenSuccess),
                    enabled = !isClockIn
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Clock In", fontSize = 15.sp)
                }

                // Clock Out Button
                Button(
                    onClick = {
                        scope.launch {
                            val time = System.currentTimeMillis()
                            clockOutTime = time
                            isClockIn = false

                            val hours = workingSeconds / 3600
                            val minutes = (workingSeconds % 3600) / 60
                            val totalHours = String.format(Locale.getDefault(), "%dh %dm", hours, minutes)

                            currentEmployee?.let { employee ->
                                val attendance = DataRepository.attendanceDao.getAttendanceForEmployee(employee.id).firstOrNull()?.firstOrNull { it.checkInTimestamp >= System.currentTimeMillis() - 24 * 60 * 60 * 1000 }
                                if (attendance != null) {
                                    attendance.checkOutTimestamp = time
                                    attendance.totalHours = hours.toFloat()
                                    DataRepository.attendanceDao.insert(attendance)
                                }
                            }

                            Toast.makeText(context, "Total working hours: $totalHours", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.RedError),
                    enabled = isClockIn
                ) {
                    Icon(Icons.Default.Stop, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Clock Out", fontSize = 15.sp)
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Current Working Time: ${String.format(Locale.getDefault(), "%02d:%02d:%02d",
                    workingSeconds/3600, (workingSeconds%3600)/60, workingSeconds%60)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun TasksSection() {
    val currentEmployee = DataRepository.currentEmployee
    val myTasks by DataRepository.taskDao
        .getTasksForEmployee(currentEmployee?.id ?: "")
        .collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp))
    {
        Column(Modifier.padding(16.dp)) {
            Text("Tasks & Messages", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(12.dp))

            if (myTasks.isEmpty()) {
                Text("No tasks assigned.", color = AppColors.LightText)
            } else {
                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                    items(myTasks) { task ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Priority indicator
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        when(task.priority) {
                                            "High" -> AppColors.RedError
                                            "Medium" -> AppColors.OrangePending
                                            else -> AppColors.GreenSuccess
                                        },
                                        CircleShape
                                    )
                            )
                            Spacer(Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(task.title, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                                Text("Due: ${Date(task.dueTimestamp)}", fontSize = 13.sp, color = AppColors.LightText)
                            }
                            // Status badge
                            StatusBadge(task.status)
                        }

                        if (task.status != "completed") {
                            Button(
                                onClick = {
                                    scope.launch {
                                        task.status = "completed"
                                        DataRepository.taskDao.insert(task)
                                        Toast.makeText(context, "Task marked as completed", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.height(36.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AppColors.GreenSuccess)
                            ) {
                                Text("Mark Complete", fontSize = 13.sp)
                            }
                        }

                        HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveRequestSection() {
    val currentEmployee = DataRepository.currentEmployee
    val context = LocalContext.current
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }
    var fromDate by remember { mutableStateOf<Long?>(null) }
    var toDate by remember { mutableStateOf<Long?>(null) }
    var leaveReason by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Date formatter
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    // In the Leave Request Card, replace the simple text field with this:
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp))
    {
        Column(Modifier.padding(16.dp)) {
            Text("Request Leave", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(16.dp))

            // From Date Selector
            OutlinedButton(
                onClick = { showFromDatePicker = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (fromDate != null) {
                        "From: ${dateFormatter.format(Date(fromDate!!))}"
                    } else {
                        "Select From Date"
                    },
                    fontSize = 15.sp,
                    color = if (fromDate != null) AppColors.DarkText else AppColors.LightText
                )
            }

            Spacer(Modifier.height(12.dp))

            // To Date Selector
            OutlinedButton(
                onClick = { showToDatePicker = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                enabled = fromDate != null // Can only select 'to date' after 'from date'
            ) {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (toDate != null) {
                        "To: ${dateFormatter.format(Date(toDate!!))}"
                    } else {
                        "Select To Date"
                    },
                    fontSize = 15.sp,
                    color = if (toDate != null) AppColors.DarkText else AppColors.LightText
                )
            }

            Spacer(Modifier.height(12.dp))

            // Calculate number of days
            if (fromDate != null && toDate != null) {
                val days = ((toDate!! - fromDate!!) / (1000 * 60 * 60 * 24)) + 1
                Text(
                    text = "Total Days: $days",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.PrimaryBlue
                )
                Spacer(Modifier.height(12.dp))
            }

            // Reason Text Field
            OutlinedTextField(
                value = leaveReason,
                onValueChange = { leaveReason = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Reason for leave...") },
                shape = RoundedCornerShape(10.dp),
                minLines = 3,
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.PrimaryBlue,
                    unfocusedBorderColor = AppColors.LightText.copy(alpha = 0.5f)
                )
            )

            Spacer(Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = {
                    if (fromDate != null && toDate != null && leaveReason.isNotBlank() && currentEmployee != null) {
                        scope.launch {
                            val days = ((toDate!! - fromDate!!) / (1000 * 60 * 60 * 24)) + 1
                            val newRequest = LeaveRequest(
                                employeeId = currentEmployee.id,
                                reason = leaveReason,
                                status = "pending",
                                requestTimestamp = System.currentTimeMillis(),
                                startTimestamp = fromDate!!,
                                endTimestamp = toDate!!,
                                numberOfDays = days.toInt(),
                                leaveType = "casual leave"
                            )
                            // Save to database (see Part 3)
                            DataRepository.leaveRequestDao.insert(newRequest)

                            // Clear form
                            fromDate = null
                            toDate = null
                            leaveReason = ""

                            Toast.makeText(context, "Leave request submitted", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
            ) {
                Text("Submit Request", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }

    // From Date Picker Dialog
    if (showFromDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = fromDate ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showFromDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    fromDate = datePickerState.selectedDateMillis
                    showFromDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFromDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // To Date Picker Dialog
    if (showToDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = toDate ?: fromDate ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showToDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null && fromDate != null && selectedDate >= fromDate!!) {
                        toDate = selectedDate
                        showToDatePicker = false
                    } else {
                        Toast.makeText(context, "To date must be after from date", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showToDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun RequestStatusSection() {
    val currentEmployee = DataRepository.currentEmployee
    val myLeaveRequests by DataRepository.leaveRequestDao
        .getLeaveRequestsForEmployee(currentEmployee?.id ?: "")
        .collectAsState(initial = emptyList())

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Request Status", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(12.dp))

            if (myLeaveRequests.isEmpty()) {
                Text("No leave requests submitted.", color = AppColors.LightText)
            } else {
                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                    items(myLeaveRequests) { request ->
                        Column(Modifier.padding(vertical = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(request.reason, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                                    Text(
                                        "${Date(request.startTimestamp)} to ${Date(request.endTimestamp)} (${request.numberOfDays} days)",
                                        fontSize = 13.sp,
                                        color = AppColors.LightText
                                    )
                                    Text("Submitted: ${Date(request.requestTimestamp)}", fontSize = 12.sp, color = AppColors.LightText)
                                    if (request.approvedBy != null) {
                                        Text("by ${request.approvedBy}", fontSize = 12.sp, color = AppColors.LightText)
                                    }
                                }
                                StatusBadge(request.status)
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmployeePortalScreenPreview() {
    SmartWorkforceTheme {
        EmployeePortalScreen(navController = rememberNavController())
    }
}
