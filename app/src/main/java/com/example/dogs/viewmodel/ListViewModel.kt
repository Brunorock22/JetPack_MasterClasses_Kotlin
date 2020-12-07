package com.example.dogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.dogs.model.Dog
import com.example.dogs.model.DogDataBase
import com.example.dogs.model.DogsApiService
import com.example.dogs.util.NotificationsHelper
import com.example.dogs.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class ListViewModel(application: Application) : BaseViewModel(application) {
    private val disposable = CompositeDisposable()
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L
    private var prefHelper = SharedPreferencesHelper(getApplication())
    val dogs = MutableLiveData<List<Dog>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        checkCacheDuration()
        NotificationsHelper(getApplication()).createNotification()

        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDataBase()
        } else {

            fetchFromRemote()
        }
    }

    private fun checkCacheDuration() {
        val cachePreferencesInt = prefHelper.getCacheDuration()

        try {
            val cachePreferenceInt = cachePreferencesInt?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun fetchFromDataBase() {
        loading.value = true
        launch {
            val dogs = DogDataBase(getApplication()).dogDao().getAllDogs()
            dogsRetrived(dogs)
            Toast.makeText(getApplication(), "Dogs retrived from database", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            DogsApiService.service.getDogs().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Dog>>() {
                    override fun onSuccess(dogsList: List<Dog>) {
                        storeDogsLocally(dogsList)
                        Toast.makeText(
                            getApplication(),
                            "Dogs retrived from back-end",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                    }

                })
        )
    }

    private fun dogsRetrived(dogList: List<Dog>) {
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    private fun storeDogsLocally(list: List<Dog>) {
        launch {
            val dao = DogDataBase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uui = result[i].toInt()
                i++
            }
            dogsRetrived(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }
}