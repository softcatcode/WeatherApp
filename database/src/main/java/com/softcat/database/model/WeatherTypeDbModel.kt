package com.softcat.database.model

data class WeatherTypeDbModel(
    val code: Int,
    val dayDescription: String,
    val nightDescription: String,
    val url: String,
    val iconBytes: ByteArray?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeatherTypeDbModel

        if (code != other.code) return false
        if (dayDescription != other.dayDescription) return false
        if (nightDescription != other.nightDescription) return false
        if (url != other.url) return false
        if (iconBytes != null) {
            if (other.iconBytes == null) return false
            if (!iconBytes.contentEquals(other.iconBytes)) return false
        } else if (other.iconBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code
        result = 31 * result + dayDescription.hashCode()
        result = 31 * result + nightDescription.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + (iconBytes?.contentHashCode() ?: 0)
        return result
    }
}