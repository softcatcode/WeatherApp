@file:Suppress("UNCHECKED_CAST")

package com.softcat.weatherapp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.softcat.database.facade.DatabaseFacade
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito.any
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull

object MockCreator {

    private val store = mutableMapOf<String, String>()
    private val favourites = mutableMapOf<String, MutableSet<Int>>()

    private fun getMutablePreferences(): MutablePreferences {
        store.clear()
        val preferences: MutablePreferences = mock(MutablePreferences::class.java)
        `when`(preferences[any() as Preferences.Key<String>]).thenAnswer { invocation ->
            val key = invocation.arguments.first() as Preferences.Key<String>
            return@thenAnswer store[key.name]
        }
        `when`(
            preferences.set(any() as Preferences.Key<String>,
            anyString())
        ).thenAnswer { invocation ->
            val key = invocation.arguments[0] as Preferences.Key<String>
            val value = invocation.arguments[1] as String
            store[key.name] = value
            return@thenAnswer Unit
        }
        return preferences
    }

    fun getDataStoreMock(): DataStore<Preferences> = runBlocking {
        val ds = mock(DataStore::class.java) as DataStore<Preferences>
        val preferences = getMutablePreferences()
        runBlocking {
            `when`(ds.edit(anyOrNull())).thenAnswer { invocation ->
                val callback = invocation.arguments.first() as (Preferences) -> Unit
                return@thenAnswer callback.invoke(preferences)
            }
        }
        return@runBlocking ds
    }

    fun getDatabaseMock(): DatabaseFacade = runBlocking {
        val db = mock(DatabaseFacade::class.java)
        favourites.clear()
        `when`(db.addToFavourites(anyString(), anyInt())).thenAnswer { invocation ->
            val userId = invocation.arguments[0] as String
            val cityId = invocation.arguments[1] as Int
            favourites[userId]?.add(cityId)
        }
        `when`(db.removeFromFavourites(anyString(), anyInt())).thenAnswer { invocation ->
            val userId = invocation.arguments[0] as String
            val cityId = invocation.arguments[1] as Int
            favourites[userId]?.remove(cityId)
        }
        `when`(db.isFavourite(anyString(), anyInt())).thenAnswer { invocation ->
            val userId = invocation.arguments[0] as String
            val cityId = invocation.arguments[1] as Int
            return@thenAnswer cityId in (favourites[userId] ?: emptySet())
        }
        `when`(db.getFavouriteCities(anyString())).thenAnswer { invocation ->
            val userId = invocation.arguments[0] as String
            return@thenAnswer favourites[userId]
        }
        return@runBlocking db
    }
}