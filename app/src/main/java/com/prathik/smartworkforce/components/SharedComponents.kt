package com.prathik.smartworkforce.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prathik.smartworkforce.ui.theme.AppColors

@Composable
fun StatusBadge(status: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = when(status) {
            "Pending" -> Color(0xFFFFF4E6)
            "Approved" -> Color(0xFFE8F8F5)
            "Rejected" -> Color(0xFFFDEDED)
            "In Progress" -> AppColors.PrimaryBlue.copy(alpha = 0.1f)
            "Completed" -> AppColors.GreenSuccess.copy(alpha = 0.1f)
            "High" -> AppColors.RedError.copy(alpha = 0.1f)
            "Medium" -> AppColors.OrangePending.copy(alpha = 0.1f)
            "Low" -> AppColors.GreenSuccess.copy(alpha = 0.1f)
            else -> Color(0xFFF0F0F0)
        }
    ) {
        Text(
            status,
            Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = when(status) {
                "Pending" -> AppColors.OrangePending
                "Approved" -> AppColors.GreenSuccess
                "Rejected" -> AppColors.RedError
                "In Progress" -> AppColors.PrimaryBlue
                "Completed" -> AppColors.GreenSuccess
                "High" -> AppColors.RedError
                "Medium" -> AppColors.OrangePending
                "Low" -> AppColors.GreenSuccess
                else -> AppColors.LightText
            }
        )
    }
}
