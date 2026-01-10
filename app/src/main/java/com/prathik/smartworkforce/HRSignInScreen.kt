package com.prathik.smartworkforce

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prathik.smartworkforce.auth.FirebaseManager
import com.prathik.smartworkforce.auth.SessionManager
import com.prathik.smartworkforce.data.entities.EmployeeEntity
import com.prathik.smartworkforce.ui.theme.AppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HRSignInScreen(navController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val firebaseManager = remember { FirebaseManager() }
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val database = (context.applicationContext as SmartWorkforceApplication).database

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
            Text("HR Login", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AppColors.DarkText)
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email ID") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.PrimaryBlue,
                    unfocusedBorderColor = AppColors.LightText.copy(alpha = 0.4f),
                    focusedLabelColor = AppColors.PrimaryBlue,
                    cursorColor = AppColors.PrimaryBlue
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, "Toggle password visibility", tint = AppColors.LightText)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.PrimaryBlue,
                    unfocusedBorderColor = AppColors.LightText.copy(alpha = 0.4f),
                    focusedLabelColor = AppColors.PrimaryBlue,
                    cursorColor = AppColors.PrimaryBlue
                )
            )
            Spacer(modifier = Modifier.height(32.dp))
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        keyboardController?.hide()
                        isLoading = true
                        errorMessage = null
                        coroutineScope.launch {
                            val success = firebaseManager.signIn(email, password)
                            if (success) {
                                val firebaseUid = firebaseManager.getCurrentUser()?.uid
                                if (firebaseUid != null) {
                                    val hrManager = database.hrManagerDao().getHrManagerByFirebaseUid(firebaseUid)
                                    if (hrManager != null) {
                                        sessionManager.saveSession(hrManager.id.toString(), "hr")
                                        DataRepository.currentUserType = "HR"
                                        // Also set the current employee in the DataRepository
                                        val employee = database.employeeDao().getEmployeeByEmail(hrManager.email)
                                        if (employee != null) {
                                            DataRepository.currentEmployee = employee
                                        } else {
                                            // If the HR manager is not in the employees table, add them.
                                            val newEmployee = EmployeeEntity(
                                                id = hrManager.id.toString(),
                                                organizationId = hrManager.organizationId,
                                                firebaseUid = hrManager.firebaseUid,
                                                name = hrManager.name,
                                                email = hrManager.email,
                                                role = "HR",
                                                department = "Management",
                                                rating = 5,
                                                password = "", // Not needed
                                                accountType = "HR",
                                                approvalStatus = "approved",
                                                createdBy = "System",
                                                additionTimestamp = System.currentTimeMillis(),
                                                isActive = true
                                            )
                                            database.employeeDao().insert(newEmployee)
                                            DataRepository.currentEmployee = newEmployee
                                        }

                                        navController.navigate("hr_dashboard") {
                                            popUpTo("main_login") { inclusive = true }
                                        }
                                    } else {
                                        errorMessage = "HR Manager not found in the database."
                                    }
                                } else {
                                    errorMessage = "Could not retrieve user information."
                                }
                            } else {
                                errorMessage = "Sign in failed. Please check your credentials."
                            }
                            isLoading = false
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

            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { navController.navigate("hr_sign_up") }) {
                Text("Don't have an account? Sign Up")
            }
        }
    }
}
