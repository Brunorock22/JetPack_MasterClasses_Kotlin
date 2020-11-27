package com.example.dogs.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.example.dogs.model.DogDataBase

class SharedPreferencesHelper {

    companion object {

        private var prefes: SharedPreferences? = null
        private const val PREF_TIME = "Pref_time"

        @Volatile
        private var instance: SharedPreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance ?: buildHelper(context).also {
                    instance = it
                }
            }


        private fun buildHelper(context: Context): SharedPreferencesHelper {
            prefes = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesHelper()
        }
    }

    fun saveUpdateTime(time: Long) {
        prefes?.edit(commit = true) {
            putLong(PREF_TIME, time)

        }
    }

    fun getUpdateTime(): Long {
        var update = prefes?.getLong(PREF_TIME, 0)
        update?.let {
            return it
        }
        return 0L
    }
}