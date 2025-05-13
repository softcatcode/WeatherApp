package com.softcat.database.model

data class CountryDbModel(
    val id: Int = UNSPECIFIED_ID,
    val name: String
) {
    companion object {
        const val UNSPECIFIED_ID = 0
    }
}