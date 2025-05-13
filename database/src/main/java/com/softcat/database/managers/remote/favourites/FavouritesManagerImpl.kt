package com.softcat.database.managers.remote.favourites

import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.softcat.database.internal.DatabaseRules
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class FavouritesManagerImpl @Inject constructor(): FavouritesManager {

    private fun favouritesRef(userId: Int) = Firebase.database.getReference(userId.toString())

    private suspend fun updateFavouriteCities(userId: Int, cities: List<Int>) {
        val reference = Firebase.database.getReference(userId.toString())
        return withTimeout(DatabaseRules.TIMEOUT) {
            var flag = true
            reference.setValue(cities).addOnSuccessListener {
                flag = false
            }.addOnFailureListener {
                throw it
            }
            while (flag) {
                delay(1L)
            }
        }
    }

    override suspend fun addToFavourites(userId: Int, cityId: Int): Result<Unit> {
        val cities: List<Int> = try {
            getFavouriteCitiesFromFirebase(userId)
        } catch (e: Exception) {
            return Result.failure(e)
        }
        if (cities.contains(cityId))
            return Result.success(Unit)
        val newCityList = cities.toMutableList().apply{
            add(cityId)
        }
        try {
            updateFavouriteCities(userId, newCityList)
        } catch (e: Exception) {
            return Result.failure(e)
        }
        return Result.success(Unit)
    }

    override suspend fun removeFromFavourites(userId: Int, cityId: Int): Result<Unit> {
        val cities: List<Int>
        try {
            cities = getFavouriteCitiesFromFirebase(userId)
        } catch (e: Exception) {
            return Result.failure(e)
        }
        val newCityList = cities.toMutableList().apply{
            remove(cityId)
        }
        try {
            updateFavouriteCities(userId, newCityList)
        } catch (e: Exception) {
            return Result.failure(e)
        }
        return Result.success(Unit)
    }

    private suspend fun getFavouriteCitiesFromFirebase(userId: Int): List<Int> {
        val reference = favouritesRef(userId)
        return withTimeout(DatabaseRules.TIMEOUT) {
            val result = mutableListOf<Int>()
            var flag = true
            reference.get().addOnSuccessListener { response ->
                response.children.iterator().forEach { elem ->
                    result.add(elem.value.toString().toInt())
                }
                flag = false
            }.addOnFailureListener {
                throw it
            }
            while (flag) {
                delay(1L)
            }
            result
        }
    }

    override suspend fun getFavouriteCitiesIds(userId: Int): Result<List<Int>> {
        try {
            val cities = getFavouriteCitiesFromFirebase(userId)
            return Result.success(cities)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun isFavourite(userId: Int, cityId: Int): Result<Boolean> {
        val cities: List<Int>
        try {
            cities = getFavouriteCitiesFromFirebase(userId)
        } catch (e: Exception) {
            return Result.failure(e)
        }
        val answer = cities.contains(cityId)
        return Result.success(answer)
    }
}