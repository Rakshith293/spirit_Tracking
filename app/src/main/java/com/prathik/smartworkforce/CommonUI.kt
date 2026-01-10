package com.prathik.smartworkforce

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.prathik.smartworkforce.ui.theme.AppColors

@Composable
fun Logo() {
    Box(
        modifier = Modifier.size(100.dp).clip(CircleShape).background(AppColors.PrimaryBlue),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_compass),
            contentDescription = "Logo",
            modifier = Modifier.size(60.dp),
            tint = Color.White
        )
    }
}

@Composable
fun SignUpPrompt(onSignUpClick: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        append("Don't have an account? ")
        withStyle(style = SpanStyle(color = AppColors.PrimaryBlue, fontWeight = FontWeight.Bold)) {
            append("Sign Up")
        }
    }
    Text(
        text = annotatedString,
        modifier = Modifier.clickable { onSignUpClick() }
    )
}
