package com.prathik.smartworkforce

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth
import com.prathik.smartworkforce.auth.SessionManager
import com.prathik.smartworkforce.data.entities.ActivityLog
import com.prathik.smartworkforce.data.entities.EmployeeEntity
import com.prathik.smartworkforce.ui.theme.AppColors
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeesScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val organizationId = sessionManager.getUserId()?.toLong() ?: -1L
    val employees by DataRepository.employeeDao.getAllEmployees(organizationId).collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                currentRoute = currentRoute,
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            containerColor = AppColors.Background,
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.employees), color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.PrimaryBlue),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(id = R.string.hamburger_menu_content_description), tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("pending_approvals") }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Pending Approvals", tint = Color.White)
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = AppColors.PrimaryBlue,
                    contentColor = Color.White,
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_new_employee_content_description), tint = Color.White)
                }
            },
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search employees...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.PrimaryBlue,
                        unfocusedBorderColor = AppColors.LightText.copy(alpha = 0.5f)
                    )
                )
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(employees.filter { it.name.contains(searchQuery, ignoreCase = true) }) { employee ->
                        EmployeeCard(employee = employee, navController = navController)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddEmployeeDialog(
            onDismiss = { showDialog = false },
            onAddEmployee = { name, email, password, role, department, rating ->
                scope.launch {
                    val firebaseAuth = FirebaseAuth.getInstance()
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser = task.result?.user
                                firebaseUser?.let {
                                    val employee = EmployeeEntity(
                                        id = UUID.randomUUID().toString(),
                                        organizationId = organizationId,
                                        firebaseUid = it.uid,
                                        name = name,
                                        email = email,
                                        role = role,
                                        department = department,
                                        rating = rating,
                                        password = "", // Never store password in the database
                                        accountType = "Employee",
                                        approvalStatus = "approved",
                                        createdBy = "HR",
                                        additionTimestamp = System.currentTimeMillis()
                                    )
                                    scope.launch {
                                        DataRepository.employeeDao.insert(employee)
                                        val activityLog = ActivityLog(
                                            userId = DataRepository.currentEmployee?.id ?: "",
                                            userType = "HR",
                                            activityType = "employee_added",
                                            activityDescription = "Added new employee: $name",
                                            activityTimestamp = System.currentTimeMillis()
                                        )
                                        DataRepository.activityLogDao.insert(activityLog)
                                        Toast.makeText(context, "Employee added successfully", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        )
    }
}

@Composable
fun EmployeeCard(employee: EmployeeEntity, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                DataRepository.currentEmployee = employee
                navController.navigate("employee_detail")
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(AppColors.PrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = employee.name.split(" ").take(2).joinToString("") { it.first().uppercase() },
                    fontWeight = FontWeight.Bold,
                    color = AppColors.PrimaryBlue,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = employee.name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text(text = employee.role, fontSize = 15.sp, color = AppColors.LightText)
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= employee.rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = null,
                            tint = if (i <= employee.rating) AppColors.OrangePending else AppColors.LightText.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeDialog(
    onDismiss: () -> Unit,
    onAddEmployee: (String, String, String, String, String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(0) }
    var isPasswordVisible by remember { mutableStateOf(false) }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.add_employee)) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = stringResource(id = R.string.employee_name)) },
                    shape = RoundedCornerShape(10.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                 OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Email") },
                    shape = RoundedCornerShape(10.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Password") },
                    shape = RoundedCornerShape(10.dp),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (isPasswordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(imageVector = image, "")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text(text = stringResource(id = R.string.job_role_title)) },
                    shape = RoundedCornerShape(10.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = department,
                    onValueChange = { department = it },
                    label = { Text(text = "Department") },
                    shape = RoundedCornerShape(10.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(id = R.string.rating))
                Row {
                    for (i in 1..5) {
                        IconButton(onClick = { rating = i }) {
                            Icon(
                                imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = if (i <= rating) AppColors.OrangePending else AppColors.LightText.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val trimmedEmail = email.trim()
                    val trimmedPassword = password.trim()
                    if (name.isNotBlank() && role.isNotBlank() && trimmedEmail.isNotBlank() && department.isNotBlank() && trimmedPassword.length >= 6) {
                        onAddEmployee(name.trim(), trimmedEmail, trimmedPassword, role.trim(), department.trim(), rating)
                        onDismiss()
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
            ) {
                Text(text = stringResource(id = R.string.add))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}
