package com.example.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogs.model.Dog

class ListViewModel : ViewModel() {
    val dogs = MutableLiveData<List<Dog>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        val dog1 = Dog("Corgi", "15 years", "trertgh", "esg", "rgfes", "srgse")
        val dog2 = Dog("Labrador", "15 years", "trertgh", "esg", "rgfes", "srgse")
        val dog3 = Dog("rotwailer", "15 years", "trertgh", "esg", "rgfes", "srgse")
        val dogList = arrayListOf<Dog>(dog1, dog2, dog3)

        dogs.value = dogList
        dogsLoadError.value = false;
        loading.value = false;
    }
}