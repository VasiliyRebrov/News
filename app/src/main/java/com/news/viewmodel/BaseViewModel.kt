package com.news.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.data.Repository
import com.news.components.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel<T>(application: Application) : AndroidViewModel(application) {

    private val correspondenceLD = SingleLiveEvent<Resource<T>>()
    fun getCorrespondenceLD(): LiveData<Resource<T>> = correspondenceLD

    private val progressLD = MutableLiveData<Status>(Status.COMPLETED)
    fun getProgressLD(): LiveData<Status> = progressLD


    protected fun runCoroutine(block: suspend () -> T) {
        progressLD.value = Status.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = block()
                correspondenceLD.postValue(Resource(result, null))
            } catch (exception: Exception) {
                correspondenceLD.postValue(Resource(null, exception))
            } finally {
                progressLD.postValue(Status.COMPLETED)
            }
        }
    }

    abstract fun refreshData()
}