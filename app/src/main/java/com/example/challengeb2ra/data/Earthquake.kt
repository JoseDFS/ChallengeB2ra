package com.example.challengeb2ra.data

data class Earthquake(
    val id: String,
    val properties: Properties,
    val geometry: Geometry
)

data class Properties(
    val mag: Double,
    val place: String,
    val time: Long,
    val title: String
)

data class Geometry(
    val coordinates: List<Double>
)
