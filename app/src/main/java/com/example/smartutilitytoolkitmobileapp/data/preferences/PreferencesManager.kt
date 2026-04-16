package com.example.smartutilitytoolkitmobileapp.data.preferences


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    private val context: Context
) {
    companion object {
        private val TASKS_TUTORIAL_SHOWN = booleanPreferencesKey("tasks_tutorial_shown")
        private val CONVERTER_TUTORIAL_SHOWN = booleanPreferencesKey("converter_tutorial_shown")
        private val BMI_TUTORIAL_SHOWN = booleanPreferencesKey("bmi_tutorial_shown")
    }

    fun hasSeenTasksTutorial(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[TASKS_TUTORIAL_SHOWN] ?: false
        }
    }

    suspend fun setTasksTutorialShown() {
        context.dataStore.edit { preferences ->
            preferences[TASKS_TUTORIAL_SHOWN] = true
        }
    }

    fun hasSeenConverterTutorial(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[CONVERTER_TUTORIAL_SHOWN] ?: false
        }
    }

    suspend fun setConverterTutorialShown() {
        context.dataStore.edit { preferences ->
            preferences[CONVERTER_TUTORIAL_SHOWN] = true
        }
    }

    fun hasSeenBMITutorial(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[BMI_TUTORIAL_SHOWN] ?: false
        }
    }

    suspend fun setBMITutorialShown() {
        context.dataStore.edit { preferences ->
            preferences[BMI_TUTORIAL_SHOWN] = true
        }
    }
}