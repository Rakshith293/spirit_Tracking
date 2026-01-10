package com.prathik.smartworkforce

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.prathik.smartworkforce.auth.FirebaseManager
import com.prathik.smartworkforce.auth.SessionManager
import com.prathik.smartworkforce.data.entities.HrManager
import com.prathik.smartworkforce.data.entities.Organization
import com.prathik.smartworkforce.ui.theme.AppColors
import com.prathik.smartworkforce.ui.theme.SmartWorkforceTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val firebaseManager = FirebaseManager()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val database = (context.applicationContext as SmartWorkforceApplication).database

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "HR Sign Up") },
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

                Text("Create HR Account", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AppColors.DarkText)
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

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            if (password == confirmPassword) {
                                isLoading = true
                                coroutineScope.launch {
                                    val success = firebaseManager.signUp(email, password)
                                    if (success) {
                                        val organizationId = database.organizationDao().insert(Organization(name = "$name's Organization"))
                                        val firebaseUid = firebaseManager.getCurrentUser()?.uid ?: ""
                                        val hrManager = HrManager(
                                            firebaseUid = firebaseUid,
                                            name = name,
                                            email = email,
                                            organizationId = organizationId
                                        )
                                        val hrManagerId = database.hrManagerDao().insert(hrManager)
                                        sessionManager.saveSession(hrManagerId.toString(), "hr")

                                        navController.navigate("hr_dashboard") {
                                            popUpTo("main_login") { inclusive = false }
                                        }
                                    } else {
                                        errorMessage = "Sign up failed. Please try again."
                                    }
                                    isLoading = false
                                }
                            } else {
                                errorMessage = "Passwords do not match."
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

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SmartWorkforceTheme {
        SignUpScreen(navController = rememberNavController())
    }
}
