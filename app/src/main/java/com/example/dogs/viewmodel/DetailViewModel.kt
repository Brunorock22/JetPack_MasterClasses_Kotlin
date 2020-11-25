package com.example.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogs.model.Dog

class DetailViewModel : ViewModel() {

    val dogLiveDate = MutableLiveData<Dog>()

    fun fetch() {
        val dog1 = Dog("Corgi", "15 years", "trertgh", "esg", "rgfes", "srgse")
        dogLiveDate.value = dog1
    }
}