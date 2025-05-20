package com.softcat.domain.entity

data class WeatherTypeInfo(
    val code: Int,
    val dayDescription: String,
    val nightDescription: String,
    val iconUrl: String,
    val icon: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeatherTypeInfo

        if (code != other.code) return false
        if (dayDescription != other.dayDescription) return false
        if (nightDescription != other.nightDescription) return false
        if (iconUrl != other.iconUrl) return false
        if (!icon.contentEquals(other.icon)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code
        result = 31 * result + dayDescription.hashCode()
        result = 31 * result + nightDescription.hashCode()
        result = 31 * result + iconUrl.hashCode()
        result = 31 * result + icon.contentHashCode()
        return result
    }
}