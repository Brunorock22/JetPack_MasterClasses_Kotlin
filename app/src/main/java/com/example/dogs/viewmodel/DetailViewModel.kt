package com.example.dogs.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogs.model.Dog
import com.example.dogs.model.DogDataBase
import com.example.dogs.model.DogsApiService
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {

    val dogLiveDate = MutableLiveData<Dog>()
    val progressingLiveDate = MutableLiveData<Boolean>()


    fun fetch(uuid: Int) {
        progressingLiveDate.value = true
        launch {
            val dao = DogDataBase(getApplication()).dogDao().getDog(uuid)
            dogLiveDate.value = dao

        }
    }
}