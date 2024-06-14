package com.example.challengeb2ra.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.challengeb2ra.data.Earthquake
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.example.challengeb2ra.viewmodel.AuthViewModel
import com.example.challengeb2ra.viewmodel.EarthquakeViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalMaterial3Api
@Composable
fun EarthquakeListScreen(
    viewModel: EarthquakeViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    navController: NavHostController,
    onEarthquakeClick: (String) -> Unit
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    var showDatePicker by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    // DateRangePicker
    val dateRangePicker = remember {
        MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Selecciona un rango de fechas")
            .build()
    }

    dateRangePicker.addOnPositiveButtonClickListener { selection ->
        val startMillis = selection.first ?: return@addOnPositiveButtonClickListener
        val endMillis = selection.second ?: return@addOnPositiveButtonClickListener

        startDate = dateFormat.format(Date(startMillis))
        endDate = dateFormat.format(Date(endMillis))
    }

    LaunchedEffect(Unit) {
        viewModel.fetchEarthquakes()
    }

    val earthquakes = viewModel.getEarthquakesPaging(startDate, endDate).collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { showDatePicker = true }) {
                Text("Seleccionar Rango de Fechas")
            }
            Button(onClick = {
                authViewModel.logout {
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            }) {
                Text("Cerrar Sesión")
            }
        }
        if (showDatePicker) {
            val fragmentManager = (context as androidx.fragment.app.FragmentActivity).supportFragmentManager
            dateRangePicker.show(fragmentManager, "dateRangePicker")
            showDatePicker = false
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Fecha Inicio: $startDate")
        Text(text = "Fecha Término: $endDate")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { earthquakes.refresh() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Filtrar")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(earthquakes) { earthquake ->
                earthquake?.let {
                    EarthquakeItem(it) { selectedEarthquakeId ->
                        navController.navigate("earthquakeDetail/$selectedEarthquakeId")
                    }
                    Divider()
                }
            }
        }
    }
}

@Composable
fun EarthquakeItem(earthquake: Earthquake, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(earthquake.id) }
            .padding(16.dp)
    ) {
        Text(text = earthquake.properties.title, style = MaterialTheme.typography.bodyLarge)
        Text(text = "Magnitud: ${earthquake.properties.mag}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Profundidad: ${earthquake.geometry.coordinates[2]} km", style = MaterialTheme.typography.bodySmall)
        Text(text = "Lugar: ${earthquake.properties.place}", style = MaterialTheme.typography.bodySmall)
    }
}


