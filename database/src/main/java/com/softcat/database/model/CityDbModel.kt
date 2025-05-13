package com.softcat.database.model

data class CityDbModel(
    val id: Int = UNSPECIFIED_ID,
    val name: String,
    val countryId: Int,
    val latitude: Float,
    val longitude: Float,
) {
    companion object {
        const val UNSPECIFIED_ID = 0
    }
}