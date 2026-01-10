package com.prathik.smartworkforce

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.prathik.smartworkforce.data.entities.EmployeeEntity
import com.prathik.smartworkforce.ui.theme.AppColors
import com.prathik.smartworkforce.ui.theme.SmartWorkforceTheme
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeSignUpScreen(navController: NavController) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var jobRole by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }

    val jobRoles = listOf("Developer", "Designer", "Manager", "HR", "Sales", "Marketing")
    val departments = listOf("Engineering", "Design", "HR", "Sales", "Marketing", "Finance")


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text("Create an Account", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AppColors.DarkText)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Let's get you set up.", fontSize = 16.sp, color = AppColors.LightText)
                Spacer(modifier = Modifier.height(40.dp))

                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AppColors.LightText) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.PrimaryBlue,
                        unfocusedBorderColor = AppColors.LightText.copy(alpha = 0.4f),
                        focusedLabelColor = AppColors.PrimaryBlue,
                        cursorColor = AppColors.PrimaryBlue
                    ),
                    textStyle = TextStyle(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email ID") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = AppColors.LightText) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.PrimaryBlue,
                        unfocusedBorderColor = AppColors.LightText.copy(alpha = 0.4f),
                        focusedLabelColor = AppColors.PrimaryBlue,
                        cursorColor = AppColors.PrimaryBlue
                    ),
                    textStyle = TextStyle(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Job Role Dropdown
                var jobRoleExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = jobRoleExpanded, onExpandedChange = { jobRoleExpanded = !jobRoleExpanded }) {
                    OutlinedTextField(
                        value = jobRole,
                        onValueChange = {},
                        label = { Text("Job Role") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        readOnly = true,
                        shape = RoundedCornerShape(10.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = jobRoleExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppColors.PrimaryBlue,
                            unfocusedBorderColor = AppColors.LightText.copy(alpha = 0.4f),
                            focusedLabelColor = AppColors.PrimaryBlue,
                            cursorColor = AppColors.PrimaryBlue
                        )
                    )
                    ExposedDropdownMenu(expanded = jobRoleExpanded, onDismissRequest = { jobRoleExpanded = false }) {
                        jobRoles.forEach {
                            DropdownMenuItem(text = { Text(it) }, onClick = { 
                                jobRole = it
                                jobRoleExpanded = false
                             })
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Department Dropdown
                var departmentExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = departmentExpanded, onExpandedChange = { departmentExpanded = !departmentExpanded }) {
                    OutlinedTextField(
                        value = department,
                        onValueChange = {},
                        label = { Text("Department") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        readOnly = true,
                        shape = RoundedCornerShape(10.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = departmentExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppColors.PrimaryBlue,
                            unfocusedBorderColor = AppColors.LightText.copy(alpha = 0.4f),
                            focusedLabelColor = AppColors.PrimaryBlue,
                            cursorColor = AppColors.PrimaryBlue
                        )
                    )
                    ExposedDropdownMenu(expanded = departmentExpanded, onDismissRequest = { departmentExpanded = false }) {
                        departments.forEach {
                            DropdownMenuItem(text = { Text(it) }, onClick = { 
                                department = it
                                departmentExpanded = false
                             })
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AppColors.LightText) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, "Toggle password visibility", tint = AppColors.LightText)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.PrimaryBlue,
                        unfocusedBorderColor = AppColors.LightText.copy(alpha = 0.4f),
                        focusedLabelColor = AppColors.PrimaryBlue,
                        cursorColor = AppColors.PrimaryBlue
                    ),
                    textStyle = TextStyle(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password Field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AppColors.LightText) },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(image, "Toggle password visibility", tint = AppColors.LightText)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.PrimaryBlue,
                        unfocusedBorderColor = AppColors.LightText.copy(alpha = 0.4f),
                        focusedLabelColor = AppColors.PrimaryBlue,
                        cursorColor = AppColors.PrimaryBlue
                    ),
                    textStyle = TextStyle(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Sign Up Button
                Button(
                    onClick = {
                        if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() && jobRole.isNotBlank() && department.isNotBlank()) {
                            if (password == confirmPassword) {
                                if (password.length >= 6) {
                                    scope.launch {
                                        val firebaseAuth = FirebaseAuth.getInstance()
                                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    val firebaseUser = task.result?.user
                                                    firebaseUser?.let {
                                                        val employee = EmployeeEntity(
                                                            id = UUID.randomUUID().toString(),
                                                            organizationId = 0L, // Placeholder value
                                                            firebaseUid = it.uid,
                                                            name = name,
                                                            email = email,
                                                            role = jobRole,
                                                            department = department,
                                                            rating = 0,
                                                            password = "", // Never store password in the database
                                                            approvalStatus = "pending",
                                                            createdBy = "self"
                                                        )
                                                        scope.launch {
                                                            DataRepository.employeeDao.insert(employee)
                                                            firebaseAuth.signOut()
                                                            Toast.makeText(context, "Registration submitted. Waiting for HR approval.", Toast.LENGTH_LONG).show()
                                                            navController.popBackStack()
                                                        }
                                                    }
                                                } else {
                                                    Toast.makeText(context, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                    }
                                } else {
                                    Toast.makeText(context, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.PrimaryBlue
                    )
                ) {
                    Text("Sign Up", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmployeeSignUpScreenPreview() {
    SmartWorkforceTheme {
        EmployeeSignUpScreen(navController = rememberNavController())
    }
}
