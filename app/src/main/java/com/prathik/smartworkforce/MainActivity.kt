package com.prathik.smartworkforce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.prathik.smartworkforce.auth.SessionManager
import com.prathik.smartworkforce.ui.theme.AppColors
import com.prathik.smartworkforce.ui.theme.SmartWorkforceTheme
import com.prathik.smartworkforce.viewmodels.ActivityLogViewModel
import com.prathik.smartworkforce.viewmodels.HRDashboardViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartWorkforceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_login") {
        composable("main_login") { LoginScreen(navController = navController) }
        composable("hr_sign_in") { HRSignInScreen(navController = navController) }
        composable("hr_sign_up") { SignUpScreen(navController = navController) }
        composable("employee_sign_in") { EmployeeSignInScreen(navController = navController) }
        composable("hr_dashboard") { HRDashboardScreen(navController = navController) }
        composable("employee_portal") { EmployeePortalScreen(navController = navController) }
        composable("employees") { EmployeesScreen(navController = navController) }
        composable("employee_detail") { EmployeeDetailScreen(navController = navController) }
        composable("tasks") { TasksScreen(navController = navController) }
        composable("add_task") { AddTaskScreen(navController = navController) }
        composable("leave_requests") { LeaveRequestsScreen(navController = navController) }
        composable("reports") { ReportsScreen(navController = navController) }
        composable("pending_approvals") { PendingApprovalsScreen(navController = navController) }
        composable("profile") { ProfileScreen(navController = navController) }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Logo()
            Spacer(modifier = Modifier.height(16.dp))
            Title()
            Spacer(modifier = Modifier.height(8.dp))
            Tagline()
            Spacer(modifier = Modifier.height(32.dp))
            LoginButtons(navController = navController)
        }
    }
}

@Composable
fun Title() {
    Text(
        text = stringResource(id = R.string.login_title),
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = AppColors.DarkText
    )
}

@Composable
fun Tagline() {
    Text(
        text = stringResource(id = R.string.login_tagline),
        fontSize = 14.sp,
        color = AppColors.LightText
    )
}

@Composable
fun LoginButtons(navController: NavController) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 24.dp)) {
        Button(
            onClick = { navController.navigate("hr_sign_in") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
        ) {
            Text("HR Login", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("employee_sign_in") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
        ) {
            Text("Employee Login", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HRDashboardScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val database = (context.applicationContext as SmartWorkforceApplication).database
    val hrManagerId = sessionManager.getUserId()?.toLong() ?: -1

    val viewModel: HRDashboardViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HRDashboardViewModel(database.employeeDao(), database.hrManagerDao(), hrManagerId) as T
            }
        }
    )

    val activityLogViewModel: ActivityLogViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ActivityLogViewModel(database.activityLogDao()) as T
            }
        }
    )

    val allTasks by database.taskDao().getAllTasks().collectAsState(initial = emptyList())
    val recentActivity by activityLogViewModel.recentActivity.collectAsState()
    val employees by database.employeeDao().getAllEmployees(hrManagerId).collectAsState(initial = emptyList())

    val pendingTasks = allTasks.count { it.status == "pending" }
    val completedTasks = allTasks.count { it.status == "completed" }

    val today = Calendar.getInstance()
    val presentEmployeeIds = recentActivity.filter { log ->
        val logDate = Calendar.getInstance().apply { timeInMillis = log.activityTimestamp }
        log.activityType == "login" &&
                logDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                logDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
    }.map { it.userId }.distinct()

    val employeeMap = employees.associateBy({ it.id.toString() }, { it.name })
    val presentEmployees = presentEmployeeIds.mapNotNull { employeeMap[it] }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController, currentRoute) { scope.launch { drawerState.close() } } }
    ) {
        Scaffold(
            containerColor = AppColors.Background,
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.dashboard), color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.PrimaryBlue),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, stringResource(id = R.string.hamburger_menu_content_description), tint = Color.White)
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { KeyMetrics(viewModel) }
                item {
                    DashboardCard(title = "Add Task") {
                        Button(
                            onClick = { navController.navigate("add_task") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add New Task")
                        }
                    }
                }
                item { TasksOverview(pendingTasks, completedTasks, allTasks.size) }
                item { AttendanceList(presentEmployees) }
                item { ReportsOverview(navController) }
            }
        }
    }
}

@Composable
fun AttendanceList(employees: List<String>) {
    DashboardCard(title = "Today's Attendance") {
        Column {
            if (employees.isNotEmpty()) {
                employees.forEach { name ->
                    Text(text = "$name - Present")
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                Text("No employees have logged in today.")
            }
        }
    }
}


@Composable
fun TasksOverview(pending: Int, completed: Int, total: Int) {
    DashboardCard(title = "Tasks Overview") {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            TaskStatusItem("Pending", pending)
            TaskStatusItem("Completed", completed)
            TaskStatusItem("Total", total)
        }
    }
}

@Composable
fun TaskStatusItem(status: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = status, fontSize = 14.sp, color = AppColors.LightText)
    }
}

@Composable
fun KeyMetrics(viewModel: HRDashboardViewModel) {
    val totalEmployees by viewModel.totalEmployees.collectAsState()

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        DashboardMetricCard(title = "Total Employees", value = totalEmployees.toString(), icon = Icons.Default.People, modifier = Modifier.weight(1f))
        DashboardMetricCard(title = "On Leave", value = "8", icon = Icons.Default.BeachAccess, modifier = Modifier.weight(1f))
    }
}


@Composable
fun ReportsOverview(navController: NavController) {
    DashboardCard(title = "Reports Overview") {
        Column {
            Text("Monthly performance and attendance reports are available.")
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate("reports") }) {
                Text("View All Reports")
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun DashboardMetricCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = title, tint = AppColors.PrimaryBlue)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = AppColors.PrimaryBlue)
            Text(text = title, fontSize = 14.sp, color = AppColors.LightText)
        }
    }
}

data class DrawerItem(val route: String, val labelResId: Int, val icon: ImageVector)

@Composable
fun DrawerContent(navController: NavController, currentRoute: String?, onCloseDrawer: () -> Unit) {
    val menuItems = listOf(
        DrawerItem("hr_dashboard", R.string.dashboard, Icons.Default.Dashboard),
        DrawerItem("employees", R.string.employees, Icons.Default.People),
        DrawerItem("tasks", R.string.tasks, Icons.Default.Task),
        DrawerItem("leave_requests", R.string.leave_requests, Icons.Default.Mail),
        DrawerItem("reports", R.string.reports, Icons.Default.BarChart)
    )

    ModalDrawerSheet(modifier = Modifier.width(300.dp), drawerContainerColor = AppColors.CardWhite) {
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
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = null,
                    tint = AppColors.PrimaryBlue,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = stringResource(id = R.string.hr_admin_portal), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Text("HR Manager", color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
        }

        Spacer(Modifier.height(12.dp))

        menuItems.forEach { item ->
            NavigationDrawerItem(
                icon = { Icon(item.icon, null, modifier = Modifier.size(24.dp)) },
                label = { Text(stringResource(item.labelResId), fontSize = 16.sp) },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route); onCloseDrawer() },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .height(56.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedTextColor = AppColors.DarkText,
                    selectedContainerColor = AppColors.PrimaryBlue.copy(alpha = 0.1f),
                    selectedTextColor = AppColors.PrimaryBlue
                )
            )
        }

        Spacer(Modifier.weight(1f))

        Divider(thickness = 1.dp, color = AppColors.LightText.copy(alpha = 0.5f))

        NavigationDrawerItem(
            label = { Text(stringResource(R.string.logout), fontSize = 16.sp) },
            selected = false,
            onClick = { onCloseDrawer(); navController.navigate("main_login") { popUpTo("main_login") { inclusive = true } } },
            icon = { Icon(Icons.AutoMirrored.Filled.Logout, null, modifier = Modifier.size(24.dp)) },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(56.dp),
            colors = NavigationDrawerItemDefaults.colors(unselectedTextColor = AppColors.RedError, unselectedIconColor = AppColors.RedError)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HRDashboardScreenPreview() {
    SmartWorkforceTheme { HRDashboardScreen(rememberNavController()) }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SmartWorkforceTheme { AppNavigation() }
}
