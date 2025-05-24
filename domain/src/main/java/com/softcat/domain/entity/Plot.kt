package com.softcat.domain.entity

data class Plot(
    val id: Int,
    val parameter: String,
    val values: FloatArray,
    val time: LongArray,
    val cityId: Int,
    val authorId: String,
    val description: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Plot

        if (id != other.id) return false
        if (cityId != other.cityId) return false
        if (parameter != other.parameter) return false
        if (!values.contentEquals(other.values)) return false
        if (!time.contentEquals(other.time)) return false
        if (authorId != other.authorId) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + cityId
        result = 31 * result + parameter.hashCode()
        result = 31 * result + values.contentHashCode()
        result = 31 * result + time.contentHashCode()
        result = 31 * result + authorId.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }
}