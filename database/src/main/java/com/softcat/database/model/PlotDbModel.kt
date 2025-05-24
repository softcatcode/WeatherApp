package com.softcat.database.model

data class PlotDbModel(
    val id: Int,
    val parameter: String,
    val values: String,
    val time: String,
    val cityId: Int,
    val authorId: String,
    val description: String
)