package com.example.dogs.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Dog(
    @ColumnInfo(name = "breed_id")
    @SerializedName("id")
    val breedId: String?,
    @ColumnInfo(name = "dog_name")
    @SerializedName("name")
    val dogBreed: String?,
    @ColumnInfo(name = "life_span")
    @SerializedName("life_span")
    val lifeSpan: String?,
    @ColumnInfo(name = "bred_group")
    @SerializedName("bred_group")
    val bredGrop: String?,
    @ColumnInfo(name = "bred_for")
    @SerializedName("bred_for")
    val bredFor: String?,
    @ColumnInfo(name = "temperament")
    @SerializedName("temperament")
    val temperament: String?,
    @ColumnInfo(name = "dog_url")
    @SerializedName("url")
    val imgUrl: String?
) {
    @PrimaryKey(autoGenerate = true)
    var uui: Int = 0
}

data class DogPalette(var color: Int)

data class SmsInfo(
    var to: String,
    var text: String?,
    var imageUrl: String?
)