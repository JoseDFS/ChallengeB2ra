package com.example.challengeb2ra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.challengeb2ra.data.Api
import com.example.challengeb2ra.data.Earthquake
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.example.challengeb2ra.data.EarthquakePagingSource

class EarthquakeViewModel : ViewModel() {
    private val _earthquakes = MutableStateFlow<List<Earthquake>>(emptyList())
    val earthquakes: StateFlow<List<Earthquake>> get() = _earthquakes

    private val api = Api.retrofitService

    fun getEarthquakesPaging(startDate: String? = null, endDate: String? = null): Flow<PagingData<Earthquake>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { EarthquakePagingSource(api, startDate, endDate) }
        ).flow.cachedIn(viewModelScope)
    }

    fun fetchEarthquakes() {
        viewModelScope.launch {
            try {
                val response = api.getEarthquakes()
                _earthquakes.value = response.features
                Log.d("EarthquakeViewModel", "Fetched ${response.features.size} earthquakes")
            } catch (e: Exception) {
                Log.e("EarthquakeViewModel", "Error fetching earthquakes", e)
                _earthquakes.value = emptyList()
            }
        }
    }
}
