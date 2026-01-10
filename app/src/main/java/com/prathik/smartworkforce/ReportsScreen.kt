package com.prathik.smartworkforce

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.prathik.smartworkforce.ui.theme.AppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController, null) { scope.launch { drawerState.close() } } }
    ) {
        Scaffold(
            containerColor = AppColors.Background,
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.reports), color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.PrimaryBlue),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(id = R.string.hamburger_menu_content_description), tint = Color.White)
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { EmployeePerformanceDistribution() }
                item { TaskCompletionRate() }
                item { AttendanceTrend() }
            }
        }
    }
}

@Composable
fun EmployeePerformanceDistribution() {
    val pieChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice("Excellent", 45f, AppColors.GreenSuccess),
            PieChartData.Slice("Good", 30f, AppColors.PrimaryBlue),
            PieChartData.Slice("Average", 15f, AppColors.OrangePending),
            PieChartData.Slice("Poor", 10f, AppColors.RedError)
        ),
        plotType = PlotType.Donut
    )

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        showSliceLabels = true,
        animationDuration = 1500,
        sliceLabelTextSize = 14.sp
    )
    ChartCard(title = "Employee Performance Distribution") {
        DonutPieChart(
            modifier = Modifier.fillMaxWidth().height(250.dp),
            pieChartData = pieChartData,
            pieChartConfig = pieChartConfig
        )
    }
}

@Composable
fun TaskCompletionRate() {
    val barChartData = BarChartData(
        chartData = listOf(
            BarData(Point(0f, 90f), label = "Feature A"),
            BarData(Point(1f, 75f), label = "Feature B"),
            BarData(Point(2f, 60f), label = "Bug Fixes"),
            BarData(Point(3f, 85f), label = "Feature C")
        )
    )

    ChartCard(title = "Task Completion Rate") {
        BarChart(modifier = Modifier.height(250.dp), barChartData = barChartData)
    }
}

@Composable
fun AttendanceTrend() {
    val points = (1..10).map { Point(it.toFloat(), (80..100).random().toFloat()) }
    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = points,
                    lineStyle = LineStyle(color = AppColors.PrimaryBlue)
                )
            )
        ),
        xAxisData = AxisData.Builder().build(),
        yAxisData = AxisData.Builder().build()
    )

    ChartCard(title = "Attendance Trend") {
        LineChart(
            modifier = Modifier.fillMaxWidth().height(250.dp),
            lineChartData = lineChartData
        )
    }
}

@Composable
fun ChartCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            content()
        }
    }
}
