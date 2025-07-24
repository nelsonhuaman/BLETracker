package com.danp.bletracker.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences @Inject constructor(private val context: Context) {
    private val UUID_KEY = stringPreferencesKey("user_uuid")
    private val DNI_KEY = stringPreferencesKey("user_dni")

    val obtenerUuid: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[UUID_KEY] ?: ""
        }

    val obtenerDni: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[DNI_KEY] ?: ""
        }

    suspend fun guardarUuid(uuid: String) {
        context.dataStore.edit { it[UUID_KEY] = uuid }
    }

    suspend fun guardarDni(dni: String) {
        context.dataStore.edit { it[DNI_KEY] = dni }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}