package com.prathik.smartworkforce

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.prathik.smartworkforce.data.entities.ActivityLog
import com.prathik.smartworkforce.data.entities.LoginHistory
import com.prathik.smartworkforce.ui.theme.AppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeSignInScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()

    Scaffold(containerColor = AppColors.Background) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Logo()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Employee Login", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AppColors.DarkText)
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email ID") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    val trimmedEmail = email.trim()
                    val trimmedPassword = password.trim()
                    if (trimmedEmail.isNotBlank() && trimmedPassword.isNotBlank()) {
                        scope.launch {
                            firebaseAuth.signInWithEmailAndPassword(trimmedEmail, trimmedPassword)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        scope.launch {
                                            val employee = DataRepository.employeeDao.getEmployeeByEmail(trimmedEmail)
                                            if (employee != null) {
                                                when (employee.approvalStatus) {
                                                    "approved" -> {
                                                        DataRepository.currentEmployee = employee
                                                        DataRepository.currentUserType = "Employee"
                                                        val loginHistory = LoginHistory(
                                                            employeeId = employee.id,
                                                            firebaseUid = employee.firebaseUid,
                                                            loginTimestamp = System.currentTimeMillis()
                                                        )
                                                        DataRepository.loginHistoryDao.insert(loginHistory)
                                                        val activityLog = ActivityLog(
                                                            userId = employee.id,
                                                            userType = "Employee",
                                                            activityType = "login",
                                                            activityDescription = "Employee logged in",
                                                            activityTimestamp = System.currentTimeMillis()
                                                        )
                                                        DataRepository.activityLogDao.insert(activityLog)
                                                        navController.navigate("employee_portal") {
                                                            popUpTo("main_login") { inclusive = true }
                                                        }
                                                    }
                                                    "pending" -> {
                                                        firebaseAuth.signOut()
                                                        Toast.makeText(context, "Your account is pending HR approval", Toast.LENGTH_SHORT).show()
                                                    }
                                                    "rejected" -> {
                                                        firebaseAuth.signOut()
                                                        Toast.makeText(context, "Your account has been rejected. Contact HR.", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            } else {
                                                firebaseAuth.signOut()
                                                Toast.makeText(context, "Account not found", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } else {
                                        Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
            ) {
                Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
