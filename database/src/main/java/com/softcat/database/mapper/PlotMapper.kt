package com.softcat.database.mapper

import com.google.gson.Gson
import com.softcat.database.model.PlotDbModel
import com.softcat.domain.entity.Plot

private fun String.toFloatArray(): FloatArray {
    val result = Gson().fromJson(this, FloatArray::class.java)
    return result
}

private fun String.toLongArray(): LongArray {
    val result = Gson().fromJson(this, LongArray::class.java)
    return result
}

fun PlotDbModel.toEntity() = Plot(
    id = id,
    parameter = parameter,
    values = values.toFloatArray(),
    time = values.toLongArray(),
    cityId = cityId,
    authorId = authorId,
    description = description,
)