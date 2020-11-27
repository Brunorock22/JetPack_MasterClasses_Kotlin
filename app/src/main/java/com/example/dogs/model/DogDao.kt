package com.example.dogs.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DogDao {
    @Insert
    suspend fun insertAll(vararg dogs: Dog): List<Long>

    @Query("SELECT * FROM  dog")
    suspend fun getAllDogs(): List<Dog>

    @Query("SELECT * FROM dog WHERE uui = :dogId")
    suspend fun getDog(dogId: Int): Dog

    @Query("DELETE FROM dog")
    suspend fun deleteAllDogs()
}