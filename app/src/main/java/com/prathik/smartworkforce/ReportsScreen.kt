package com.prathik.smartworkforce

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prathik.smartworkforce.ui.theme.AppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController, "reports") { scope.launch { drawerState.close() } } }
    ) {
        Scaffold(
            containerColor = Color(0xFFF1F8E9), // Matching the light green background from image
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.reports), color = Color.White, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2E7D32)),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(id = R.string.hamburger_menu_content_description), tint = Color.White)
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
                item { PerformanceDistributionCard() }
                item { TaskCompletionRateCard() }
                item { AttendanceTrendCard() }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun PerformanceDistributionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Employee Performance Distribution",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3147)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(150.dp)) {
                    Canvas(modifier = Modifier.size(140.dp)) {
                        val strokeWidth = 45f
                        // High - Green (60%)
                        drawArc(
                            color = Color(0xFF2E7D32),
                            startAngle = -90f,
                            sweepAngle = 216f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                        )
                        // Average - Orange (25%)
                        drawArc(
                            color = Color(0xFFFFA000),
                            startAngle = 126f,
                            sweepAngle = 90f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                        )
                        // Low - Red (15%)
                        drawArc(
                            color = Color(0xFFE53935),
                            startAngle = 216f,
                            sweepAngle = 54f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("124", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text("TOTAL", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LegendItem("High", "60%", Color(0xFF2E7D32))
                    LegendItem("Average", "25%", Color(0xFFFFA000))
                    LegendItem("Low", "15%", Color(0xFFE53935))
                }
            }
        }
    }
}

@Composable
fun LegendItem(label: String, percentage: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.width(130.dp)) {
        Box(modifier = Modifier.size(12.dp).background(color, RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label, modifier = Modifier.weight(1f), fontSize = 14.sp, color = Color.Gray)
        Text(text = percentage, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

@Composable
fun TaskCompletionRateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Task Completion Rate",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B3147)
                )
                Surface(color = Color(0xFFF0F2F5), shape = RoundedCornerShape(8.dp)) {
                    Text("Weekly", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 12.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                val data = listOf(0.7f, 0.4f, 0.9f, 0.3f, 0.6f)
                val labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
                data.forEachIndexed { index, value ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.width(42.dp).height(120.dp).background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp)), contentAlignment = Alignment.BottomCenter) {
                            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(value).background(Color(0xFF388E3C), RoundedCornerShape(8.dp)))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(labels[index], fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun AttendanceTrendCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Attendance Trend",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B3147)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(16.dp))
                    Text(" +2.4%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = Path()
                    val width = size.width
                    val height = size.height
                    
                    val points = listOf(0.8f, 0.65f, 0.85f, 0.45f, 0.8f, 0.4f, 0.75f, 0.35f, 0.9f)
                    val xInterval = width / (points.size - 1)
                    
                    path.moveTo(0f, height * (1 - points[0]))
                    
                    for (i in 1 until points.size) {
                        val prevX = (i - 1) * xInterval
                        val prevY = height * (1 - points[i - 1])
                        val currX = i * xInterval
                        val currY = height * (1 - points[i])
                        
                        val controlX1 = prevX + (currX - prevX) / 2
                        val controlX2 = prevX + (currX - prevX) / 2
                        
                        path.cubicTo(controlX1, prevY, controlX2, currY, currX, currY)
                    }

                    // Background Gradient
                    val fillPath = Path().apply {
                        addPath(path)
                        lineTo(width, height)
                        lineTo(0f, height)
                        close()
                    }
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF4CAF50).copy(alpha = 0.25f), Color.Transparent)
                        )
                    )

                    // Curve Line
                    drawPath(
                        path = path,
                        color = Color(0xFF2E7D32),
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                    
                    // Grid lines
                    drawLine(Color(0xFFF0F2F5), Offset(0f, height * 0.33f), Offset(width, height * 0.33f))
                    drawLine(Color(0xFFF0F2F5), Offset(0f, height * 0.66f), Offset(width, height * 0.66f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Week 1", "Week 2", "Week 3", "Week 4").forEach {
                    Text(it, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}
