package com.example.challengeb2ra.ui.views
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.challengeb2ra.data.Earthquake

@Composable
fun EarthquakeDetailScreen(earthquake: Earthquake) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = earthquake.properties.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Magnitud: ${earthquake.properties.mag}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Profundidad: ${earthquake.geometry.coordinates[2]} km", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Lugar: ${earthquake.properties.place}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        val location = LatLng(earthquake.geometry.coordinates[1], earthquake.geometry.coordinates[0])
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    onCreate(Bundle())
                    onResume()
                    getMapAsync { googleMap ->
                        googleMap.addMarker(MarkerOptions().position(location).title(earthquake.properties.title))
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
